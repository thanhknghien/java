SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- Tạo cơ sở dữ liệu
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8;
USE `mydb`;

-- Tạo bảng Category (Danh mục sách)
CREATE TABLE Category (
    CategoryID VARCHAR(10) PRIMARY KEY,
    Name VARCHAR(50) NOT NULL
);

-- Tạo bảng Book (Sách)
CREATE TABLE Book (
    BookID VARCHAR(10) PRIMARY KEY,
    Title VARCHAR(100) NOT NULL,
    Author VARCHAR(50),
    Genre VARCHAR(30),
    Price DECIMAL(10, 2) NOT NULL,
    StockQuantity INT NOT NULL DEFAULT 0,
    CategoryID VARCHAR(10),
    FOREIGN KEY (CategoryID) REFERENCES Category(CategoryID)
);

-- Tạo bảng Customer (Khách hàng)
CREATE TABLE Customer (
    CustomerID VARCHAR(10) PRIMARY KEY,
    Name VARCHAR(50) NOT NULL,
    Address VARCHAR(200),
    Phone VARCHAR(15),
    Email VARCHAR(50)
);

-- Tạo bảng Employee (Nhân viên)
CREATE TABLE Employee (
    EmployeeID VARCHAR(10) PRIMARY KEY,
    Name VARCHAR(50) NOT NULL,
    Status VARCHAR(20) NOT NULL DEFAULT 'Active', -- Trạng thái: Active, Inactive, OnLeave
    Phone VARCHAR(15),
    Email VARCHAR(50)
);

-- Tạo bảng Role (Vai trò)
CREATE TABLE Role (
    RoleID VARCHAR(10) PRIMARY KEY,
    RoleName VARCHAR(30) NOT NULL UNIQUE
);

-- Tạo bảng Permission (Quyền)
CREATE TABLE Permission (
    PermissionID VARCHAR(10) PRIMARY KEY,
    PermissionName VARCHAR(50) NOT NULL UNIQUE
);

-- Tạo bảng RolePermission (Phân quyền vai trò)
CREATE TABLE RolePermission (
    RoleID VARCHAR(10),
    PermissionID VARCHAR(10),
    PRIMARY KEY (RoleID, PermissionID),
    FOREIGN KEY (RoleID) REFERENCES Role(RoleID),
    FOREIGN KEY (PermissionID) REFERENCES Permission(PermissionID)
);

-- Tạo bảng Account (Tài khoản)
CREATE TABLE Account (
    AccountID VARCHAR(10) PRIMARY KEY,
    Username VARCHAR(30) NOT NULL UNIQUE,
    Password VARCHAR(50) NOT NULL,
    RoleID VARCHAR(10),
    CustomerID VARCHAR(10) NULL,
    EmployeeID VARCHAR(10) NULL,
    FOREIGN KEY (RoleID) REFERENCES Role(RoleID),
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID),
    FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID)
);

-- Tạo bảng Cart (Giỏ hàng)
CREATE TABLE Cart (
    CartID VARCHAR(10) PRIMARY KEY,
    CustomerID VARCHAR(10) NOT NULL,
    CreatedDate DATETIME NOT NULL DEFAULT NOW(),
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID)
);

-- Tạo bảng CartItem (Chi tiết giỏ hàng)
CREATE TABLE CartItem (
    CartID VARCHAR(10),
    BookID VARCHAR(10),
    Quantity INT NOT NULL DEFAULT 1,
    PRIMARY KEY (CartID, BookID),
    FOREIGN KEY (CartID) REFERENCES Cart(CartID),
    FOREIGN KEY (BookID) REFERENCES Book(BookID)
);

-- Tạo bảng Order (Đơn hàng)
CREATE TABLE `Order` (
    OrderID VARCHAR(10) PRIMARY KEY,
    CustomerID VARCHAR(10),
    OrderDate DATETIME NOT NULL DEFAULT NOW(),
    TotalAmount DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    Status VARCHAR(20) NOT NULL DEFAULT 'Pending', -- Trạng thái: Pending, Confirmed, Cancelled
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID)
);

-- Tạo bảng OrderDetail (Chi tiết đơn hàng)
CREATE TABLE OrderDetail (
    OrderID VARCHAR(10),
    BookID VARCHAR(10),
    Quantity INT NOT NULL,
    UnitPrice DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (OrderID, BookID),
    FOREIGN KEY (OrderID) REFERENCES `Order`(OrderID),
    FOREIGN KEY (BookID) REFERENCES Book(BookID)
);

-- Tạo bảng Warehouse (Kho hàng)
CREATE TABLE Warehouse (
    WarehouseID VARCHAR(10) PRIMARY KEY,
    BookID VARCHAR(10),
    Quantity INT NOT NULL DEFAULT 0,
    LastUpdated DATETIME NOT NULL DEFAULT NOW(),
    FOREIGN KEY (BookID) REFERENCES Book(BookID)
);

