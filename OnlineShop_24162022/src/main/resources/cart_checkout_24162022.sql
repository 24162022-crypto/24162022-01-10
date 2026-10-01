-- =========================================================
-- Gio hang + Thanh toan COD - OnlineShop_24162022 (MySQL)
-- CHI CHAY 1 LAN, danh cho may DA CO san database cu (bang Cart chua co cac cot nhan hang).
-- May cai moi (chay db_24162022.sql sau khi cap nhat) KHONG can chay file nay.
-- Neu chay lai se bao loi "Duplicate column name" - khong anh huong du lieu.
-- =========================================================
USE OnlineShop_24162022;

ALTER TABLE Cart ADD COLUMN receiverName NVARCHAR(100);
ALTER TABLE Cart ADD COLUMN receiverPhone NVARCHAR(20);
ALTER TABLE Cart ADD COLUMN shippingAddress NVARCHAR(300);
ALTER TABLE Cart ADD COLUMN note NVARCHAR(300);
ALTER TABLE Cart ADD COLUMN paymentMethod VARCHAR(20);
ALTER TABLE Cart ADD COLUMN totalAmount DOUBLE;

-- ---------------------------------------------------------
-- (TUY CHON) Du lieu gio mau de test: user1 co san 1 gio (status = 0) gom 2 san pham.
-- Chay SAU data_24162022.sql. Bo comment 2 khoi lenh duoi neu muon dung.
-- ---------------------------------------------------------
-- INSERT INTO Cart(cartId, userId, buyDate, status)
-- VALUES ('demo-cart-user1', (SELECT userId FROM Users WHERE username = 'user1'), NULL, 0);
-- INSERT INTO CartItem(cartItemId, quantity, unitPrice, productId, cartId) VALUES
-- ('demo-item-1', 2, 150000, (SELECT productId FROM Product WHERE productCode = 1000000001), 'demo-cart-user1'),
-- ('demo-item-2', 1, 890000, (SELECT productId FROM Product WHERE productCode = 2000000001), 'demo-cart-user1');
