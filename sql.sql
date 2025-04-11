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
    name VARCHAR(50) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng sản phẩm
CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    author VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    categoryid INT NOT NULL,
    imagePath VARCHAR(255),
    description TEXT,
    stock INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (categoryid) REFERENCES category(categoryid) ON DELETE CASCADE
);

-- Bảng khách hàng
CREATE TABLE customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) UNIQUE NOT NULL,
    email VARCHAR(100),
    address TEXT,
    points INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng đơn hàng
CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date DATETIME DEFAULT CURRENT_TIMESTAMP,
    customer_id INT,
    employee_id INT,
    total DECIMAL(10,2) NOT NULL,
    status ENUM('Chờ xác nhận', 'Đã xác nhận', 'Đã giao', 'Đã hủy') DEFAULT 'Chờ xác nhận',
    payment_method ENUM('Tiền mặt', 'Chuyển khoản', 'Thẻ tín dụng') DEFAULT 'Tiền mặt',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE SET NULL
);

-- Bảng chi tiết đơn hàng
CREATE TABLE order_details (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    product_id INT,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
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
('nhanvien1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EHsM8', 1, true),
('quanly1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EHsM8', 2, true),
('admin1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EHsM8', 3, true);

-- Thêm danh mục
INSERT INTO category (name, description) VALUES
('Sách Văn học', 'Các tác phẩm văn học trong và ngoài nước'),
('Sách Khoa học', 'Sách về các lĩnh vực khoa học'),
('Sách Thiếu nhi', 'Sách dành cho trẻ em');

-- Thêm sản phẩm
INSERT INTO products (name, author, price, categoryid, description, stock) VALUES
('Nhà giả kim', 'Paulo Coelho', 120000, 1, 'Tiểu thuyết nổi tiếng của Paulo Coelho', 50),
('Đắc nhân tâm', 'Dale Carnegie', 150000, 1, 'Sách về kỹ năng giao tiếp', 30),
('Vũ trụ trong một hạt cát', 'Stephen Hawking', 200000, 2, 'Sách về vật lý thiên văn', 20),
('Hành trình về phương Đông', 'Baird T. Spalding', 180000, 2, 'Sách về tâm linh và triết học', 25),
('Dế mèn phiêu lưu ký', 'Tô Hoài', 80000, 3, 'Truyện thiếu nhi nổi tiếng', 100);

-- Thêm khách hàng
INSERT INTO customers (name, phone, email, address, points) VALUES
('Nguyễn Văn A', '0905123456', 'nguyenvana@example.com', '123 Đường ABC, Quận 1, TP.HCM', 50),
('Trần Thị B', '0915123456', 'tranthib@example.com', '456 Đường XYZ, Quận 2, TP.HCM', 30),
('Lê Văn C', '0925123456', 'levanc@example.com', '789 Đường DEF, Quận 3, TP.HCM', 20);

-- Thêm đơn hàng
INSERT INTO orders (customer_id, employee_id, total, status, payment_method) VALUES
(1, 1, 270000, 'Đã giao', 'Tiền mặt'), -- Đơn hàng của Nguyễn Văn A, nhân viên nhanvien1
(2, 2, 380000, 'Đã giao', 'Chuyển khoản'), -- Đơn hàng của Trần Thị B, quản lý quanly1
(3, 3, 160000, 'Đã giao', 'Thẻ tín dụng'); -- Đơn hàng của Lê Văn C, admin admin1

-- Thêm chi tiết đơn hàng
INSERT INTO order_details (order_id, product_id, quantity, price) VALUES
-- Đơn hàng 1
(1, 1, 1, 120000), -- Nhà giả kim
(1, 2, 1, 150000), -- Đắc nhân tâm
-- Đơn hàng 2
(2, 3, 1, 200000), -- Vũ trụ trong một hạt cát
(2, 4, 1, 180000), -- Hành trình về phương Đông
-- Đơn hàng 3
(3, 5, 2, 80000);  -- Dế mèn phiêu lưu ký (2 cuốn)