SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- Tạo cơ sở dữ liệu
CREATE SCHEMA IF NOT EXISTS `bookstore` DEFAULT CHARACTER SET utf8;
USE `bookstore`;

-- Tạo bảng Category (Danh mục sách)
CREATE TABLE Category (
    CategoryID INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(50) NOT NULL
);

-- Tạo bảng Book (Sách)
CREATE TABLE Book (
    BookID INT PRIMARY KEY AUTO_INCREMENT,
    Title VARCHAR(100) NOT NULL,
    Author VARCHAR(50),
    Genre VARCHAR(30),
    Price DECIMAL(10, 2) NOT NULL,
    StockQuantity INT NOT NULL DEFAULT 0,
    CategoryID INT,
    ImagePath VARCHAR(255) NULL,
    FOREIGN KEY (CategoryID) REFERENCES Category(CategoryID)
);

-- Tạo bảng Customer (Khách hàng)
CREATE TABLE Customer (
    CustomerID INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(50) NOT NULL,
    Address VARCHAR(200),
    Phone VARCHAR(15),
    Email VARCHAR(50)
);

-- Tạo bảng Employee (Nhân viên)
CREATE TABLE Employee (
    EmployeeID INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(50) NOT NULL,
    Status VARCHAR(20) NOT NULL DEFAULT 'Active',
    Phone VARCHAR(15),
    Email VARCHAR(50)
);

-- Tạo bảng Role (Vai trò)
CREATE TABLE Role (
    RoleID INT PRIMARY KEY AUTO_INCREMENT,
    RoleName VARCHAR(30) NOT NULL UNIQUE
);

-- Tạo bảng Permission (Quyền)
CREATE TABLE Permission (
    PermissionID INT PRIMARY KEY AUTO_INCREMENT,
    PermissionName VARCHAR(50) NOT NULL UNIQUE
);

-- Tạo bảng RolePermission (Phân quyền vai trò)
CREATE TABLE RolePermission (
    RoleID INT,
    PermissionID INT,
    PRIMARY KEY (RoleID, PermissionID),
    FOREIGN KEY (RoleID) REFERENCES Role(RoleID),
    FOREIGN KEY (PermissionID) REFERENCES Permission(PermissionID)
);

-- Tạo bảng Account (Tài khoản)
CREATE TABLE Account (
    AccountID INT PRIMARY KEY AUTO_INCREMENT,
    Username VARCHAR(30) NOT NULL UNIQUE,
    Password VARCHAR(50) NOT NULL,
    RoleID INT,
    CustomerID INT NULL,
    EmployeeID INT NULL,
    FOREIGN KEY (RoleID) REFERENCES Role(RoleID),
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID),
    FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID)
);

-- Tạo bảng Order (Đơn hàng)
CREATE TABLE `Order` (
    OrderID INT PRIMARY KEY AUTO_INCREMENT,
    CustomerID INT,
    OrderDate DATETIME NOT NULL DEFAULT NOW(),
    TotalAmount DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    Status VARCHAR(20) NOT NULL DEFAULT 'Pending',
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID)
);

-- Tạo bảng OrderDetail (Chi tiết đơn hàng)
CREATE TABLE OrderDetail (
    OrderID INT,
    BookID INT,
    Quantity INT NOT NULL,
    UnitPrice DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (OrderID, BookID),
    FOREIGN KEY (OrderID) REFERENCES `Order`(OrderID),
    FOREIGN KEY (BookID) REFERENCES Book(BookID)
);

-- Tạo bảng ImportSlip (Phiếu nhập kho)
CREATE TABLE ImportSlip (
    SlipID INT PRIMARY KEY AUTO_INCREMENT,
    EmployeeID INT,
    ImportDate DATETIME NOT NULL DEFAULT NOW(),
    TotalAmount DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID)
);

-- Tạo bảng ImportSlipDetail (Chi tiết phiếu nhập)
CREATE TABLE ImportSlipDetail (
    SlipID INT,
    BookID INT,
    Quantity INT NOT NULL,
    UnitPrice DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (SlipID, BookID),
    FOREIGN KEY (SlipID) REFERENCES ImportSlip(SlipID),
    FOREIGN KEY (BookID) REFERENCES Book(BookID)
);

-- Tạo bảng Promotion (Khuyến mãi)
CREATE TABLE Promotion (
    PromotionID INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(50) NOT NULL,
    DiscountPercent DECIMAL(5, 2) NOT NULL,
    StartDate DATE NOT NULL,
    EndDate DATE NOT NULL,
    CategoryID INT NULL,
    FOREIGN KEY (CategoryID) REFERENCES Category(CategoryID)
);

