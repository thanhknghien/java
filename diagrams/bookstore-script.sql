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

-- Tạo bảng nhà cung cấp
CREATE TABLE Supplier (
    SupplierID INT PRIMARY KEY AUTO_INCREMENT,
    SupplierName VARCHAR(50) NOT NULL,
    SupplierNumber VARCHAR(15) NOT NULL,
    SupplierAddress VARCHAR(200) NOT NULL,
    Status VARCHAR(20) NOT NULL DEFAULT 'Active'
);

-- Tạo bảng Book (Sách)
CREATE TABLE Book (
    BookID INT PRIMARY KEY AUTO_INCREMENT,
    Title VARCHAR(100) NOT NULL,
    Author VARCHAR(50),
    Genre VARCHAR(30),
    Price DECIMAL(10,2) NOT NULL,
    StockQuantity INT NOT NULL DEFAULT 0,
    CategoryID INT,
    ImagePath VARCHAR(255) NULL,
    FOREIGN KEY (CategoryID) REFERENCES Category(CategoryID)
);

-- Tạo bảng Employee (Nhân viên)
CREATE TABLE Employee (
    EmployeeID INT PRIMARY KEY AUTO_INCREMENT,
    Status VARCHAR(20) NOT NULL DEFAULT 'Active'
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
    FOREIGN KEY (RoleID) REFERENCES Role(RoleID) ON DELETE CASCADE,
    FOREIGN KEY (PermissionID) REFERENCES Permission(PermissionID) ON DELETE CASCADE
);

-- Tạo bảng Account (Tài khoản)
CREATE TABLE Account (
    AccountID INT PRIMARY KEY AUTO_INCREMENT,
    Username VARCHAR(30) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    FullName VARCHAR(20) NOT NULL,
    Email VARCHAR(50) NOT NULL,
    Phone VARCHAR(15) NOT NULL,
    Address VARCHAR(200) NOT NULL,
    RoleID INT,
    EmployeeID INT NULL,
    FOREIGN KEY (RoleID) REFERENCES Role(RoleID) ON DELETE SET NULL,
    FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID) ON DELETE CASCADE
);

-- Tạo bảng Orders (Đơn hàng)
CREATE TABLE Orders (
    OrderID INT PRIMARY KEY AUTO_INCREMENT,
    EmployeeID INT,
    CustomerID INT,
    OrderDate DATETIME NOT NULL DEFAULT NOW(),
    TotalAmount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    Status VARCHAR(20) NOT NULL DEFAULT 'Pending',
    FOREIGN KEY (CustomerID) REFERENCES Account(AccountID) ON DELETE CASCADE,
    FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID) ON DELETE SET NULL
);

-- Tạo bảng OrderDetail (Chi tiết đơn hàng)
CREATE TABLE OrderDetail (
    OrderID INT,
    BookID INT,
    Quantity INT NOT NULL,
    UnitPrice DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (OrderID, BookID),
    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID) ON DELETE CASCADE,
    FOREIGN KEY (BookID) REFERENCES Book(BookID) ON DELETE CASCADE
);

-- Tạo bảng ImportSlip (Phiếu nhập kho)
CREATE TABLE ImportSlip (
    SlipID INT PRIMARY KEY AUTO_INCREMENT,
    SupplierID INT DEFAULT NULL,
    EmployeeID INT,
    ImportDate DATETIME NOT NULL DEFAULT NOW(),
    TotalAmount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID) ON DELETE SET NULL,
    FOREIGN KEY (SupplierID) REFERENCES Supplier(SupplierID) ON DELETE SET NULL
);

-- Tạo bảng ImportSlipDetail (Chi tiết phiếu nhập)
CREATE TABLE ImportSlipDetail (
    SlipID INT,
    BookID INT,
    Quantity INT NOT NULL,
    UnitPrice DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (SlipID, BookID),
    FOREIGN KEY (SlipID) REFERENCES ImportSlip(SlipID) ON DELETE CASCADE,
    FOREIGN KEY (BookID) REFERENCES Book(BookID) ON DELETE CASCADE
);