-- Tạo bảng ImportSlip (Phiếu nhập kho)
CREATE TABLE ImportSlip (
    SlipID VARCHAR(10) PRIMARY KEY,
    EmployeeID VARCHAR(10),
    ImportDate DATETIME NOT NULL DEFAULT NOW(),
    TotalAmount DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID)
);

-- Tạo bảng ImportSlipDetail (Chi tiết phiếu nhập)
CREATE TABLE ImportSlipDetail (
    SlipID VARCHAR(10),
    BookID VARCHAR(10),
    Quantity INT NOT NULL,
    UnitPrice DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (SlipID, BookID),
    FOREIGN KEY (SlipID) REFERENCES ImportSlip(SlipID),
    FOREIGN KEY (BookID) REFERENCES Book(BookID)
);

-- Tạo bảng Promotion (Khuyến mãi)
CREATE TABLE Promotion (
    PromotionID VARCHAR(10) PRIMARY KEY,
    Name VARCHAR(50) NOT NULL,
    DiscountPercent DECIMAL(5, 2) NOT NULL,
    StartDate DATE NOT NULL,
    EndDate DATE NOT NULL,
    CategoryID VARCHAR(10) NULL,
    FOREIGN KEY (CategoryID) REFERENCES Category(CategoryID)
);

-- Tạo bảng PromotionBook (Khuyến mãi áp dụng cho sách cụ thể)
CREATE TABLE PromotionBook (
    PromotionID VARCHAR(10),
    BookID VARCHAR(10),
    PRIMARY KEY (PromotionID, BookID),
    FOREIGN KEY (PromotionID) REFERENCES Promotion(PromotionID),
    FOREIGN KEY (BookID) REFERENCES Book(BookID)
);

-- Trigger kiểm tra tồn kho trước khi thêm vào giỏ hàng
DELIMITER //
CREATE TRIGGER CheckStockBeforeAddToCart
BEFORE INSERT ON CartItem
FOR EACH ROW
BEGIN
    DECLARE stock INT;
    SELECT StockQuantity INTO stock
    FROM Book
    WHERE BookID = NEW.BookID;
    
    IF stock < NEW.Quantity THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Số lượng tồn kho không đủ để thêm vào giỏ hàng.';
    END IF;
END //
DELIMITER ;

-- Trigger kiểm tra tồn kho trước khi cập nhật giỏ hàng
DELIMITER //
CREATE TRIGGER CheckStockBeforeUpdateCart
BEFORE UPDATE ON CartItem
FOR EACH ROW
BEGIN
    DECLARE stock INT;
    SELECT StockQuantity INTO stock
    FROM Book
    WHERE BookID = NEW.BookID;
    
    IF stock < NEW.Quantity THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Số lượng tồn kho không đủ để cập nhật giỏ hàng.';
    END IF;
END //
DELIMITER ;

-- Trigger kiểm tra tồn kho trước khi bán (OrderDetail)
DELIMITER //
CREATE TRIGGER CheckStockBeforeOrder
BEFORE INSERT ON OrderDetail
FOR EACH ROW
BEGIN
    DECLARE stock INT;
    SELECT StockQuantity INTO stock
    FROM Book
    WHERE BookID = NEW.BookID;
    
    IF stock < NEW.Quantity THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Số lượng tồn kho không đủ để thực hiện đơn hàng.';
    END IF;
END //
DELIMITER ;

-- Trigger cập nhật tồn kho sau khi bán
DELIMITER //
CREATE TRIGGER UpdateStockAfterOrder
AFTER INSERT ON OrderDetail
FOR EACH ROW
BEGIN
    UPDATE Book
    SET StockQuantity = StockQuantity - NEW.Quantity
    WHERE BookID = NEW.BookID;
END //
DELIMITER ;

-- Trigger tính tổng tiền đơn hàng
DELIMITER //
CREATE TRIGGER CalculateOrderTotal
AFTER INSERT ON OrderDetail
FOR EACH ROW
BEGIN
    DECLARE total DECIMAL(10, 2);
    SELECT SUM(Quantity * UnitPrice) INTO total
    FROM OrderDetail
    WHERE OrderID = NEW.OrderID;
    
    UPDATE `Order`
    SET TotalAmount = total
    WHERE OrderID = NEW.OrderID;
END //
DELIMITER ;

-- Trigger áp dụng khuyến mãi khi thêm vào đơn hàng
DELIMITER //
CREATE TRIGGER ApplyPromotionBeforeOrder
BEFORE INSERT ON OrderDetail
FOR EACH ROW
BEGIN
    DECLARE discount DECIMAL(5, 2);
    SELECT p.DiscountPercent INTO discount
    FROM Promotion p
    JOIN Book b ON (p.CategoryID = b.CategoryID OR EXISTS (
        SELECT 1 FROM PromotionBook pb WHERE pb.PromotionID = p.PromotionID AND pb.BookID = NEW.BookID
    ))
    WHERE NEW.BookID = b.BookID
    AND CURDATE() BETWEEN p.StartDate AND p.EndDate
    LIMIT 1;
    
    IF discount IS NOT NULL THEN
        SET NEW.UnitPrice = NEW.UnitPrice * (1 - discount / 100);
    END IF;
