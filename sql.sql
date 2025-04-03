-- Tạo database
CREATE DATABASE pos_system;
USE pos_system;

-- Bảng vai trò (Roles)
CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL -- (Nhân viên, Quản lý, Admin)
);

-- Bảng nhóm quyền (Permission Groups)
CREATE TABLE permission_groups (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL -- (Quản lý Sản phẩm, Quản lý Đơn hàng, ...)
);

-- Bảng quyền chi tiết (Permissions)
CREATE TABLE permissions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    group_id INT,
    name ENUM('Thêm', 'Sửa', 'Xóa', 'Tìm kiếm') NOT NULL,
    FOREIGN KEY (group_id) REFERENCES permission_groups(id) ON DELETE CASCADE
);

-- Bảng ánh xạ quyền cho từng vai trò (Role-Permissions)
CREATE TABLE role_permissions (
    role_id INT,
    permission_id INT,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE
);

-- Bảng người dùng (Users)
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role_id INT,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE SET NULL
);
CREATE TABLE category(
    categoryid INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);
-- Bảng sản phẩm
CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    author VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    categoryid INT NOT NULL,
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
    FOREIGN KEY (employee_id) REFERENCES users(id)
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

-- Bảng báo cáo doanh thu (tùy chọn)
CREATE VIEW sales_report AS
SELECT o.id AS order_id, o.date, u.username AS employee, o.total 
FROM orders o 
JOIN users u ON o.employee_id = u.id;

-- Dữ liệu mẫu
-- them danh muc
INSERT INTO category(categoryid, name) VALUES
(1, "Thực pphẩm"),
(2, "Đồ uống");
-- Thêm vai trò
INSERT INTO roles (name) VALUES ('Nhân viên'), ('Quản lý'), ('Admin');

-- Thêm nhóm quyền
INSERT INTO permission_groups (name) VALUES ('Quản lý Sản phẩm'), ('Quản lý Đơn hàng'), ('Quản lý Khách hàng');

-- Thêm quyền chi tiết
INSERT INTO permissions (group_id, name) VALUES 
(1, 'Thêm'), (1, 'Sửa'), (1, 'Xóa'), (1, 'Tìm kiếm'),
(2, 'Thêm'), (2, 'Sửa'), (2, 'Xóa'), (2, 'Tìm kiếm'),
(3, 'Thêm'), (3, 'Sửa'), (3, 'Xóa'), (3, 'Tìm kiếm');

-- Gán quyền cho vai trò
INSERT INTO role_permissions (role_id, permission_id) VALUES 
(1, 1), (1, 2), (1, 4),
(2, 1), (2, 2), (2, 3), (2, 4),
(3, 1), (3, 2), (3, 3), (3, 4);

-- Thêm người dùng
INSERT INTO users (username, password, role_id) VALUES 
('nv001', 'password1', 1), 
('ql001', 'password2', 2), 
('admin', 'password3', 3);

-- Thêm sản phẩm
INSERT INTO products (name, price, categoryid) VALUES 
('Sản phẩm A', 10000, 2),
('Sản phẩm B', 20000, 1);

-- Thêm khách hàng
INSERT INTO customers (name, phone, points) VALUES 
('Nguyễn Văn A', '0909123456', 10),
('Trần Thị B', '0988765432', 20);

-- Thêm đơn hàng
INSERT INTO orders (employee_id, total) VALUES 
(1, 30000);

-- Thêm chi tiết đơn hàng
INSERT INTO order_details (order_id, product_id, quantity, price) VALUES 
(1, 1, 2, 20000),
(1, 2, 1, 10000);
