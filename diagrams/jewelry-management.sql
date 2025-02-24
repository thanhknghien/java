SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- Tạo cơ sở dữ liệu
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8;
USE `mydb`;

-- Tạo bảng Category
CREATE TABLE IF NOT EXISTS `Category` (
  `categoryID` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(500) NOT NULL,
  PRIMARY KEY (`categoryID`))
ENGINE = InnoDB;

-- Tạo bảng Product
CREATE TABLE IF NOT EXISTS `Product` (
  `productID` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `price` DOUBLE DEFAULT NULL,
  `quantityInStock` INT DEFAULT NULL,
  `minStockLevel` INT NOT NULL,
  `categoryID` INT NOT NULL,
  PRIMARY KEY (`productID`),
  FOREIGN KEY (`categoryID`) REFERENCES `Category` (`categoryID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- Tạo bảng Supplier
CREATE TABLE IF NOT EXISTS `Supplier` (
  `supplierID` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `address` VARCHAR(45) NOT NULL,
  `phone` VARCHAR(45) DEFAULT NULL,
  PRIMARY KEY (`supplierID`))
ENGINE = InnoDB;

-- Tạo bảng StockImport
CREATE TABLE IF NOT EXISTS `StockImport` (
  `importID` INT NOT NULL AUTO_INCREMENT,
  `date` DATETIME NOT NULL,
  `supplierID` INT NOT NULL,
  PRIMARY KEY (`importID`),
  FOREIGN KEY (`supplierID`) REFERENCES `Supplier` (`supplierID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- Tạo bảng StockImportDetail
CREATE TABLE IF NOT EXISTS `StockImportDetail` (
  `importID` INT NOT NULL,
  `productID` INT NOT NULL,
  `quantityImported` INT DEFAULT NULL,
  `importPrice` DOUBLE DEFAULT NULL,
  PRIMARY KEY (`importID`, `productID`),
  FOREIGN KEY (`importID`) REFERENCES `StockImport` (`importID`),
  FOREIGN KEY (`productID`) REFERENCES `Product` (`productID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- Tạo bảng Employee
CREATE TABLE IF NOT EXISTS `Employee` (
  `employeeID` INT NOT NULL AUTO_INCREMENT,
  `fullName` VARCHAR(45) NOT NULL,
  `status` VARCHAR(45) NOT NULL,
  `address` VARCHAR(45) NOT NULL,
  `phoneNumber` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`employeeID`))
ENGINE = InnoDB;

-- Tạo bảng Invoice
CREATE TABLE IF NOT EXISTS `Invoice` (
  `invoiceID` INT NOT NULL AUTO_INCREMENT,
  `date` DATETIME NOT NULL,
  `totalAmount` DOUBLE NOT NULL,
  `employeeID` INT NOT NULL,
  PRIMARY KEY (`invoiceID`),
  FOREIGN KEY (`employeeID`) REFERENCES `Employee` (`employeeID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- Tạo bảng InvoiceDetail
CREATE TABLE IF NOT EXISTS `InvoiceDetail` (
  `invoiceID` INT NOT NULL,
  `productID` INT NOT NULL,
  `quantity` INT NOT NULL,
  `unitPrice` DOUBLE NOT NULL,
  PRIMARY KEY (`invoiceID`, `productID`),
  FOREIGN KEY (`invoiceID`) REFERENCES `Invoice` (`invoiceID`),
  FOREIGN KEY (`productID`) REFERENCES `Product` (`productID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- Tạo bảng Role
CREATE TABLE IF NOT EXISTS `Role` (
  `roleID` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(500) NOT NULL,
  PRIMARY KEY (`roleID`))
ENGINE = InnoDB;

-- Tạo bảng Permission
CREATE TABLE IF NOT EXISTS `Permission` (
  `permissionID` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(500) DEFAULT NULL,
  PRIMARY KEY (`permissionID`))
ENGINE = InnoDB;

-- Tạo bảng Account
CREATE TABLE IF NOT EXISTS `Account` (
  `accountID` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(20) NOT NULL,
  `password` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`accountID`))
ENGINE = InnoDB;

-- Tạo bảng RolePermission
CREATE TABLE IF NOT EXISTS `RolePermission` (
  `rolePermissionID` INT NOT NULL AUTO_INCREMENT,
  `roleID` INT NOT NULL,
  `permissionID` INT NOT NULL,
  PRIMARY KEY (`rolePermissionID`),
  FOREIGN KEY (`roleID`) REFERENCES `Role` (`roleID`),
  FOREIGN KEY (`permissionID`) REFERENCES `Permission` (`permissionID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- Tạo bảng AccountPermission
CREATE TABLE IF NOT EXISTS `AccountPermission` (
  `accountPermissionID` INT NOT NULL AUTO_INCREMENT,
  `accountID` INT NOT NULL,
  `permissionID` INT NOT NULL,
  PRIMARY KEY (`accountPermissionID`),
  FOREIGN KEY (`accountID`) REFERENCES `Account` (`accountID`),
  FOREIGN KEY (`permissionID`) REFERENCES `Permission` (`permissionID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- Tạo bảng Payroll
CREATE TABLE IF NOT EXISTS `Payroll` (
  `payrollID` INT NOT NULL AUTO_INCREMENT,
  `employeeID` INT NOT NULL,
  `month` DATE NOT NULL,
  `salary` DOUBLE NOT NULL,stockimportdetail
  PRIMARY KEY (`payrollID`),
  FOREIGN KEY (`employeeID`) REFERENCES `Employee` (`employeeID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;