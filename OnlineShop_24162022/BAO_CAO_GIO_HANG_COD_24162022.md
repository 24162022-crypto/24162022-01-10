# Bao cao: Gio hang + Thanh toan COD (role User) - OnlineShop_24162022

## 1. Danh sach file
### Moi tao
| File | Mo ta |
|---|---|
| src/main/resources/cart_checkout_24162022.sql | ALTER TABLE Cart them 6 cot (chay 1 lan cho DB cu) + du lieu gio mau (comment) |
| entity/Cart_24162022.java, entity/CartItem_24162022.java | Entity gio/don va dong gio (hang so MAX_QUANTITY_PER_LINE = 99) |
| dao/CartDAO_24162022.java | JDBC gio hang: tim/tao gio, them/sua/xoa dong, dem so dong |
| dao/OrderDAO_24162022.java | JDBC don hang: khoa gio/san pham, tru kho an toan, chuyen gio -> don, lich su |
| service/CartService_24162022.java | Nghiep vu gio hang (validate 1..min(stock,99), cong don, kiem tra quyen so huu) |
| service/OrderService_24162022.java | Dat hang COD trong 1 transaction JDBC, validate thong tin nhan hang |
| filter/UserAuthFilter_24162022.java | Bao ve /cart, /checkout, /order-success, /orders (chua login -> /login, sai role -> thong bao) |
| controller/CartServlet_24162022.java | /cart: GET xem; POST add/update/remove/clear (PRG + flash) |
| controller/CheckoutServlet_24162022.java | /checkout: GET form, POST dat hang |
| controller/OrderSuccessServlet_24162022.java | /order-success?id= (chi chu don xem duoc) |
| controller/OrderHistoryServlet_24162022.java | /orders (danh sach) va /orders?id= (chi tiet) - phan tuy chon |
| WEB-INF/jsp/cart/cart.jsp | Trang gio hang |
| WEB-INF/jsp/order/checkout.jsp, success.jsp, detail.jsp, list.jsp, order-info.jsp | Thanh toan, xac nhan, chi tiet, lich su, phan thong tin don dung chung |
| WEB-INF/jsp/common/cart-flash.jsp | Hien flash message 1 lan roi xoa |
| WEB-INF/jsp/common/user-only.jsp | Trang "khong co quyen" cho Admin/Seller |

### Da sua
| File | Noi dung sua |
|---|---|
| src/main/resources/db_24162022.sql | CREATE TABLE Cart them cot receiverName, receiverPhone, shippingAddress, note, paymentMethod, totalAmount |
| controller/LoginServlet_24162022.java | Ho tro quay lai URL cu sau dang nhap (session `redirectAfterLogin`, tham so `next`), nap `cartCount` |
| WEB-INF/jsp/product/detail.jsp | Hien ton kho, o so luong + nut "Them vao gio hang" (User), nut dang nhap (khach), flash |
| WEB-INF/jsp/product/list.jsp | Nut "Them vao gio" (so luong 1), flash, id cho tung dong |
| WEB-INF/decorators/header.jsp | Link "Gio hang (n)" va "Don hang cua toi" cho role User |
| assets/css/style_24162022.css | Them class: btn-checkout, inline-form, qty-input, cart-table, cart-warn, cart-actions, radio-row... |

## 2. Huong dan chay
1. May cai moi: chay `db_24162022.sql` roi `data_24162022.sql` (db da gom cac cot moi cua Cart).
2. May da co DB cu: chay them `cart_checkout_24162022.sql` DUNG 1 LAN (khong chay lai db_24162022.sql vi se mat du lieu).
3. Sua mat khau MySQL trong DBConnection_24162022 (file nay khong bi thay doi).
4. `mvn clean package` -> deploy `target/OnlineShop_24162022.war` len Tomcat 10.1 (hoac Run on Server trong Eclipse/STS/IntelliJ).
5. Mo http://localhost:8080/OnlineShop_24162022/ ; tai khoan test: user1..3 / 123456 (User), seller1..3, admin.

