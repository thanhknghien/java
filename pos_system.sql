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

-- Bảng quản lý danh mục (Category Management)
CREATE TABLE category_management (
    id INT PRIMARY KEY,
    can_add BOOLEAN DEFAULT false,
    can_edit BOOLEAN DEFAULT false,
    can_delete BOOLEAN DEFAULT false,
    can_view BOOLEAN DEFAULT false,
    FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

-- Bảng quản lý khách hàng (Customer Management)
CREATE TABLE customer_management (
    id INT PRIMARY KEY,
    can_add BOOLEAN DEFAULT false,
    can_edit BOOLEAN DEFAULT false,
    can_delete BOOLEAN DEFAULT false,
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

-- Thêm quyền quản lý danh mục
INSERT INTO category_management (id, can_add, can_edit, can_delete, can_view) VALUES
(1, false, false, false, true),  -- Nhân viên chỉ xem
(2, true, true, false, true),    -- Quản lý thêm, sửa, xem
(3, true, true, true, true);     -- Admin đầy đủ quyền

-- Thêm quyền quản lý khách hàng
INSERT INTO customer_management (id, can_add, can_edit, can_delete, can_view) VALUES
(1, false, false, false, true),  -- Nhân viên chỉ xem
(2, true, true, false, true),    -- Quản lý thêm, sửa, xem
(3, true, true, true, true);     -- Admin đầy đủ quyền

-- Thêm cấu hình quyền theo vai trò
INSERT INTO role_permission_configs (role_id, module, action, allowed) VALUES
-- Nhân viên (role_id = 1)
(1, 'user_management', 'view', true),
(1, 'product_management', 'view', true),
(1, 'order_management', 'view', true),
(1, 'category_management', 'view', true),
(1, 'customer_management', 'view', true),
-- Quản lý (role_id = 2)
(2, 'user_management', 'add', true),
(2, 'user_management', 'edit', true),
(2, 'user_management', 'view', true),
(2, 'product_management', 'add', true),
(2, 'product_management', 'edit', true),
(2, 'product_management', 'view', true),
(2, 'order_management', 'view', true),
(2, 'statistics_management', 'view', true),
(2, 'category_management', 'add', true),
(2, 'category_management', 'edit', true),
(2, 'category_management', 'view', true),
(2, 'customer_management', 'add', true),
(2, 'customer_management', 'edit', true),
(2, 'customer_management', 'view', true),
-- Admin (role_id = 3)pos_system
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
(3, 'category_management', 'add', true),
(3, 'category_management', 'edit', true),
(3, 'category_management', 'delete', true),
(3, 'category_management', 'view', true),
(3, 'customer_management', 'add', true),
(3, 'customer_management', 'edit', true),
(3, 'customer_management', 'delete', true),
(3, 'customer_management', 'view', true),
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

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE category;
TRUNCATE TABLE products;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO category (name) VALUES
('Sách Tiếng anh'),
('Sách Văn hóa'),
('Tản văn'),
('Tiểu thuyết'),
('Văn học'),
('Sách Kỹ năng'),
('Sách Lịch sử'),
('Sách Tôn giáo'),
('Truyện dài'),
('Truyện ngắn'),
('Sách Song ngữ');

INSERT INTO products (name, author, price, categoryid, imagePath) VALUES
('Wuthering Heights', 'Emily Bronte', 100000, 1, 'product/1.jpg'), 
('Malicious', 'James Patterson', 80000, 1, 'product/2.jpg'),
('The Negotiator', 'Dee Henderson', 60000, 1, 'product/3.jpg'),
('The Crucible', 'Arthur Miller', 80000, 1, 'product/4.jpg'),
('Kiss Like You Mean It', 'Louise Harwood', 90000, 1, 'product/5.jpg'),
('Sài Gòn vang bóng', 'Lý Nhân Phan Thứ Lang', 70000, 2, 'product/6.jpg'),
('Con giai phố cổ', 'Nguyễn Việt Hà', 80000, 3, 'product/7.jpg'),
('Phố phường Hà-Nội-xưa', 'Hoàng Đạo Thúy', 100000, 4, ''),
('Hà Nội thành phố nghìn năm', 'Nguyễn Vinh Phúc', 60000, 2, 'product/8.jpg'),
('Nhớ Hà Nội, thương Sài Gòn', 'Nhiều tác giả', 70000, 4, 'product/9.jpg'),
('Sài Gòn ẩm thực sắc sắc không không', 'Ngữ Yên', 70000, 2, 'product/10.jpg'),
('Sài Gòn quán xá thương yêu', 'Lưu Quang Minh, Trần Khánh Ngân', 80000, 4, 'product/11.jpg'),
('20 truyện ngắn đặc sắc Sài Gòn', 'Nhiều tác giả', 80000, 5, 'product/12.jpg'),
('Đi học Hà Nội', 'Nguyễn Ngọc Tiến', 70000, 4, 'product/13.jpg'),
('Hà Nội là Hà Nội', 'Nguyễn Trương Quý', 70000, 3, 'product/14.jpg'),
('Hà Nội mùa đông ấy', 'Huỳnh Việt Hải', 60000, 4, 'product/15.jpg'),
('Hà Nội lầm than', 'Trọng Lang', 70000, 4, 'product/16.jpg'),
('Sài Gòn chữ VỘI trên vai', 'Vũ Minh Đức', 70000, 3, 'product/17.jpg'),
('Mình là nắng, việc của mình chói chang', 'Kazuko Watanabe', 70000, 6, 'product/18.jpg'),
('Dám tha thứ', 'Edward M. Hallowell, M.D.', 60000, 6, 'product/19.jpg'),
('Đi tìm lẽ sống', 'Viktor E. Frankl', 70000, 4, 'product/20.jpg'),
('Nói ít hiểu nhiều', 'Connie Dieken', 60000, 6, 'product/21.jpg'),
('Làm như chơi', 'Minh Niệm', 70000, 6, 'product/22.jpg'),
('Đắc Nhân Tâm', 'Dale Carnegie', 80000, 6, 'product/23.jpg'),
('Quẳng gánh lo đi & vui sống', 'Dale Carnegie', 80000, 6, 'product/24.jpg'),
('Cà phê cung Tony', 'Tony buổi sáng', 76000, 3, 'product/25.jpg'),
('trên dường băng', 'Tony buổi sáng', 76000, 3, ''),
('Cảm xúc là kẻ thù số một của thành công', 'TS. Lê Thẩm Dương', 60000, 6, 'product/26.jpg'),
('Triết lý làm giàu của người Do Thái', 'Phạm Hồng', 60000, 6, 'product/27.jpg'),
('Nguyên tắc 50 không sợ hãi', '50 Cent, Robert Greene', 70000, 6, 'product/28.jpg'),
('48 nguyên tắc chủ chốt của quyền lực', 'Robert Greene', 120000, 6, 'product/29.jpg'),
('Lời nguyện cầu chín năm trước', 'Ono Masatsugu', 70000, 4, 'product/30.jpg'),
('Con mắt thần Osiris', 'R. Austin Freeman', 80000, 4, 'product/31.jpg'),
('Chào mừng đến với N.H.K', 'Tatsuhiko Takimoto', 60000, 4, 'product/32.jpg'),
('Rìu đàn cúc', 'Yokomizo Seishi', 80000, 4, 'product/33.jpg'),
('Kasha', 'Miyuki Miyabe', 90000, 4, 'product/34.jpg'),
('Cô dâu đen', 'Cornell Woolrich', 80000, 4, 'product/35.jpg'),
('Thằng nhóc', 'Alphonse Daudet', 120000, 4, 'product/36.jpg'),
('Hoa cúc xanh', 'Karel Capek', 80000, 4, 'product/37.jpg'),
('Cẩm nang đốt nhà các văn hào New England', 'Brock Clarke', 90000, 4, 'product/38.jpg'),
('Giải ngải ký (2c)', 'Tổng Ngọc', 220000, 4, 'product/39.jpg'),
('Tục thờ Bác ở đồng bằng sông Cửu Long', 'Nguyễn Thị Đức', 70000, 2, 'product/40.jpg'),
('Vàng trong lửa', 'GS. Trần Văn Giàu, Trần Bạch Đằng', 100000, 7, 'product/41.jpg'),
('Hỏa ngục', 'Dan Brown', 210000, 4, 'product/42.jpg'),
('Điềm tỉnh và nồng nhiệt <Đỏ>', 'Ekuni Kaori', 70000, 4, 'product/43.jpg'),
('Điềm tỉnh và nồng nhiệt <Lam>', 'Tsuji Hitonari', 70000, 4, 'product/44.jpg'),
('Điều kì diệu của tiệm tạp hóa Namiya', 'Higashino Keigo', 80000, 4, 'product/45.jpg'),
('Hoa mộng ảo', 'Higashino Keigo', 90000, 4, 'product/46.jpg'),
('Lâu đài trên mây', 'Diana Wynne Jones', 110000, 4, 'product/47.jpg'),
('Ngôi nhà mũi tên', 'A. E. W. Mason', 80000, 4, 'product/48.jpg'),
('Án mạng đêm giáng sinh', 'Agatha Christie', 80000, 4, 'product/49.jpg'),
('Án mạng trên sông Nile', 'Agathe Christie', 80000, 4, 'product/50.jpg'),
('Chuỗi án mạng ABC', 'Agatha Christie', 80000, 4, 'product/51.jpg'),
('Vụ ám sát ông Roger Ackroyo', 'Agatha Christie', 80000, 4, 'product/52.jpg'),
('Vòng hoa cúc', 'Sharon Bolton', 120000, 4, 'product/53.jpg'),
('5 centimet trên giây', 'Shinkai Makoto', 70000, 4, 'product/54.jpg'),
('Nhật ký bí mật của Jimon', 'Becky Albertalli', 100000, 4, 'product/55.jpg'),
('Giữa lòng tăm tối', 'Joseph Conrad', 80000, 4, 'product/56.jpg'),
('Xuân yến', 'An Ni Bảo Bối', 120000, 4, 'product/57.jpg'),
('Chiến thắng trò chơi cuộc sống', 'Adam Khoo', 70000, 6, 'product/58.jpg'),
('Suy đến nơi nghĩ đến chốn 2', 'Anne-Sophie Chilard', 160000, 6, 'product/59.jpg'),
('Mẹ Việt dạy con bước cùng toàn cầu', 'Hồ Thị Hải Âu', 90000, 6, 'product/60.jpg'),
('Tuyển tập Thạch Lam', 'Thạch Lam', 140000, 5, 'product/61.jpg'),
('Nội các Trần Trọng Kim bản chất, vai trò và vị trí lịch sử', 'Phạm Hồng Tùng', 180000, 7, 'product/62.jpg'),
('The Godfather', 'Mario Puzo', 220000, 1, 'product/63.jpg'),
('Muôn kiếp nhân sinh (2c)', 'Nguyên Phong', 240000, 4, 'product/64.jpg'),
('Nam Bộ xưa & nay', 'Tạp chí Xưa & Nay', 120000, 7, 'product/65.jpg'),
('Giải mã tính cách, chọn lựa tương lai', 'Khởi Hàng', 70000, 6, 'product/66.jpg'),
('Minh tâm bảo giám', 'Tạ Thanh Bạch (dịch chú)', 150000, 8, 'product/67.jpg'),
('Kinh thư', 'Khổng Tử', 120000, 6, 'product/68.jpg'),
('Vạch một chân trời, chim quyên xuống đất', 'Sơn Nam', 180000, 9, 'product/69.jpg'),
('Văn đàn bảo giám', 'Trần Trung Viên', 120000 , 2, 'product/70.jpg'),
('Văn Hồ Chủ Tịch', 'Hồ Chí Minh', 500000, 5, 'product/71.jpg'),
('Đức Phật và Phật Pháp', 'Nãrada Mahã Thera', 150000, 8, 'product/72.jpg'),
('Xứ Đông Dương', 'Paul Doumer', 200000, 7, 'product/73.jpg'),
('Sherlock Holmes', 'Conan Doyle', 620000, 4, 'product/74.jpg'),
('Nghệ thuật An Nam', 'Louis Bezacier', 200000, 2, 'product/75.jpg'),
('Tinh thần ngầm', 'Dostoevsky', 250000, 10, 'product/76.jpg'),
('Thơ Đường - Poems of the T`ang Dynasty', 'Trần Trọng San', 60000, 11, 'product/77.jpg'),
('Mãi mãi tuổi hai mươi', 'Nguyễn Văn Thạc', 70000, 7, 'product/78.jpg'),
('Nghệ thuật áo mũ thời Nguyễn đầu thế kỷ XX', 'Trần Minh Nhựt', 500000, 2, 'product/79.jpg'),
('Vĩnh biệt mùa hè', 'Nguyễn Đông Thức', 60000, 4, 'product/80.jpg'),
('Diễm đi đâu?', 'Nguyễn Đông Thức', 70000, 4, 'product/81.jpg'),
('Trăm sông về biển', 'Nguyễn Đông Thức', 60000, 4, 'product/82.jpg'),
('Như núi như mây', 'Nguyễn Đông Thức', 60000, 4, 'product/83.jpg'),
('Lệ Mai', 'Lý Lan', 60000, 4, 'product/84.jpg'),
('Người đàn bà kể chuyện', 'Lý Lan', 60000, 10, 'product/85.jpg'),
('Miên man tùy bút', 'Lý Lan', 50000, 3, 'product/86.jpg'),
('Từ Dòn Mé Sán đến ...', 'Lý Lan', 60000, 4, 'product/87.jpg'),
('Bày tỏ tình yêu', 'Lý Lan', 50000, 4, 'product/88.jpg'),
('Ngón tay mình còn thơm mùi oải hương', 'Ngô Thị Giáng Uyên', 70000, 3, 'product/89.jpg'),
('Truyện ngắn đương đại Nam bộ - Contemporary short fiction from southern Vietnam', 'Nhiều tác giả', 160000, 11, 'product/90.jpg'),
('Nemesis', 'Jo Nesbo', 180000, 1, 'product/91.jpg'),
('The Redbreast', 'Jo Nesbo', 200000, 1, 'product/92.jpg'),
('Biệt động Sài Gòn-Chợ Lớn-Gia Định - Sai Gon-Cho Lon-Gia Dinh special forces', 'Nguyễn Đức Hùng(Tư Chung)', 200000, 11, 'product/93.jpg'),
('Portrait of traditional HaNoi the 1,000 year-old City of the Dragon', 'Hữu Ngọc', 120000, 1, 'product/94.jpg'),
('80 ngày vòng quanh thế giới', 'J. Vec Nơ', 80000, 4, 'product/95.jpg'),
('Alexis Zorba con người hoan lạc', 'Nikos Kazantzakis', 120000, 4, 'product/96.jpg'),
('The power of habit', 'Charles Duhigg', 150000, 1, 'product/97.jpg'),
('21 Lessons for the 21`st Century', 'Yuval Noah Harari', 200000, 1, 'product/98.jpg'),
('The drawing of the three', 'Stephen King', 120000, 1, 'product/99.jpg'),
('The lucky one', 'Nicholas Sparks', 120000, 1, 'product/100.jpg'),
('The silk roads', 'Peter Frankopan', 150000, 1, 'product/101.jpg'),
('The last don', 'Mario Puzo', 100000, 1, 'product/102.jpg'),
('The sicilian', 'Mario Puzo', 120000, 1, 'product/103.jpg'),
('The general retires', 'Nguyen Huy Thiep', 80000, 1, 'product/104.jpg'),
('A time far past', 'Le Luu', 120000, 1, 'product/105.jpg'),
('The 30 year war', 'Nhiều tác giả', 160000, 1, 'product/106.jpg'),
('The 48 laws of power', 'Robert Greene', 220000, 1, 'product/107.jpg'),
('Quo Vadis', 'Henryk Sienkiewicz', 100000, 1, 'product/108.jpg'),
('Leonardo Da Vinci', 'Walter Isaacson', 200000, 1, 'product/109.jpg'),
('Không gia đình - Nobody`s boy', 'Hector Malot', 280000, 1, 'product/110.jpg'),
('Ông già và biển cả - The old man and the sea', 'Ernest Hemingway', 80000, 11, 'product/111.jpg'),
('Hai vạn dặm dưới biển', 'Jules Verne', 100000, 11, 'product/112.jpg'),
('One hundred names', 'Cecelia Ahern', 120000, 1, 'product/113.jpg'),
('Madame Bovary', 'Gustave Flaubert', 100000, 1, 'product/114.jpg'),
('Tell it to the Skies', 'Erica James', 100000, 1, 'product/115.jpg'),
('Valentine', 'Rebecca Farnworth', 120000, 1, 'product/116.jpg'),
('Emma', 'Jane Austen', 100000, 1, 'product/117.jpg'),
('How to develop self-confidence and influence people by public speaking', 'Dale Carnegie', 100000, 1, 'product/118.jpg'),
('Tôn Tử mưu lược nhân sinh', 'Hùng Trung Vũ', 80000, 6, 'product/119.jpg'),
('Những điệp viên may mắn', 'Nguyễn Văn Tàu', 120000, 7, 'product/120.jpg'),
('Đọc hồi ký của các tướng tá Sài Gòn xuất bản ở nước ngoài', 'Mai Nguyễn', 80000, 7, 'product/121.jpg'),
('Tóm tắt niên biểu lịch sử Việt Nam', 'Nhiều tác giả', 100000, 7, 'product/122.jpg'),
('Đại tướng Võ Nguyên Giáp - Chiến đấu trong vòng vây', 'Hữu Mai', 120000, 7, 'product/123.jpg'),
('Đại tướng Võ Nguyên Giáp - Điện Biên Phủ-Điểm hạn lịch sử ', 'Hữu Mai', 120000, 7, 'product/124.jpg'),
('Đại tướng Võ Nguyên Giáp - Những năm tháng không thể nào quên', 'Hữu Mai', 120000, 7, 'product/125.jpg'),
('Chính trị thế giới sau năm 1945', 'Peter Calvocoressi', 160000, 7, 'product/126.jpg'),
('Gia Định Thành Thông Chí', 'Trịnh Hoài Đức', 220000, 7, 'product/127.jpg'),
('Sử liệu cổ nhạc Việt Nam', 'Đặng Hoành Loan', 250000, 2, 'product/128.jpg'),
('Kinh Thư', 'Khổng Tử', 120000, 6, 'product/129.jpg'),
('Thư pháp & thiền', 'Nguyễn Bá Hoàn', 100000, 6, 'product/130.jpg'),
('Khuyến học', 'Fukuzawa Yukichi', 700000, 6, 'product/131.jpg'),
('Việt Nam phong tục', 'Phan Kế Bình', 300000, 2, 'product/132.jpg'),
('Đầu xuân ra sông giặt áo', 'Nguyễn Nhật Ánh', 160000, 5, 'product/133.jpg'),
('Truyện cổ tích của anh em Grimm', 'Ae Grimm', 1200000, 5, 'product/134.jpg'),
('Ngyễn Tuân truyện ngắn', 'Nguyễn Tuân', 130000, 5, 'product/135.jpg'),
('Ngọn đèn dầu lạc', 'Nguyễn Tuân', 90000, 5, 'product/136.jpg'),
('Những thành phố trôi dạt', 'Nguyễn Vĩnh Nguyên', 70000, 4, 'product/137.jpg'),
('Động vật trong thành phố', 'Nguyễn Vĩnh Nguyên', 60000, 4, 'product/138.jpg'),
('Những dòng tơ', 'Nguyễn Đắc Thắng', 50000, 4, 'product/139.jpg'),
('Đời sống trong thiền viện Nhật Bản', 'Daisetzteitaro Suzuki', 60000, 2, 'product/140.jpg'),
('THuốc mê', 'Thâm Tâm', 70000, 4, 'product/141.jpg'),
('Climats', 'André Maurois', 40000, 1, 'product/142.jpg'),
('50 great short stories', 'Nhiều tác giả', 80000, 1, 'product/143.jpg'),
('61 hours', 'Lee Child', 90000, 1, 'product/144.jpg'),
('Extreme most wanted', 'Chris Ryan', 60000, 1, 'product/145.jpg'),
('Angels & Demons', 'Dan Brown', 120000, 1, 'product/146.jpg'),
('Nghệ thuật khởi đầu câu chuyện & kêt bạn', 'Don Ga Bor, Prentice Hall', 50000, 6, 'product/147.jpg'),
('Để thắng tính nhút nhát', 'Raymond De Saint Laurent, Jean Chartier', 60000, 6, 'product/148.jpg'),
('Sống thoải mái', 'Phạm Côn Sơn', 50000, 6, 'product/149.jpg'),
('Gửi đây chút duyên tình đọc', 'Nguyễn Thị Thanh Xuân', 70000, 5, 'product/150.jpg'),
('Giai thoại làng nho', 'Lãng Nhân', 700000, 2, 'product/151.jpg'),
('Nhị-Thập Tứ Hiếu', 'Lý Văn Phức', 500000, 6, 'product/152.jpg'),
('Thuật huấn cán', 'Hoàng Xuân Việt', 300000, 6, 'product/153.jpg'),
('Sự nghiệp và đời người', 'Hoàng Xuân Việt', 300000, 6, 'product/154.jpg'),
('Những tiếng trống qua cửa các nhà sấm', 'Huệ Thiên', 180000, 5, 'product/155.jpg'),
('Tổng Bí thư Lê Duẩn - Party General Secretary Lê Duẩn', 'NXB Thông Tấn', 220000, 11, 'product/156.jpg'),
('Ngũ luân thư', 'Miyamoto Musashi', 100000, 6, 'product/157.jpg'),
('Khởi sinh của cô độc', 'Paul Auster', 80000, 4, 'product/158.jpg'),
('Người trộm bóng', 'Marc Levy', 120000, 4, 'product/159.jpg'),
('Xấu', 'Natsuo Kirino', 70000, 4, 'product/160.jpg'),
('Góp phần nghiên cứu văn hóa và tộc người', 'Nguyễn Tư Chí', 150000, 2, 'product/161.jpg'),
('Làm sách phục vụ dạy-học tiếng các dân tộc thiểu số', 'Vũ Bá Hoà, Vũ Viết Chinh, Hà Thị Hải Yến, Bùi Tất Tươm', 120000, 6, 'product/162.jpg'),
('Dấu chân người kiến tạo trên hành trình đến nền giáo dục mở', 'NGND.TS Thái Văn Long', 100000, 2, 'product/163.jpg'),
('Madam Như Trần Lệ Xuân - Quyền lực bà rồng', 'Monique Brinson Demery', 220000, 4, 'product/164.jpg'),
('Không đánh mà thắng', 'Cao Kiến Hoa', 150000, 6, 'product/165.jpg'),
('Quy luật vũ trụ', 'Joanna Martine Woolfolk', 120000, 4, 'product/166.jpg'),
('Phân tích chứng khoán', 'Benjamin Graham David L. Dodd', 130000, 6, 'product/167.jpg'),
('Nhìn lại quá khứ', 'Robert S. McNamara', 180000, 7, 'product/168.jpg'),
('Theatre', 'W. Somerset Maugham', 50000, 1, 'product/169.jpg'),
('Nikolai gogol and the west european novel', 'Anna Yelistratova', 100000, 1, 'product/170.jpg'),
('Men of mathematics', 'E. T. Bell', 60000, 1, 'product/171.jpg'),
('The ways of thinking of eastern peoples', 'H. Nakamura', 120000, 1, 'product/172.jpg'),
('Sons and lovers', 'D. H. Lawrence', 50000, 1, 'product/173.jpg'),
('Moby Dick', 'Herman Melville', 50000, 1, 'product/174.jpg'),
('Thế giới phẳng', 'Thomas L. Friedman', 120000, 7, 'product/175.jpg'),
('Bay di nhung con mua phun', 'Phạm Công Thiện', 250000, 4, 'product/176.jpg'),
('Y thuc moi - Trong van nghe va triet hoc', 'Phạm Công Thiện', 300000, 4, 'product/177.jpg'),
('Im lang ho tham', 'Phạm Công Thiện', 300000, 4, 'product/178.jpg'),
('Việt-Nam Văn-Học Sử-Yếu', 'Dương Quảng Hàm', 500000, 5, 'product/179.jpg'),
('Thuật lãnh đạo', 'Hoàng Xuân Việt', 200000, 6, 'product/180.jpg'),
('Cá tính của Miền nam', 'Sơn Nam', 60000, 4, 'product/181.jpg'),
('Mật mã Da Vinci', 'Dan Brown', 300000, 4, 'product/182.jpg'),
('Thiên thần bóng tối (2c)', 'Chi Chan', 140000, 4, 'product/183.jpg'),
('Rừng Nauy', 'Haruki Murakami', 80000, 4, 'product/184.jpg'),
('10 điều khác biệt nhất giữa kẻ giàu & người nghèo', 'Keith Cameron Smith', 80000, 6, 'product/185.jpg'),
('Sức mạnh của lắng nghe', 'Bernard T. Ferrari', 80000, 6, 'product/186.jpg'),
('Bở đi mà sống', 'Mèo Xù', 70000, 3, 'product/187.jpg'),
('Nghĩ đơn giản, sống đơn thuần', 'Tolly Burkan', 70000, 6, 'product/188.jpg'),
('Mặc kệ nó làm tới đi!', 'Richard Branson', 70000, 6, 'product/189.jpg'),
('Mình là cá, việc của mình là bơi ', 'Takeshi Furukawa', 80000, 6, 'product/190.jpg'),
('Sống sót nơi công sở', 'Karen Dillon', 80000, 6, 'product/191.jpg'),
('Deep work làm ra làm chơi ra chơi', 'Cal Newport', 120000, 6, 'product/192.jpg'),
('Yes or No những quyết định thay đổi cuộc sống', 'Spencer Johnson, M. D', 100000, 6, 'product/193.jpg'),
('Trả lại nụ hôn', 'Dương Thụy', 70000, 4, 'product/194.jpg'),
('Oxford thương yêu', 'Dương Thụy', 80000, 4, 'product/195.jpg'),
('Nhắm mắt thấy Paris', 'Dương Thụy', 60000, 4, 'product/196.jpg'),
('Cung đường vàng nắng', 'Dương Thụy', 80000, 4, 'product/197.jpg');
