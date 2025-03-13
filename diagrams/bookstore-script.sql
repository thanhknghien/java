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
    FOREIGN KEY (RoleID) REFERENCES Role(RoleID) ON DELETE CASCADE,
    FOREIGN KEY (PermissionID) REFERENCES Permission(PermissionID) ON DELETE CASCADE
);

-- Tạo bảng Account (Tài khoản)
CREATE TABLE Account (
    AccountID INT PRIMARY KEY AUTO_INCREMENT,
    Username VARCHAR(30) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    RoleID INT,
    CustomerID INT NULL,
    EmployeeID INT NULL,
    FOREIGN KEY (RoleID) REFERENCES Role(RoleID) ON DELETE SET NULL,
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID) ON DELETE CASCADE,
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
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID) ON DELETE CASCADE,
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
----------------------
-- Dữ liệu mẫu--------
----------------------

-- Table Role
INSERT INTO Role (RoleName) VALUES 
('Admin'),
('Manager'),
('Warehouse Staff'),  
('Invoice Staff'),    
('Customer');

-- Table Permission
INSERT INTO Permission (PermissionName) VALUES 
('Manage Users'),       -- Quản lý tài khoản
('Manage Books'),       -- Quản lý sách
('Manage Orders'),      -- Quản lý đơn hàng
('Manage Inventory'),   -- Quản lý kho sách
('View Reports'),       -- Xem báo cáo
('Purchase Books');     -- Khách hàng mua sách

-- Table RolePermission
-- Admin có tất cả quyền
INSERT INTO RolePermission (RoleID, PermissionID)
SELECT 1, PermissionID FROM Permission;

-- Manager có quyền quản lý sách, đơn hàng, kho và báo cáo
INSERT INTO RolePermission (RoleID, PermissionID) VALUES 
(2, 2), -- Manage Books
(2, 3), -- Manage Orders
(2, 4), -- Manage Inventory
(2, 5); -- View Reports

-- Nhân viên quản lý kho chỉ có quyền quản lý kho sách
INSERT INTO RolePermission (RoleID, PermissionID) VALUES 
(3, 4); -- Manage Inventory

-- Nhân viên quản lý hóa đơn chỉ có quyền quản lý đơn hàng
INSERT INTO RolePermission (RoleID, PermissionID) VALUES 
(4, 3); -- Manage Orders

-- Khách hàng chỉ có quyền mua sách
INSERT INTO RolePermission (RoleID, PermissionID) VALUES 
(5, 6); -- Purchase Books

