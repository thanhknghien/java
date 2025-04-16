-- Tạo database
CREATE DATABASE IF NOT EXISTS pos_system;
USE pos_system;

-- Bảng vai trò (Roles)
CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL -- (Nhân viên, Quản lý, Admin)
);

-- Bảng quản lý người dùng (User Management)
CREATE TABLE user_management (
    id INT AUTO_INCREMENT PRIMARY KEY,
    role_id INT,
    can_add BOOLEAN DEFAULT false,
    can_edit BOOLEAN DEFAULT false,
    can_delete BOOLEAN DEFAULT false,
    can_view BOOLEAN DEFAULT false,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Bảng quản lý hóa đơn (Invoice Management)
CREATE TABLE invoice_management (
    id INT AUTO_INCREMENT PRIMARY KEY,
    role_id INT,
    can_add BOOLEAN DEFAULT false,
    can_edit BOOLEAN DEFAULT false,
    can_delete BOOLEAN DEFAULT false,
    can_view BOOLEAN DEFAULT false,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Bảng quản lý sản phẩm (Product Management)
CREATE TABLE product_management (
    id INT AUTO_INCREMENT PRIMARY KEY,
    role_id INT,
    can_add BOOLEAN DEFAULT false,
    can_edit BOOLEAN DEFAULT false,
    can_delete BOOLEAN DEFAULT false,
    can_view BOOLEAN DEFAULT false,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Bảng quản lý đơn hàng (Order Management)
CREATE TABLE order_management (
    id INT AUTO_INCREMENT PRIMARY KEY,
    role_id INT,
    can_add BOOLEAN DEFAULT false,
    can_edit BOOLEAN DEFAULT false,
    can_delete BOOLEAN DEFAULT false,
    can_view BOOLEAN DEFAULT false,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Bảng người dùng (Users)
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role_id INT,
    status BOOLEAN DEFAULT true,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE SET NULL
);

-- Bảng danh mục sản phẩm
CREATE TABLE category (
    categoryid INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

-- Bảng sản phẩm
CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    author VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    categoryid INT NOT NULL,
    imagePath VARCHAR(255),
    FOREIGN KEY (categoryid) REFERENCES category(categoryid) ON DELETE CASCADE
);

-- Bảng khách hàng
CREATE TABLE customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) UNIQUE NOT NULL,
    points INT DEFAULT 0
);

-- Bảng đơn hàng
CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date DATETIME DEFAULT CURRENT_TIMESTAMP,
    customer_id INT,
    employee_id INT,
    total DECIMAL(10,1) NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES users(id),
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

-- Bảng chi tiết đơn hàng
CREATE TABLE order_details (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    product_id INT,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Xóa dữ liệu cũ nếu có
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE order_management;
TRUNCATE TABLE product_management;
TRUNCATE TABLE invoice_management;
TRUNCATE TABLE user_management;
TRUNCATE TABLE order_details;
TRUNCATE TABLE orders;
TRUNCATE TABLE products;
TRUNCATE TABLE category;
TRUNCATE TABLE users;
TRUNCATE TABLE roles;
SET FOREIGN_KEY_CHECKS = 1;

-- Thêm dữ liệu mẫu
-- Thêm vai trò
INSERT INTO roles (name) VALUES 
('Nhân viên'),
('Quản lý'),
('Admin');

-- Thêm quyền quản lý người dùng
INSERT INTO user_management (role_id, can_add, can_edit, can_delete, can_view) VALUES
(1, false, false, false, true),  -- Nhân viên chỉ xem
(2, true, true, false, true),   -- Quản lý thêm, sửa, xem
(3, true, true, true, true);    -- Admin đầy đủ quyền

-- Thêm quyền quản lý hóa đơn
INSERT INTO invoice_management (role_id, can_add, can_edit, can_delete, can_view) VALUES
(1, true, false, false, true),  -- Nhân viên thêm và xem
(2, true, true, false, true),   -- Quản lý thêm, sửa, xem
(3, true, true, true, true);    -- Admin đầy đủ quyền

-- Thêm quyền quản lý sản phẩm
INSERT INTO product_management (role_id, can_add, can_edit, can_delete, can_view) VALUES
(1, false, false, false, true), -- Nhân viên chỉ xem
(2, true, true, false, true),   -- Quản lý thêm, sửa, xem
(3, true, true, true, true);    -- Admin đầy đủ quyền

-- Thêm quyền quản lý đơn hàng
INSERT INTO order_management (role_id, can_add, can_edit, can_delete, can_view) VALUES
(1, true, true, false, true),   -- Nhân viên thêm, sửa, xem
(2, true, true, true, true),    -- Quản lý đầy đủ quyền
(3, true, true, true, true);    -- Admin đầy đủ quyền

-- Thêm users
INSERT INTO users (username, password, role_id, status) VALUES 
('nhanvien1', '123', 1, true),
('quanly1', '123', 2, true),
('admin1', '123', 3, true);

INSERT INTO customers (name, phone, points) VALUES
('Nguyễn Văn A', '0905123456', 50),
('Trần Thị B', '0915123456', 30),
('Lê Văn C', '0925123456', 20);

INSERT INTO orders (customer_id, employee_id, total) VALUES
(1, 1, 240000), -- Đơn hàng của Nguyễn Văn A, nhân viên employee1
(2, 1, 350000), -- Đơn hàng của Trần Thị B, quản lý manager1
(3, 1, 160000); -- Đơn hàng của Lê Văn C, admin admin1

INSERT INTO order_details (order_id, product_id, quantity, price) VALUES
-- Đơn hàng 1
(1, 1, 1, 120000), 
(1, 2, 1, 150000),
-- Đơn hàng 2
(2, 3, 1, 200000), 
(2, 4, 1, 180000), 
-- Đơn hàng 3
(3, 5, 2, 80000); 