-- Tạo bảng PromotionBook (Khuyến mãi áp dụng cho sách cụ thể)
CREATE TABLE PromotionBook (
    PromotionID INT,
    BookID INT,
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

-- Trigger hạ Book.StockQuantity ngay khi đặt đơn hàng (Pending)
DELIMITER //
CREATE TRIGGER ReserveStockAfterOrder
AFTER INSERT ON OrderDetail
FOR EACH ROW
BEGIN
    UPDATE Book
    SET StockQuantity = StockQuantity - NEW.Quantity
    WHERE BookID = NEW.BookID;
END //
DELIMITER ;

-- Trigger hạ Warehouse.Quantity khi đơn được xác nhận (Confirmed)
DELIMITER //
CREATE TRIGGER UpdateWarehouseAfterConfirm
AFTER UPDATE ON `Order`
FOR EACH ROW
BEGIN
    IF OLD.Status = 'Pending' AND NEW.Status = 'Confirmed' THEN
        UPDATE Warehouse w
        JOIN OrderDetail od ON w.BookID = od.BookID
        SET w.Quantity = w.Quantity - od.Quantity,
            LastUpdated = NOW()
        WHERE od.OrderID = NEW.OrderID;
    END IF;
END //
DELIMITER ;

-- Trigger khôi phục Book.StockQuantity khi hủy đơn (Pending → Cancelled)
DELIMITER //
CREATE TRIGGER RestoreStockAfterCancelPending
AFTER UPDATE ON `Order`
FOR EACH ROW
BEGIN
    IF OLD.Status = 'Pending' AND NEW.Status = 'Cancelled' THEN
        UPDATE Book b
        JOIN OrderDetail od ON b.BookID = od.BookID
        SET b.StockQuantity = b.StockQuantity + od.Quantity
        WHERE od.OrderID = NEW.OrderID;
    END IF;
END //
DELIMITER ;

-- Trigger khôi phục cả Book.StockQuantity và Warehouse.Quantity khi hủy đơn (Confirmed → Cancelled)
DELIMITER //
CREATE TRIGGER RestoreStockAfterCancelConfirmed
AFTER UPDATE ON `Order`
FOR EACH ROW
BEGIN
    IF OLD.Status = 'Confirmed' AND NEW.Status = 'Cancelled' THEN
        UPDATE Book b
        JOIN OrderDetail od ON b.BookID = od.BookID
        SET b.StockQuantity = b.StockQuantity + od.Quantity
        WHERE od.OrderID = NEW.OrderID;
        
        UPDATE Warehouse w
        JOIN OrderDetail od ON w.BookID = od.BookID
        SET w.Quantity = w.Quantity + od.Quantity,
            LastUpdated = NOW()
        WHERE od.OrderID = NEW.OrderID;
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
    VALUES (NEW.SlipID, NEW.BookID, NEW.Quantity, NOW())
    ON DUPLICATE KEY UPDATE
    Quantity = Quantity + NEW.Quantity,
    LastUpdated = NOW();
END //
DELIMITER ;

-- Trigger xử lý xóa tài khoản (chỉ cập nhật RoleID hoặc Status)
DELIMITER //
CREATE TRIGGER BeforeDeleteAccountUpdateRole
BEFORE DELETE ON Account
FOR EACH ROW
BEGIN
    IF OLD.EmployeeID IS NOT NULL THEN
        UPDATE Employee
        SET Status = 'Inactive'
        WHERE EmployeeID = OLD.EmployeeID;
        
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Không thể xóa tài khoản nhân viên, chỉ cập nhật trạng thái thành Inactive.';
    END IF;
    
    IF OLD.CustomerID IS NOT NULL THEN
        UPDATE Account
        SET RoleID = NULL
        WHERE AccountID = OLD.AccountID;
        
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Không thể xóa tài khoản khách hàng, chỉ xóa RoleID.';
    END IF;
END //
DELIMITER ;

-- Thêm dữ liệu mẫu
INSERT INTO Category (Name) VALUES 
('Văn học'), 
('Khoa học');

INSERT INTO Book (Title, Author, Genre, Price, StockQuantity, CategoryID, ImagePath) VALUES 
('Harry Potter', 'J.K. Rowling', 'Fantasy', 15.99, 100, 1, '/images/1.jpg'),
('The Hobbit', 'J.R.R. Tolkien', 'Fantasy', 12.99, 50, 1, '/images/2.jpg');

INSERT INTO Customer (Name, Address, Phone, Email) VALUES 
('Nguyen Van A', '123 Hanoi', '0901234567', 'nva@gmail.com');

INSERT INTO Employee (Name, Status, Phone, Email) VALUES 
('Tran Thi B', 'Active', '0912345678', 'ttb@gmail.com');

INSERT INTO Role (RoleName) VALUES 
('Customer'), 
('Sales'), 
('Warehouse'), 
('Director'), 
('Admin');

INSERT INTO Permission (PermissionName) VALUES 
('ViewOrder'), 
('ManageOrder'), 
('ManageStock'), 
('CreatePromotion'), 
('ManageUser');

INSERT INTO RolePermission (RoleID, PermissionID) VALUES 
(1, 1), 
(2, 1), (2, 2), 
(3, 3), 
(4, 4), 
(5, 5);

INSERT INTO Account (Username, Password, RoleID, CustomerID, EmployeeID) VALUES 
('nva', 'pass123', 1, 1, NULL), 
('ttb', 'pass456', 3, NULL, 1);

INSERT INTO Cart (CustomerID) VALUES 
(1);

INSERT INTO CartItem (CartID, BookID, Quantity) VALUES 
(1, 1, 2);

INSERT INTO ImportSlip (EmployeeID, ImportDate, TotalAmount) VALUES 
(1, '2025-02-27 10:00:00', 159.90);

INSERT INTO ImportSlipDetail (SlipID, BookID, Quantity, UnitPrice) VALUES 
(1, 1, 10, 15.99);

INSERT INTO Promotion (Name, DiscountPercent, StartDate, EndDate, CategoryID) VALUES 
('Giảm giá Văn học', 20.00, '2025-03-01', '2025-03-07', 1);