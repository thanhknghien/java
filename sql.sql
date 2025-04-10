-- Tạo database
CREATE DATABASE IF NOT EXISTS pos_system;
USE pos_system;

-- Bảng vai trò (Roles)
CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL -- (Nhân viên, Quản lý, Admin)
);

-- Bảng nhóm quyền (Permission Groups)
CREATE TABLE permission_groups (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL -- (Quản lý Sản phẩm, Quản lý Đơn hàng, Quản lý Hóa đơn, ...)
);

-- Bảng quyền chi tiết (Permissions)
CREATE TABLE permissions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    group_id INT,
    name ENUM('Chỉnh sửa', 'Chỉ xem') NOT NULL,
    FOREIGN KEY (group_id) REFERENCES permission_groups(id) ON DELETE CASCADE
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

-- Bảng ánh xạ quyền cho từng user (User-Permissions)
CREATE TABLE user_permissions (
    user_id INT,
    permission_id INT,
    PRIMARY KEY (user_id, permission_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE
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
    total DECIMAL(10,2) NOT NULL,
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
TRUNCATE TABLE role_permissions;
TRUNCATE TABLE permissions;
TRUNCATE TABLE permission_groups;
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

-- Thêm nhóm quyền
INSERT INTO permission_groups (name) VALUES 
('Quản lý Sản phẩm'),
('Quản lý Đơn hàng'),
('Quản lý Hóa đơn');

-- Thêm quyền chi tiết
INSERT INTO permissions (group_id, name) VALUES 
(1, 'Chỉnh sửa'), -- Quản lý Sản phẩm - Chỉnh sửa
(1, 'Chỉ xem'),   -- Quản lý Sản phẩm - Chỉ xem
(2, 'Chỉnh sửa'), -- Quản lý Đơn hàng - Chỉnh sửa
(2, 'Chỉ xem'),   -- Quản lý Đơn hàng - Chỉ xem
(3, 'Chỉnh sửa'), -- Quản lý Hóa đơn - Chỉnh sửa
(3, 'Chỉ xem');   -- Quản lý Hóa đơn - Chỉ xem

-- Thêm user hiện có: nhanvien1 (Nhân viên, quyền Chỉ xem Quản lý Sản phẩm)
INSERT INTO users (username, password, role_id, status) 
VALUES ('nhanvien1', 'hashed_password1', 1, true); -- role_id 1 là "Nhân viên"

INSERT INTO user_permissions (user_id, permission_id) 
VALUES (1, 2); -- user_id 1 là nhanvien1, permission_id 2 là "Chỉ xem" Quản lý Sản phẩm

INSERT INTO category (name) VALUES
('Sách Văn học'),
('Sách Khoa học'),
('Sách Thiếu nhi');

INSERT INTO products (name, author, price, categoryid) VALUES
('Nhà giả kim', 'Paulo Coelho', 120000, 1),
('Đắc nhân tâm', 'Dale Carnegie', 150000, 1),
('Vũ trụ trong một hạt cát', 'Stephen Hawking', 200000, 2),
('Hành trình về phương Đông', 'Baird T. Spalding', 180000, 2),
('Dế mèn phiêu lưu ký', 'Tô Hoài', 80000, 3);

INSERT INTO customers (name, phone, points) VALUES
('Nguyễn Văn A', '0905123456', 50),
('Trần Thị B', '0915123456', 30),
('Lê Văn C', '0925123456', 20);

INSERT INTO orders (customer_id, employee_id, total) VALUES
(1, 1, 240000), -- Đơn hàng của Nguyễn Văn A, nhân viên employee1
(2, 2, 350000), -- Đơn hàng của Trần Thị B, quản lý manager1
(3, 3, 160000); -- Đơn hàng của Lê Văn C, admin admin1

INSERT INTO order_details (order_id, product_id, quantity, price) VALUES
-- Đơn hàng 1
(1, 1, 1, 120000), -- Nhà giả kim
(1, 2, 1, 150000), -- Đắc nhân tâm
-- Đơn hàng 2
(2, 3, 1, 200000), -- Vũ trụ trong một hạt cát
(2, 4, 1, 180000), -- Hành trình về phương Đông
-- Đơn hàng 3
(3, 5, 2, 80000);  -- Dế mèn phiêu lưu ký (2 cuốn)