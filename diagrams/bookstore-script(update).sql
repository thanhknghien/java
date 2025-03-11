-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th3 11, 2025 lúc 02:08 PM
-- Phiên bản máy phục vụ: 10.5.28-MariaDB
-- Phiên bản PHP: 8.2.12

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
-- Cấu trúc bảng cho bảng `Account`
--

DROP TABLE IF EXISTS `Account`;
CREATE TABLE `Account` (
  `AccountID` int(11) NOT NULL,
  `Username` varchar(30) NOT NULL,
  `Password` varchar(50) NOT NULL,
  `RoleID` int(11) DEFAULT NULL,
  `CustomerID` int(11) DEFAULT NULL,
  `EmployeeID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Bẫy `Account`
--
-- Cơ sở dữ liệu của bạn có vẻ như có dùng các bẫy;
-- xuất bí danh có thể không làm việc đúng trong mọi trường hợp
--
DROP TRIGGER IF EXISTS `BeforeDeleteAccountUpdateRole`;
DELIMITER $$
CREATE TRIGGER `BeforeDeleteAccountUpdateRole` BEFORE DELETE ON `Account` FOR EACH ROW BEGIN
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
-- Cấu trúc bảng cho bảng `Book`
--

DROP TABLE IF EXISTS `Book`;
CREATE TABLE `Book` (
  `BookID` int(11) NOT NULL,
  `Title` varchar(100) NOT NULL,
  `Author` varchar(50) DEFAULT NULL,
  `Genre` varchar(30) DEFAULT NULL,
  `Price` decimal(10,2) NOT NULL,
  `StockQuantity` int(11) NOT NULL DEFAULT 0,
  `CategoryID` int(11) DEFAULT NULL,
  `ImagePath` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `Category`
--

DROP TABLE IF EXISTS `Category`;
CREATE TABLE `Category` (
  `CategoryID` int(11) NOT NULL,
  `Name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `Customer`
--

DROP TABLE IF EXISTS `Customer`;
CREATE TABLE `Customer` (
  `CustomerID` int(11) NOT NULL,
  `Name` varchar(50) NOT NULL,
  `Address` varchar(200) DEFAULT NULL,
  `Phone` varchar(15) DEFAULT NULL,
  `Email` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `Employee`
--

DROP TABLE IF EXISTS `Employee`;
CREATE TABLE `Employee` (
  `EmployeeID` int(11) NOT NULL,
  `Name` varchar(50) NOT NULL,
  `Status` varchar(20) NOT NULL DEFAULT 'Active',
  `Phone` varchar(15) DEFAULT NULL,
  `Email` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `ImportSlip`
--

DROP TABLE IF EXISTS `ImportSlip`;
CREATE TABLE `ImportSlip` (
  `SlipID` int(11) NOT NULL,
  `EmployeeID` int(11) DEFAULT NULL,
  `SupplierID` int(11) DEFAULT NULL,
  `ImportDate` datetime NOT NULL DEFAULT current_timestamp(),
  `TotalAmount` decimal(10,2) NOT NULL DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `ImportSlipDetail`
--

DROP TABLE IF EXISTS `ImportSlipDetail`;
CREATE TABLE `ImportSlipDetail` (
  `SlipID` int(11) NOT NULL,
  `BookID` int(11) NOT NULL,
  `Quantity` int(11) NOT NULL,
  `UnitPrice` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Bẫy `ImportSlipDetail`
--
-- Cơ sở dữ liệu của bạn có vẻ như có dùng các bẫy;
-- xuất bí danh có thể không làm việc đúng trong mọi trường hợp
--
DROP TRIGGER IF EXISTS `UpdateStockAfterImport`;
DELIMITER $$
CREATE TRIGGER `UpdateStockAfterImport` AFTER INSERT ON `ImportSlipDetail` FOR EACH ROW BEGIN
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
-- Cấu trúc bảng cho bảng `Order`
--

DROP TABLE IF EXISTS `Order`;
CREATE TABLE `Order` (
  `OrderID` int(11) NOT NULL,
  `CustomerID` int(11) DEFAULT NULL,
  `EmployeeID` int(11) DEFAULT NULL,
  `OrderDate` datetime NOT NULL DEFAULT current_timestamp(),
  `TotalAmount` decimal(10,2) NOT NULL DEFAULT 0.00,
  `Status` varchar(20) NOT NULL DEFAULT 'Pending'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Bẫy `Order`
--
-- Cơ sở dữ liệu của bạn có vẻ như có dùng các bẫy;
-- xuất bí danh có thể không làm việc đúng trong mọi trường hợp
--
DROP TRIGGER IF EXISTS `RestoreStockAfterCancelPending`;
DELIMITER $$
CREATE TRIGGER `RestoreStockAfterCancelPending` AFTER UPDATE ON `Order` FOR EACH ROW BEGIN
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
-- Cấu trúc bảng cho bảng `OrderDetail`
--

DROP TABLE IF EXISTS `OrderDetail`;
CREATE TABLE `OrderDetail` (
  `OrderID` int(11) NOT NULL,
  `BookID` int(11) NOT NULL,
  `Quantity` int(11) NOT NULL,
  `UnitPrice` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Bẫy `OrderDetail`
--
-- Cơ sở dữ liệu của bạn có vẻ như có dùng các bẫy;
-- xuất bí danh có thể không làm việc đúng trong mọi trường hợp
--
DROP TRIGGER IF EXISTS `CheckStockBeforeOrder`;
DELIMITER $$
CREATE TRIGGER `CheckStockBeforeOrder` BEFORE INSERT ON `OrderDetail` FOR EACH ROW BEGIN
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
CREATE TRIGGER `ReserveStockAfterOrder` AFTER INSERT ON `OrderDetail` FOR EACH ROW BEGIN
    UPDATE Book
    SET StockQuantity = StockQuantity - NEW.Quantity
    WHERE BookID = NEW.BookID;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `Permission`
--

DROP TABLE IF EXISTS `Permission`;
CREATE TABLE `Permission` (
  `PermissionID` int(11) NOT NULL,
  `PermissionName` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `Promotion`
--

DROP TABLE IF EXISTS `Promotion`;
CREATE TABLE `Promotion` (
  `PromotionID` int(11) NOT NULL,
  `Name` varchar(50) NOT NULL,
  `DiscountPercent` decimal(5,2) NOT NULL,
  `StartDate` date NOT NULL,
  `EndDate` date NOT NULL,
  `CategoryID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `PromotionBook`
--

DROP TABLE IF EXISTS `PromotionBook`;
CREATE TABLE `PromotionBook` (
  `PromotionID` int(11) NOT NULL,
  `BookID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `Role`
--

DROP TABLE IF EXISTS `Role`;
CREATE TABLE `Role` (
  `RoleID` int(11) NOT NULL,
  `RoleName` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `RolePermission`
--

DROP TABLE IF EXISTS `RolePermission`;
CREATE TABLE `RolePermission` (
  `RoleID` int(11) NOT NULL,
  `PermissionID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `Supplier`
--

DROP TABLE IF EXISTS `Supplier`;
CREATE TABLE `Supplier` (
  `SupplierID` int(11) NOT NULL,
  `SupplierName` varchar(50) NOT NULL,
  `SupplierNumber` varchar(15) NOT NULL,
  `SupplierAddress` varchar(200) NOT NULL,
  `Status` varchar(20) NOT NULL DEFAULT 'Active'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `Account`
--
ALTER TABLE `Account`
  ADD PRIMARY KEY (`AccountID`),
  ADD UNIQUE KEY `Username` (`Username`),
  ADD KEY `RoleID` (`RoleID`),
  ADD KEY `CustomerID` (`CustomerID`),
  ADD KEY `EmployeeID` (`EmployeeID`);

--
-- Chỉ mục cho bảng `Book`
--
ALTER TABLE `Book`
  ADD PRIMARY KEY (`BookID`),
  ADD KEY `CategoryID` (`CategoryID`);

--
-- Chỉ mục cho bảng `Category`
--
ALTER TABLE `Category`
  ADD PRIMARY KEY (`CategoryID`);

--
-- Chỉ mục cho bảng `Customer`
--
ALTER TABLE `Customer`
  ADD PRIMARY KEY (`CustomerID`);

--
-- Chỉ mục cho bảng `Employee`
--
ALTER TABLE `Employee`
  ADD PRIMARY KEY (`EmployeeID`);

--
-- Chỉ mục cho bảng `ImportSlip`
--
ALTER TABLE `ImportSlip`
  ADD PRIMARY KEY (`SlipID`),
  ADD KEY `EmployeeID` (`EmployeeID`),
  ADD KEY `importslip_ibfk_2` (`SupplierID`);

--
-- Chỉ mục cho bảng `ImportSlipDetail`
--
ALTER TABLE `ImportSlipDetail`
  ADD PRIMARY KEY (`SlipID`,`BookID`),
  ADD KEY `BookID` (`BookID`);

--
-- Chỉ mục cho bảng `Order`
--
ALTER TABLE `Order`
  ADD PRIMARY KEY (`OrderID`),
  ADD KEY `CustomerID` (`CustomerID`),
  ADD KEY `order_ibfk_2` (`EmployeeID`);

--
-- Chỉ mục cho bảng `OrderDetail`
--
ALTER TABLE `OrderDetail`
  ADD PRIMARY KEY (`OrderID`,`BookID`),
  ADD KEY `BookID` (`BookID`);

--
-- Chỉ mục cho bảng `Permission`
--
ALTER TABLE `Permission`
  ADD PRIMARY KEY (`PermissionID`),
  ADD UNIQUE KEY `PermissionName` (`PermissionName`);

--
-- Chỉ mục cho bảng `Promotion`
--
ALTER TABLE `Promotion`
  ADD PRIMARY KEY (`PromotionID`),
  ADD KEY `CategoryID` (`CategoryID`);

--
-- Chỉ mục cho bảng `PromotionBook`
--
ALTER TABLE `PromotionBook`
  ADD PRIMARY KEY (`PromotionID`,`BookID`),
  ADD KEY `BookID` (`BookID`);

--
-- Chỉ mục cho bảng `Role`
--
ALTER TABLE `Role`
  ADD PRIMARY KEY (`RoleID`),
  ADD UNIQUE KEY `RoleName` (`RoleName`);

--
-- Chỉ mục cho bảng `RolePermission`
--
ALTER TABLE `RolePermission`
  ADD PRIMARY KEY (`RoleID`,`PermissionID`),
  ADD KEY `PermissionID` (`PermissionID`);

--
-- Chỉ mục cho bảng `Supplier`
--
ALTER TABLE `Supplier`
  ADD PRIMARY KEY (`SupplierID`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `Account`
--
ALTER TABLE `Account`
  MODIFY `AccountID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `Book`
--
ALTER TABLE `Book`
  MODIFY `BookID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `Category`
--
ALTER TABLE `Category`
  MODIFY `CategoryID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `Customer`
--
ALTER TABLE `Customer`
  MODIFY `CustomerID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `Employee`
--
ALTER TABLE `Employee`
  MODIFY `EmployeeID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `ImportSlip`
--
ALTER TABLE `ImportSlip`
  MODIFY `SlipID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `Order`
--
ALTER TABLE `Order`
  MODIFY `OrderID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `Permission`
--
ALTER TABLE `Permission`
  MODIFY `PermissionID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `Promotion`
--
ALTER TABLE `Promotion`
  MODIFY `PromotionID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `Role`
--
ALTER TABLE `Role`
  MODIFY `RoleID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `Supplier`
--
ALTER TABLE `Supplier`
  MODIFY `SupplierID` int(11) NOT NULL AUTO_INCREMENT;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `Account`
--
ALTER TABLE `Account`
  ADD CONSTRAINT `account_ibfk_1` FOREIGN KEY (`RoleID`) REFERENCES `Role` (`RoleID`),
  ADD CONSTRAINT `account_ibfk_2` FOREIGN KEY (`CustomerID`) REFERENCES `Customer` (`CustomerID`),
  ADD CONSTRAINT `account_ibfk_3` FOREIGN KEY (`EmployeeID`) REFERENCES `Employee` (`EmployeeID`);

--
-- Các ràng buộc cho bảng `Book`
--
ALTER TABLE `Book`
  ADD CONSTRAINT `book_ibfk_1` FOREIGN KEY (`CategoryID`) REFERENCES `Category` (`CategoryID`);

--
-- Các ràng buộc cho bảng `ImportSlip`
--
ALTER TABLE `ImportSlip`
  ADD CONSTRAINT `importslip_ibfk_1` FOREIGN KEY (`EmployeeID`) REFERENCES `Employee` (`EmployeeID`),
  ADD CONSTRAINT `importslip_ibfk_2` FOREIGN KEY (`SupplierID`) REFERENCES `Supplier` (`SupplierID`);

--
-- Các ràng buộc cho bảng `ImportSlipDetail`
--
ALTER TABLE `ImportSlipDetail`
  ADD CONSTRAINT `importslipdetail_ibfk_1` FOREIGN KEY (`SlipID`) REFERENCES `ImportSlip` (`SlipID`),
  ADD CONSTRAINT `importslipdetail_ibfk_2` FOREIGN KEY (`BookID`) REFERENCES `Book` (`BookID`);

--
-- Các ràng buộc cho bảng `Order`
--
ALTER TABLE `Order`
  ADD CONSTRAINT `order_ibfk_1` FOREIGN KEY (`CustomerID`) REFERENCES `Customer` (`CustomerID`),
  ADD CONSTRAINT `order_ibfk_2` FOREIGN KEY (`EmployeeID`) REFERENCES `Employee` (`EmployeeID`);

--
-- Các ràng buộc cho bảng `OrderDetail`
--
ALTER TABLE `OrderDetail`
  ADD CONSTRAINT `orderdetail_ibfk_1` FOREIGN KEY (`OrderID`) REFERENCES `Order` (`OrderID`),
  ADD CONSTRAINT `orderdetail_ibfk_2` FOREIGN KEY (`BookID`) REFERENCES `Book` (`BookID`);

--
-- Các ràng buộc cho bảng `Promotion`
--
ALTER TABLE `Promotion`
  ADD CONSTRAINT `promotion_ibfk_1` FOREIGN KEY (`CategoryID`) REFERENCES `Category` (`CategoryID`);

--
-- Các ràng buộc cho bảng `PromotionBook`
--
ALTER TABLE `PromotionBook`
  ADD CONSTRAINT `promotionbook_ibfk_1` FOREIGN KEY (`PromotionID`) REFERENCES `Promotion` (`PromotionID`),
  ADD CONSTRAINT `promotionbook_ibfk_2` FOREIGN KEY (`BookID`) REFERENCES `Book` (`BookID`);

--
-- Các ràng buộc cho bảng `RolePermission`
--
ALTER TABLE `RolePermission`
  ADD CONSTRAINT `rolepermission_ibfk_1` FOREIGN KEY (`RoleID`) REFERENCES `Role` (`RoleID`),
  ADD CONSTRAINT `rolepermission_ibfk_2` FOREIGN KEY (`PermissionID`) REFERENCES `Permission` (`PermissionID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