## 3. Quy uoc va gia dinh
- `Product.stock` la ton kho de gioi han so luong va tru khi dat hang (`amount` KHONG dung).
- Gioi han so luong moi dong: 1 <= quantity <= min(stock, 99). Hang so `CartItem_24162022.MAX_QUANTITY_PER_LINE = 99`.
- Sua so luong = 0 => xoa dong (o o nhap trinh duyet chan min=1; server van chap nhan 0 theo quy uoc nay).
- Them trung san pham: cong don; tong vuot min(stock,99) => TU CHOI va bao loi (khong tu cat). Khi cong don, don gia cap nhat theo gia hien tai.
- Header dung `cartCount` (so MAT HANG = so dong) luu Session, cap nhat sau moi thao tac va luc dang nhap.
- Cart.status: 0 = gio dang mo; 1 = da dat hang (COD, cho giao). Don hang = Cart co status >= 1.
- Chong dat trung: form gui kem `cartId`; neu gio do da dat roi thi chuyen thang toi trang xac nhan, khong tru kho lan 2. Dong thoi gio bi khoa `SELECT ... FOR UPDATE` trong transaction.
- Thanh tien lam tron den dong VND (Math.round) vi cot gia la FLOAT.
- Admin/Seller vao /cart, /checkout, /orders thay trang "Khong co quyen truy cap" (khong redirect).
- Chi khi dang nhap bang role User moi quay lai URL da luu; Admin/Seller vao trang rieng cua ho nhu cu.
- Khong co thu vien moi, pom.xml khong doi.

## 4. Kich ban test thu cong
| # | Buoc | Ket qua mong doi |
|---|---|---|
| 1 | Dang nhap user1 -> /products -> "Them vao gio" o "Lap trinh Java co ban" | Flash xanh, header "Gio hang (1)" |
| 2 | Vao /product?id=<id sach do>, so luong 3, "Them vao gio hang" | Cong don: so luong trong gio = 4, header van (1) |
| 3 | Them so luong lon hon ton kho (vd Tai nghe Bluetooth ton 18, nhap 18 roi them tiep 1) | Flash do "Khong the them: ... vuot muc toi da ...", gio khong doi |
| 4 | /cart: sua so luong 2 -> Cap nhat | Thanh tien va tong tien doi dung |
| 5 | Sua so luong khong hop le: de trong / chu / 999 (bo `max` bang DevTools de gui) | Flash do, so luong cu giu nguyen |
| 6 | Bam "Xoa" 1 dong, xac nhan | Dong bien mat, header giam |
| 7 | "Xoa toan bo gio hang", xac nhan | Gio trong, hien link "Tiep tuc mua hang", an nut Thanh toan |
| 8 | Dang xuat, vao /cart | Chuyen /login; dang nhap user1 xong tu quay lai /cart |
| 9 | Dang nhap admin hoac seller1, vao /cart | Trang "Khong co quyen truy cap", khong thay link gio hang tren header |
| 10 | user1 them 2 san pham, /checkout, nhap day du, Dat hang | Chuyen /order-success?id=..., hien ma don, thong tin, tong tien, COD; DB: Product.stock giam, Cart.status = 1, buyDate/totalAmount co gia tri; header "Gio hang (0)" |
| 11 | Vao /checkout khi gio trong | Chuyen /cart kem thong bao gio trong |
| 12 | Tai trang /order-success bam F5 / nhan Back roi gui lai form | Khong tao don thu 2, khong tru kho lan nua |
| 13 | Dat hang voi SDT sai ("123"), dia chi rong | Quay lai form, giu du lieu da nhap, bao loi |
| 14 | Dang nhap user2, mo URL /order-success?id=<ma don cua user1> hoac /orders?id=... | Bao "Don hang khong ton tai hoac khong thuoc ve ban" |
| 15 | user1 va user2 cung co san pham ton 1; user1 dat truoc, user2 dat sau | user2 bi bao "khong du so luong", rollback, gio van con |
| 16 | /orders | Danh sach don cua user, xem chi tiet duoc |

Kiem tra DB nhanh:
```sql
SELECT cartId, userId, status, buyDate, receiverName, paymentMethod, totalAmount FROM Cart;
SELECT productName, stock FROM Product;
```

## 5. Lenh Git
```bash
git add .
git commit -m "Them chuc nang gio hang va thanh toan COD cho vai tro User"
git push origin <ten-nhanh>
```
Han commit: 16g00 ngay 1/10/2026.
