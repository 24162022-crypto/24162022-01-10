-- =========================================================
-- Du lieu test cho Cau 3, 4, 5 - OnlineShop_24162022 (MySQL)
-- Chay SAU file db_24162022.sql (chi chay 1 lan).
-- Anh (images) la ten file trong src/main/webapp/assets/images/
-- =========================================================
USE OnlineShop_24162022;

-- ---------- Seller (Shop Sach ABC da co san tu db_24162022.sql) ----------
INSERT INTO Seller(sellername, images, status) VALUES
('Shop Dien Tu XYZ', 'seller2.jpg', 1),
('Shop Thoi Trang MNO', 'seller3.jpg', 1);

-- ---------- Category (7 danh muc -> 2 trang khi test phan trang, PAGE_SIZE = 5) ----------
INSERT INTO Category(categoryName, images, status) VALUES
('Sach', 'c1.svg', 1),
('Dien tu', 'c2.svg', 1),
('Thoi trang', 'c3.svg', 1),
('Gia dung', 'c4.svg', 1),
('The thao', 'c5.svg', 1),
('Do choi', 'c6.svg', 1),
('My pham', 'c7.svg', 0);

-- ---------- Product: 3 san pham / cua hang ----------
INSERT INTO Product(productName, productCode, categoryId, description, price, amount, stock, images, wishlist, status, createDate, sellerId) VALUES
('Lap trinh Java co ban', 1000000001, (SELECT categoryId FROM Category WHERE categoryName = 'Sach'),
 'Giao trinh Java tu co ban den huong doi tuong, kem bai tap thuc hanh.', 150000, 50, 45, 'p1.svg', 0, 1, '2026-09-01',
 (SELECT sellerId FROM Seller WHERE sellername = 'Shop Sach ABC')),
('Clean Code', 1000000002, (SELECT categoryId FROM Category WHERE categoryName = 'Sach'),
 'Sach kinh dien ve cach viet ma nguon sach, de doc va de bao tri.', 220000, 30, 28, 'p2.svg', 0, 1, '2026-09-02',
 (SELECT sellerId FROM Seller WHERE sellername = 'Shop Sach ABC')),
('Cau truc du lieu va giai thuat', 1000000003, (SELECT categoryId FROM Category WHERE categoryName = 'Sach'),
 'Trinh bay cac cau truc du lieu co ban va giai thuat sap xep, tim kiem.', 180000, 40, 40, 'p3.svg', 0, 1, '2026-09-03',
 (SELECT sellerId FROM Seller WHERE sellername = 'Shop Sach ABC')),

('Tai nghe Bluetooth', 2000000001, (SELECT categoryId FROM Category WHERE categoryName = 'Dien tu'),
 'Tai nghe khong day, chong on chu dong, pin 30 gio.', 890000, 20, 18, 'p4.svg', 0, 1, '2026-09-05',
 (SELECT sellerId FROM Seller WHERE sellername = 'Shop Dien Tu XYZ')),
('Chuot khong day', 2000000002, (SELECT categoryId FROM Category WHERE categoryName = 'Dien tu'),
 'Chuot khong day 2.4GHz, DPI 1600, pin AA.', 250000, 60, 55, 'p5.svg', 0, 1, '2026-09-06',
 (SELECT sellerId FROM Seller WHERE sellername = 'Shop Dien Tu XYZ')),
('Ban phim co', 2000000003, (SELECT categoryId FROM Category WHERE categoryName = 'Dien tu'),
 'Ban phim co switch blue, den nen RGB, ket noi USB-C.', 1200000, 15, 15, 'p6.svg', 0, 1, '2026-09-07',
 (SELECT sellerId FROM Seller WHERE sellername = 'Shop Dien Tu XYZ')),

('Ao thun cotton', 3000000001, (SELECT categoryId FROM Category WHERE categoryName = 'Thoi trang'),
 'Ao thun 100% cotton, form rong, nhieu mau.', 120000, 100, 90, 'p7.svg', 0, 1, '2026-09-10',
 (SELECT sellerId FROM Seller WHERE sellername = 'Shop Thoi Trang MNO')),
('Quan jean nam', 3000000002, (SELECT categoryId FROM Category WHERE categoryName = 'Thoi trang'),
 'Quan jean ong dung, chat lieu denim co gian.', 350000, 40, 38, 'p8.svg', 0, 1, '2026-09-11',
 (SELECT sellerId FROM Seller WHERE sellername = 'Shop Thoi Trang MNO')),
('Giay the thao', 3000000003, (SELECT categoryId FROM Category WHERE categoryName = 'The thao'),
 'Giay chay bo nhe, de em, thoang khi.', 650000, 25, 25, 'p9.svg', 0, 1, '2026-09-12',
 (SELECT sellerId FROM Seller WHERE sellername = 'Shop Thoi Trang MNO'));

-- ---------- Users: 3 tai khoan Seller (mat khau 123456) + tai khoan User de test phan trang ----------
INSERT INTO Users(username, email, fullname, password, phone, status, roleId, sellerid) VALUES
('seller1', 'seller1@example.com', 'Chu Shop Sach ABC', '123456', '0911111111', 1,
 (SELECT roleId FROM UserRoles WHERE roleName = 'Seller'), (SELECT sellerId FROM Seller WHERE sellername = 'Shop Sach ABC')),
('seller2', 'seller2@example.com', 'Chu Shop Dien Tu XYZ', '123456', '0922222222', 1,
 (SELECT roleId FROM UserRoles WHERE roleName = 'Seller'), (SELECT sellerId FROM Seller WHERE sellername = 'Shop Dien Tu XYZ')),
('seller3', 'seller3@example.com', 'Chu Shop Thoi Trang MNO', '123456', '0933333333', 1,
 (SELECT roleId FROM UserRoles WHERE roleName = 'Seller'), (SELECT sellerId FROM Seller WHERE sellername = 'Shop Thoi Trang MNO')),
('user1', 'user1@example.com', 'Nguyen Van A', '123456', '0944444441', 1, (SELECT roleId FROM UserRoles WHERE roleName = 'User'), NULL),
('user2', 'user2@example.com', 'Tran Thi B', '123456', '0944444442', 1, (SELECT roleId FROM UserRoles WHERE roleName = 'User'), NULL),
('user3', 'user3@example.com', 'Le Van C', '123456', '0944444443', 1, (SELECT roleId FROM UserRoles WHERE roleName = 'User'), NULL),
('user4', 'user4@example.com', 'Pham Thi D', '123456', '0944444444', 0, (SELECT roleId FROM UserRoles WHERE roleName = 'User'), NULL);
