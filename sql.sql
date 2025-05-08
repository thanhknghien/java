-- Xóa và tạo cơ sở dữ liệu
DROP DATABASE IF EXISTS pos_system;
CREATE DATABASE pos_system;
USE pos_system;

-- Bảng vai trò (Roles)
CREATE TABLE roles (
    id INT PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
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

-- Bảng quản lý phân quyền (Permission Management)
CREATE TABLE permission_management (
    user_id INT PRIMARY KEY,
    can_manage_permissions BOOLEAN DEFAULT false,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Bảng quản lý người dùng (User Management)
CREATE TABLE user_management (
    id INT PRIMARY KEY,
    can_add BOOLEAN DEFAULT false,
    can_edit BOOLEAN DEFAULT false,
    can_delete BOOLEAN DEFAULT false,
    can_view BOOLEAN DEFAULT false,
    FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

-- Bảng quản lý sản phẩm (Product Management)
CREATE TABLE product_management (
    id INT PRIMARY KEY,
    can_add BOOLEAN DEFAULT false,
    can_edit BOOLEAN DEFAULT false,
    can_delete BOOLEAN DEFAULT false,
    can_view BOOLEAN DEFAULT false,
    FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

-- Bảng quản lý đơn hàng (Order Management)
CREATE TABLE order_management (
    id INT PRIMARY KEY,
    can_view BOOLEAN DEFAULT false,
    FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

-- Bảng quản lý thống kê (Statistics Management)
CREATE TABLE statistics_management (
    id INT PRIMARY KEY,
    can_view BOOLEAN DEFAULT false,
    FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

-- Bảng cấu hình quyền theo vai trò (Role Permission Configs)
CREATE TABLE role_permission_configs (
    role_id INT,
    module VARCHAR(50),
    action VARCHAR(50),
    allowed BOOLEAN DEFAULT false,
    PRIMARY KEY (role_id, module, action),
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Thêm dữ liệu mẫu
-- Thêm vai trò
INSERT INTO roles (id, name) VALUES 
(1, 'Nhân viên'),
(2, 'Quản lý'),
(3, 'Admin');

-- Thêm người dùng
INSERT INTO users (id, username, password, role_id, status) VALUES 
(1, 'nhanvien1', '123', 1, true),
(2, 'quanly1', '123', 2, true),
(3, 'admin1', '123', 3, true);

-- Thêm quyền quản lý phân quyền
INSERT INTO permission_management (user_id, can_manage_permissions) VALUES
(1, false),  -- Nhân viên không có quyền
(2, false),  -- Quản lý không có quyền
(3, true);   -- Admin có quyền

-- Thêm quyền quản lý người dùng
INSERT INTO user_management (id, can_add, can_edit, can_delete, can_view) VALUES
(1, false, false, false, true),  -- Nhân viên chỉ xem
(2, true, true, false, true),    -- Quản lý thêm, sửa, xem
(3, true, true, true, true);     -- Admin đầy đủ quyền

-- Thêm quyền quản lý sản phẩm
INSERT INTO product_management (id, can_add, can_edit, can_delete, can_view) VALUES
(1, false, false, false, true),  -- Nhân viên chỉ xem
(2, true, true, false, true),    -- Quản lý thêm, sửa, xem
(3, true, true, true, true);     -- Admin đầy đủ quyền

-- Thêm quyền quản lý đơn hàng
INSERT INTO order_management (id, can_view) VALUES
(1, true),   -- Nhân viên xem được
(2, true),   -- Quản lý xem được
(3, true);   -- Admin xem được

-- Thêm quyền quản lý thống kê
INSERT INTO statistics_management (id, can_view) VALUES
(1, false),  -- Nhân viên không xem được
(2, true),   -- Quản lý xem được
(3, true);   -- Admin xem được

-- Thêm cấu hình quyền theo vai trò
INSERT INTO role_permission_configs (role_id, module, action, allowed) VALUES
-- Nhân viên (role_id = 1)
(1, 'user_management', 'view', true),
(1, 'product_management', 'view', true),
(1, 'order_management', 'view', true),
-- Quản lý (role_id = 2)
(2, 'user_management', 'add', true),
(2, 'user_management', 'edit', true),
(2, 'user_management', 'view', true),
(2, 'product_management', 'add', true),
(2, 'product_management', 'edit', true),
(2, 'product_management', 'view', true),
(2, 'order_management', 'view', true),
(2, 'statistics_management', 'view', true),
-- Admin (role_id = 3)
(3, 'user_management', 'add', true),
(3, 'user_management', 'edit', true),
(3, 'user_management', 'delete', true),
(3, 'user_management', 'view', true),
(3, 'product_management', 'add', true),
(3, 'product_management', 'edit', true),
(3, 'product_management', 'delete', true),
(3, 'product_management', 'view', true),
(3, 'order_management', 'view', true),
(3, 'statistics_management', 'view', true),
(3, 'permission_management', 'manage_permissions', true);

-- Thêm danh mục
INSERT INTO category (name) VALUES
('Sách Văn học'),
('Sách Khoa học'),
('Sách Thiếu nhi');

-- Thêm sản phẩm
INSERT INTO products (name, author, price, categoryid) VALUES
('Truyện Kiều', 'Nguyễn Du', 120000, 1),
('Nhà Giả Kim', 'Paulo Coelho', 150000, 1),
('Vũ Trụ Trong Vỏ Hạt Dẻ', 'Stephen Hawking', 200000, 2),
('Đắc Nhân Tâm', 'Dale Carnegie', 180000, 2),
('Dế Mèn Phiêu Lưu Ký', 'Tô Hoài', 80000, 3);

-- Thêm khách hàng
INSERT INTO customers (name, phone, points) VALUES
('Nguyễn Văn A', '0905123456', 50),
('Trần Thị B', '0915123456', 30),
('Lê Văn C', '0925123456', 20);

-- Thêm đơn hàng
INSERT INTO orders (customer_id, employee_id, total) VALUES
(1, 1, 270000), -- Đơn hàng của Nguyễn Văn A, nhân viên nhanvien1
(2, 2, 380000), -- Đơn hàng của Trần Thị B, quản lý quanly1
(3, 3, 160000); -- Đơn hàng của Lê Văn C, admin admin1

-- Thêm chi tiết đơn hàng
INSERT INTO order_details (order_id, product_id, quantity, price) VALUES
-- Đơn hàng 1
(1, 1, 1, 120000), 
(1, 2, 1, 150000),
-- Đơn hàng 2
(2, 3, 1, 200000), 
(2, 4, 1, 180000), 
-- Đơn hàng 3
(3, 5, 2, 80000);