END //
DELIMITER ;

-- Trigger kiểm tra trạng thái nhân viên trước khi nhập kho
DELIMITER //
CREATE TRIGGER CheckEmployeeStatusBeforeImport
BEFORE INSERT ON ImportSlip
FOR EACH ROW
BEGIN
    DECLARE emp_status VARCHAR(20);
    SELECT Status INTO emp_status
    FROM Employee
    WHERE EmployeeID = NEW.EmployeeID;
    
    IF emp_status != 'Active' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Nhân viên không ở trạng thái Active, không thể tạo phiếu nhập.';
    END IF;
END //
DELIMITER ;

-- Trigger cập nhật tồn kho sau khi nhập kho
DELIMITER //
CREATE TRIGGER UpdateStockAfterImport
AFTER INSERT ON ImportSlipDetail
FOR EACH ROW
BEGIN
    UPDATE Book
    SET StockQuantity = StockQuantity + NEW.Quantity
    WHERE BookID = NEW.BookID;
    
    INSERT INTO Warehouse (WarehouseID, BookID, Quantity, LastUpdated)
    VALUES (CONCAT('WH', NEW.SlipID), NEW.BookID, NEW.Quantity, NOW())
    ON DUPLICATE KEY UPDATE
    Quantity = Quantity + NEW.Quantity,
    LastUpdated = NOW();
END //
DELIMITER ;

-- Trigger tính tổng tiền phiếu nhập
DELIMITER //
CREATE TRIGGER CalculateImportTotal
AFTER INSERT ON ImportSlipDetail
FOR EACH ROW
BEGIN
    DECLARE total DECIMAL(10, 2);
    SELECT SUM(Quantity * UnitPrice) INTO total
    FROM ImportSlipDetail
    WHERE SlipID = NEW.SlipID;
    
    UPDATE ImportSlip
    SET TotalAmount = total
    WHERE SlipID = NEW.SlipID;
END //
DELIMITER ;

-- Thêm dữ liệu mẫu
INSERT INTO Category (CategoryID, Name) VALUES 
('CAT01', 'Văn học'), 
('CAT02', 'Khoa học');

INSERT INTO Book (BookID, Title, Author, Genre, Price, StockQuantity, CategoryID) VALUES 
('B001', 'Harry Potter', 'J.K. Rowling', 'Fantasy', 15.99, 100, 'CAT01');

INSERT INTO Customer (CustomerID, Name, Address, Phone, Email) VALUES 
('C001', 'Nguyen Van A', '123 Hanoi', '0901234567', 'nva@gmail.com');

INSERT INTO Employee (EmployeeID, Name, Status, Phone, Email) VALUES 
('E001', 'Tran Thi B', 'Active', '0912345678', 'ttb@gmail.com');

INSERT INTO Role (RoleID, RoleName) VALUES 
('R01', 'Customer'), 
('R02', 'Sales'), 
('R03', 'Warehouse'), 
('R04', 'Director'), 
('R05', 'Admin');

INSERT INTO Permission (PermissionID, PermissionName) VALUES 
('P01', 'ViewOrder'), 
('P02', 'ManageOrder'), `role`
('P03', 'ManageStock'), 
('P04', 'CreatePromotion'), 
('P05', 'ManageUser');

INSERT INTO RolePermission (RoleID, PermissionID) VALUES 
('R01', 'P01'), 
('R02', 'P01'), ('R02', 'P02'), 
('R03', 'P03'), 
('R04', 'P04'), 
('R05', 'P05');

INSERT INTO Account (AccountID, Username, Password, RoleID, CustomerID, EmployeeID) VALUES 
('A001', 'nva', 'pass123', 'R01', 'C001', NULL), 
('A002', 'ttb', 'pass456', 'R03', NULL, 'E001');

INSERT INTO Cart (CartID, CustomerID) VALUES 
('CART001', 'C001');

INSERT INTO CartItem (CartID, BookID, Quantity) VALUES 
('CART001', 'B001', 2);

INSERT INTO ImportSlip (SlipID, EmployeeID, ImportDate, TotalAmount) VALUES 
('IS001', 'E001', '2025-02-27 10:00:00', 159.90);

INSERT INTO ImportSlipDetail (SlipID, BookID, Quantity, UnitPrice) VALUES 
('IS001', 'B001', 10, 15.99);

INSERT INTO Promotion (PromotionID, Name, DiscountPercent, StartDate, EndDate, CategoryID) VALUES 
('P001', 'Giảm giá Văn học', 20.00, '2025-03-01', '2025-03-07', 'CAT01');