-- Tạo bảng Promotion (Khuyến mãi)
CREATE TABLE Promotion (
    PromotionID INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(50) NOT NULL,
    DiscountPercent DECIMAL(5,2) NOT NULL,
    StartDate DATE NOT NULL,
    EndDate DATE NOT NULL,
    CategoryID INT NULL,
    FOREIGN KEY (CategoryID) REFERENCES Category(CategoryID) ON DELETE SET NULL
);

-----Dữ liệu mẫu-------
-- Danh mục sách----
INSERT INTO Category (Name) VALUES 
('Văn học'), 
('Khoa học'), 
('Kinh tế'), 
('Công nghệ'), 
('Thiếu nhi');

INSERT INTO Supplier (SupplierName, SupplierNumber, SupplierAddress) VALUES 
('Nhà sách ABC', '0123456789', '123 Đường A, TP.HCM'),
('Nhà sách XYZ', '0987654321', '456 Đường B, Hà Nội');

INSERT INTO Book (Title, Author, Genre, Price, StockQuantity, CategoryID, ImagePath) VALUES 
('Lập trình Python', 'Nguyễn Văn A', 'Công nghệ', 150000, 10, 4, 'python_book.jpg'),
('Kinh tế học vĩ mô', 'Trần Văn B', 'Kinh tế', 200000, 5, 3, 'macro_economics.jpg'),
('Doraemon tập 1', 'Fujiko F. Fujio', 'Thiếu nhi', 30000, 20, 5, 'doraemon_1.jpg');

INSERT INTO Book (Title, Author, Genre, Price, StockQuantity, CategoryID, ImagePath) VALUES 
('Lập trình Python', 'Nguyễn Văn A', 'Công nghệ', 150000, 10, 4, 'python_book.jpg'),
('Kinh tế học vĩ mô', 'Trần Văn B', 'Kinh tế', 200000, 5, 3, 'macro_economics.jpg'),
('Doraemon tập 1', 'Fujiko F. Fujio', 'Thiếu nhi', 30000, 20, 5, 'doraemon_1.jpg');

INSERT INTO Employee (Status) VALUES 
('Active'),
('Inactive');

INSERT INTO Role (RoleName) VALUES 
('Admin'), 
('Nhân viên bán hàng'), 
('Khách hàng');

INSERT INTO Permission (PermissionName) VALUES 
('Quản lý sách'), 
('Quản lý đơn hàng'), 
('Quản lý tài khoản'), 
('Xem báo cáo');

INSERT INTO RolePermission (RoleID, PermissionID) VALUES 
(1, 1), -- Admin có quyền quản lý sách
(1, 2), -- Admin có quyền quản lý đơn hàng
(1, 3), -- Admin có quyền quản lý tài khoản
(2, 2), -- Nhân viên chỉ có quyền quản lý đơn hàng
(3, 4); -- Khách hàng có quyền xem báo cáo

INSERT INTO Account (Username, Password, FullName, Email, Phone, Address, RoleID, EmployeeID) VALUES 
('admin', 'hashed_password', 'Admin User', 'admin@example.com', '0909123456', '123 Đường Admin', 1, NULL),
('employee01', 'hashed_password', 'Nhân viên 1', 'nv1@example.com', '0912345678', '456 Đường Nhân Viên', 2, 1),
('customer01', 'hashed_password', 'Khách hàng 1', 'kh01@example.com', '0923456789', '789 Đường Khách Hàng', 3, NULL);

INSERT INTO Orders (EmployeeID, CustomerID, OrderDate, TotalAmount, Status) VALUES 
(1, 3, '2025-03-14 10:00:00', 300000, 'Pending');

INSERT INTO OrderDetail (OrderID, BookID, Quantity, UnitPrice) VALUES 
(1, 1, 2, 150000);

INSERT INTO ImportSlip (SupplierID, EmployeeID, ImportDate, TotalAmount) VALUES 
(1, 1, '2025-03-14 08:30:00', 500000);

INSERT INTO ImportSlipDetail (SlipID, BookID, Quantity, UnitPrice) VALUES 
(1, 1, 10, 120000);

INSERT INTO Promotion (Name, DiscountPercent, StartDate, EndDate, CategoryID) VALUES 
('Giảm giá sách công nghệ', 10.00, '2025-03-01', '2025-03-31', 4);
