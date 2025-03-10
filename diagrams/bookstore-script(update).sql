-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th3 10, 2025 lúc 06:17 PM
-- Phiên bản máy phục vụ: 10.5.28-MariaDB
-- Phiên bản PHP: 8.2.12

SET FOREIGN_KEY_CHECKS=0;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `bookstore`
--
CREATE DATABASE IF NOT EXISTS `bookstore` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `bookstore`;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `account`
--

DROP TABLE IF EXISTS `account`;
CREATE TABLE IF NOT EXISTS `account` (
  `AccountID` int(11) NOT NULL AUTO_INCREMENT,
  `Username` varchar(30) NOT NULL,
  `Password` varchar(50) NOT NULL,
  `RoleID` int(11) DEFAULT NULL,
  `CustomerID` int(11) DEFAULT NULL,
  `EmployeeID` int(11) DEFAULT NULL,
  PRIMARY KEY (`AccountID`),
  UNIQUE KEY `Username` (`Username`),
  KEY `RoleID` (`RoleID`),
  KEY `CustomerID` (`CustomerID`),
  KEY `EmployeeID` (`EmployeeID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Bẫy `account`
--
DROP TRIGGER IF EXISTS `BeforeDeleteAccountUpdateRole`;
DELIMITER $$
CREATE TRIGGER `BeforeDeleteAccountUpdateRole` BEFORE DELETE ON `account` FOR EACH ROW BEGIN
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
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `book`
--

DROP TABLE IF EXISTS `book`;
CREATE TABLE IF NOT EXISTS `book` (
  `BookID` int(11) NOT NULL AUTO_INCREMENT,
  `Title` varchar(100) NOT NULL,
  `Author` varchar(50) DEFAULT NULL,
  `Genre` varchar(30) DEFAULT NULL,
  `Price` decimal(10,2) NOT NULL,
  `StockQuantity` int(11) NOT NULL DEFAULT 0,
  `CategoryID` int(11) DEFAULT NULL,
  `ImagePath` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`BookID`),
  KEY `CategoryID` (`CategoryID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `category`
--

DROP TABLE IF EXISTS `category`;
CREATE TABLE IF NOT EXISTS `category` (
  `CategoryID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) NOT NULL,
  PRIMARY KEY (`CategoryID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `customer`
--

DROP TABLE IF EXISTS `customer`;
CREATE TABLE IF NOT EXISTS `customer` (
  `CustomerID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) NOT NULL,
  `Address` varchar(200) DEFAULT NULL,
  `Phone` varchar(15) DEFAULT NULL,
  `Email` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`CustomerID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `employee`
--

DROP TABLE IF EXISTS `employee`;
CREATE TABLE IF NOT EXISTS `employee` (
  `EmployeeID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) NOT NULL,
  `Status` varchar(20) NOT NULL DEFAULT 'Active',
  `Phone` varchar(15) DEFAULT NULL,
  `Email` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`EmployeeID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `importslip`
--

DROP TABLE IF EXISTS `importslip`;
CREATE TABLE IF NOT EXISTS `importslip` (
  `SlipID` int(11) NOT NULL AUTO_INCREMENT,
  `EmployeeID` int(11) DEFAULT NULL,
  `SupplierID` int(11) DEFAULT NULL,
  `ImportDate` datetime NOT NULL DEFAULT current_timestamp(),
  `TotalAmount` decimal(10,2) NOT NULL DEFAULT 0.00,
  PRIMARY KEY (`SlipID`),
  KEY `EmployeeID` (`EmployeeID`),
  KEY `importslip_ibfk_2` (`SupplierID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `importslipdetail`
--

DROP TABLE IF EXISTS `importslipdetail`;
CREATE TABLE IF NOT EXISTS `importslipdetail` (
  `SlipID` int(11) NOT NULL,
  `BookID` int(11) NOT NULL,
  `Quantity` int(11) NOT NULL,
  `UnitPrice` decimal(10,2) NOT NULL,
  PRIMARY KEY (`SlipID`,`BookID`),
  KEY `BookID` (`BookID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Bẫy `importslipdetail`
--
DROP TRIGGER IF EXISTS `UpdateStockAfterImport`;
DELIMITER $$
CREATE TRIGGER `UpdateStockAfterImport` AFTER INSERT ON `importslipdetail` FOR EACH ROW BEGIN
    UPDATE Book
    SET StockQuantity = StockQuantity + NEW.Quantity
    WHERE BookID = NEW.BookID;
    
    INSERT INTO Warehouse (WarehouseID, BookID, Quantity, LastUpdated)
    VALUES (NEW.SlipID, NEW.BookID, NEW.Quantity, NOW())
    ON DUPLICATE KEY UPDATE
    Quantity = Quantity + NEW.Quantity,
    LastUpdated = NOW();
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `order`
--

DROP TABLE IF EXISTS `order`;
CREATE TABLE IF NOT EXISTS `order` (
  `OrderID` int(11) NOT NULL AUTO_INCREMENT,
  `CustomerID` int(11) DEFAULT NULL,
  `OrderDate` datetime NOT NULL DEFAULT current_timestamp(),
  `TotalAmount` decimal(10,2) NOT NULL DEFAULT 0.00,
  `Status` varchar(20) NOT NULL DEFAULT 'Pending',
  PRIMARY KEY (`OrderID`),
  KEY `CustomerID` (`CustomerID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Bẫy `order`
--
DROP TRIGGER IF EXISTS `RestoreStockAfterCancelPending`;
DELIMITER $$
CREATE TRIGGER `RestoreStockAfterCancelPending` AFTER UPDATE ON `order` FOR EACH ROW BEGIN
    IF OLD.Status = 'Pending' AND NEW.Status = 'Cancelled' THEN
        UPDATE Book b
        JOIN OrderDetail od ON b.BookID = od.BookID
        SET b.StockQuantity = b.StockQuantity + od.Quantity
        WHERE od.OrderID = NEW.OrderID;
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `orderdetail`
--

DROP TABLE IF EXISTS `orderdetail`;
CREATE TABLE IF NOT EXISTS `orderdetail` (
  `OrderID` int(11) NOT NULL,
  `BookID` int(11) NOT NULL,
  `Quantity` int(11) NOT NULL,
  `UnitPrice` decimal(10,2) NOT NULL,
  PRIMARY KEY (`OrderID`,`BookID`),
  KEY `BookID` (`BookID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Bẫy `orderdetail`
--
DROP TRIGGER IF EXISTS `CheckStockBeforeOrder`;
DELIMITER $$
CREATE TRIGGER `CheckStockBeforeOrder` BEFORE INSERT ON `orderdetail` FOR EACH ROW BEGIN
    DECLARE stock INT;
    SELECT StockQuantity INTO stock
    FROM Book
    WHERE BookID = NEW.BookID;
    
    IF stock < NEW.Quantity THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Số lượng tồn kho không đủ để thực hiện đơn hàng.';
    END IF;
END
$$
DELIMITER ;
DROP TRIGGER IF EXISTS `ReserveStockAfterOrder`;
DELIMITER $$
CREATE TRIGGER `ReserveStockAfterOrder` AFTER INSERT ON `orderdetail` FOR EACH ROW BEGIN
    UPDATE Book
    SET StockQuantity = StockQuantity - NEW.Quantity
    WHERE BookID = NEW.BookID;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `permission`
--

DROP TABLE IF EXISTS `permission`;
CREATE TABLE IF NOT EXISTS `permission` (
  `PermissionID` int(11) NOT NULL AUTO_INCREMENT,
  `PermissionName` varchar(50) NOT NULL,
  PRIMARY KEY (`PermissionID`),
  UNIQUE KEY `PermissionName` (`PermissionName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `promotion`
--

DROP TABLE IF EXISTS `promotion`;
CREATE TABLE IF NOT EXISTS `promotion` (
  `PromotionID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) NOT NULL,
  `DiscountPercent` decimal(5,2) NOT NULL,
  `StartDate` date NOT NULL,
  `EndDate` date NOT NULL,
  `CategoryID` int(11) DEFAULT NULL,
  PRIMARY KEY (`PromotionID`),
  KEY `CategoryID` (`CategoryID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `promotionbook`
--

DROP TABLE IF EXISTS `promotionbook`;
CREATE TABLE IF NOT EXISTS `promotionbook` (
  `PromotionID` int(11) NOT NULL,
  `BookID` int(11) NOT NULL,
  PRIMARY KEY (`PromotionID`,`BookID`),
  KEY `BookID` (`BookID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `role`
--

DROP TABLE IF EXISTS `role`;
CREATE TABLE IF NOT EXISTS `role` (
  `RoleID` int(11) NOT NULL AUTO_INCREMENT,
  `RoleName` varchar(30) NOT NULL,
  PRIMARY KEY (`RoleID`),
  UNIQUE KEY `RoleName` (`RoleName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `rolepermission`
--

DROP TABLE IF EXISTS `rolepermission`;
CREATE TABLE IF NOT EXISTS `rolepermission` (
  `RoleID` int(11) NOT NULL,
  `PermissionID` int(11) NOT NULL,
  PRIMARY KEY (`RoleID`,`PermissionID`),
  KEY `PermissionID` (`PermissionID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `supplier`
--

DROP TABLE IF EXISTS `supplier`;
CREATE TABLE IF NOT EXISTS `supplier` (
  `SupplierID` int(11) NOT NULL AUTO_INCREMENT,
  `SupplierName` varchar(50) NOT NULL,
  `SupplierNumber` varchar(15) NOT NULL,
  `SupplierAddress` varchar(200) NOT NULL,
  PRIMARY KEY (`SupplierID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `account`
--
ALTER TABLE `account`
  ADD CONSTRAINT `account_ibfk_1` FOREIGN KEY (`RoleID`) REFERENCES `role` (`RoleID`),
  ADD CONSTRAINT `account_ibfk_2` FOREIGN KEY (`CustomerID`) REFERENCES `customer` (`CustomerID`),
  ADD CONSTRAINT `account_ibfk_3` FOREIGN KEY (`EmployeeID`) REFERENCES `employee` (`EmployeeID`);

--
-- Các ràng buộc cho bảng `book`
--
ALTER TABLE `book`
  ADD CONSTRAINT `book_ibfk_1` FOREIGN KEY (`CategoryID`) REFERENCES `category` (`CategoryID`);

--
-- Các ràng buộc cho bảng `importslip`
--
ALTER TABLE `importslip`
  ADD CONSTRAINT `importslip_ibfk_1` FOREIGN KEY (`EmployeeID`) REFERENCES `employee` (`EmployeeID`),
  ADD CONSTRAINT `importslip_ibfk_2` FOREIGN KEY (`SupplierID`) REFERENCES `supplier` (`SupplierID`);

--
-- Các ràng buộc cho bảng `importslipdetail`
--
ALTER TABLE `importslipdetail`
  ADD CONSTRAINT `importslipdetail_ibfk_1` FOREIGN KEY (`SlipID`) REFERENCES `importslip` (`SlipID`),
  ADD CONSTRAINT `importslipdetail_ibfk_2` FOREIGN KEY (`BookID`) REFERENCES `book` (`BookID`);

--
-- Các ràng buộc cho bảng `order`
--
ALTER TABLE `order`
  ADD CONSTRAINT `order_ibfk_1` FOREIGN KEY (`CustomerID`) REFERENCES `customer` (`CustomerID`);

--
-- Các ràng buộc cho bảng `orderdetail`
--
ALTER TABLE `orderdetail`
  ADD CONSTRAINT `orderdetail_ibfk_1` FOREIGN KEY (`OrderID`) REFERENCES `order` (`OrderID`),
  ADD CONSTRAINT `orderdetail_ibfk_2` FOREIGN KEY (`BookID`) REFERENCES `book` (`BookID`);

--
-- Các ràng buộc cho bảng `promotion`
--
ALTER TABLE `promotion`
  ADD CONSTRAINT `promotion_ibfk_1` FOREIGN KEY (`CategoryID`) REFERENCES `category` (`CategoryID`);

--
-- Các ràng buộc cho bảng `promotionbook`
--
ALTER TABLE `promotionbook`
  ADD CONSTRAINT `promotionbook_ibfk_1` FOREIGN KEY (`PromotionID`) REFERENCES `promotion` (`PromotionID`),
  ADD CONSTRAINT `promotionbook_ibfk_2` FOREIGN KEY (`BookID`) REFERENCES `book` (`BookID`);

--
-- Các ràng buộc cho bảng `rolepermission`
--
ALTER TABLE `rolepermission`
  ADD CONSTRAINT `rolepermission_ibfk_1` FOREIGN KEY (`RoleID`) REFERENCES `role` (`RoleID`),
  ADD CONSTRAINT `rolepermission_ibfk_2` FOREIGN KEY (`PermissionID`) REFERENCES `permission` (`PermissionID`);
SET FOREIGN_KEY_CHECKS=1;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
