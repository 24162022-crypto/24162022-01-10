-- =========================================================
-- Database: OnlineShop_24162022 (MySQL)
-- Mon Lap Trinh Web - De thi Qua trinh - De so 06 - Cau 1
-- =========================================================
CREATE DATABASE IF NOT EXISTS OnlineShop_24162022;
USE OnlineShop_24162022;

CREATE TABLE UserRoles (
    roleId INT PRIMARY KEY AUTO_INCREMENT,
    roleName NVARCHAR(50)
);

CREATE TABLE Seller (
    sellerId INT PRIMARY KEY AUTO_INCREMENT,
    sellername NVARCHAR(50),
    images NVARCHAR(500),
    status INT DEFAULT 1
);

CREATE TABLE Users (
    userId INT PRIMARY KEY AUTO_INCREMENT,
    username NVARCHAR(50),
    email NVARCHAR(100),
    fullname NVARCHAR(50),
    password NVARCHAR(50),
    images NVARCHAR(500),
    phone NVARCHAR(20),
    status INT DEFAULT 0,
    code NVARCHAR(50),
    roleId INT,
    sellerid INT,
    FOREIGN KEY (roleId) REFERENCES UserRoles(roleId),
    FOREIGN KEY (sellerid) REFERENCES Seller(sellerId)
);

CREATE TABLE Category (
    categoryId INT PRIMARY KEY AUTO_INCREMENT,
    categoryName NVARCHAR(200),
    images NVARCHAR(500),
    status INT DEFAULT 1
);

CREATE TABLE Product (
    productId INT PRIMARY KEY AUTO_INCREMENT,
    productName NVARCHAR(200),
    productCode BIGINT,
    categoryId INT,
    description NVARCHAR(500),
    price FLOAT,
    amount INT,
    stock INT,
    images NVARCHAR(500),
    wishlist INT DEFAULT 0,
    status INT DEFAULT 1,
    createDate DATE,
    sellerId INT,
    FOREIGN KEY (categoryId) REFERENCES Category(categoryId),
    FOREIGN KEY (sellerId) REFERENCES Seller(sellerId)
);

-- Cart: status = 0 (gio hang dang mo), 1 (da dat hang - thanh toan COD, cho giao)
-- Cac cot receiverName.. totalAmount chi co gia tri sau khi dat hang (checkout COD)
CREATE TABLE Cart (
    cartId VARCHAR(50) PRIMARY KEY,
    userId INT,
    buyDate DATETIME,
    status INT DEFAULT 0,
    receiverName NVARCHAR(100),
    receiverPhone NVARCHAR(20),
    shippingAddress NVARCHAR(300),
    note NVARCHAR(300),
    paymentMethod VARCHAR(20),
    totalAmount DOUBLE,
    FOREIGN KEY (userId) REFERENCES Users(userId)
);

CREATE TABLE CartItem (
    cartItemId VARCHAR(50) PRIMARY KEY,
    quantity INT,
    unitPrice FLOAT,
    productId INT,
    cartId VARCHAR(50),
    FOREIGN KEY (productId) REFERENCES Product(productId),
    FOREIGN KEY (cartId) REFERENCES Cart(cartId)
);

-- Du lieu mau toi thieu de test cau 1 (cac cau sau se them them)
INSERT INTO UserRoles(roleName) VALUES ('Admin'), ('User'), ('Seller');

INSERT INTO Seller(sellername, images, status) VALUES
('Shop Sach ABC', 'seller1.jpg', 1);

INSERT INTO Users(username, email, fullname, password, phone, status, roleId) VALUES
('admin', 'admin@example.com', 'Quan Tri Vien', '123456', '0900000000', 1, 1);

-- =========================================================
-- Neu ban da tung dang ky thu (truoc khi cau hinh SMTP that)
-- va bi ket o buoc OTP (khong nhan duoc mail), chay lenh duoi
-- de kich hoat thu cong tai khoan do (thay 'username_cua_ban'):
-- UPDATE Users SET status = 1, code = NULL WHERE username = 'username_cua_ban';
