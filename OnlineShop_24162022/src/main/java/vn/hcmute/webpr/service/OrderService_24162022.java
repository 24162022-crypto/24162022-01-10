package vn.hcmute.webpr.service;

import vn.hcmute.webpr.dao.CartDAO_24162022;
import vn.hcmute.webpr.dao.OrderDAO_24162022;
import vn.hcmute.webpr.dao.UserDAO_24162022;
import vn.hcmute.webpr.entity.Cart_24162022;
import vn.hcmute.webpr.entity.CartItem_24162022;
import vn.hcmute.webpr.entity.Product_24162022;
import vn.hcmute.webpr.entity.User_24162022;
import vn.hcmute.webpr.util.DBConnection_24162022;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * BUSINESS LAYER (SERVICE) - nghiep vu Thanh toan / Don hang (COD).
 * Dat hang chay trong 1 transaction JDBC: khoa gio -> kiem tra tung dong -> tru ton kho an toan
 * -> chuyen Cart.status 0 -> 1. Bat ky loi nao deu rollback, khong de lai du lieu do dang.
 */
public class OrderService_24162022 {

    public static final String PAYMENT_COD = "COD";

    private static final Pattern PHONE_PATTERN = Pattern.compile("^(0|\\+84)\\d{9}$");
    private static final int MAX_NAME = 100;
    private static final int MAX_ADDRESS = 300;
    private static final int MAX_NOTE = 300;

    private final CartDAO_24162022 cartDAO = new CartDAO_24162022();
    private final OrderDAO_24162022 orderDAO = new OrderDAO_24162022();
    private final UserDAO_24162022 userDAO = new UserDAO_24162022();

    /** So dien thoai da luu cua User (dien san vao form thanh toan); co the null. */
    public String getUserPhone(int userId) throws Exception {
        User_24162022 u = userDAO.findById(userId);
        return (u == null) ? null : u.getPhone();
    }

    /**
     * Dat hang COD.
     * @param cartId ma gio lay tu form (chong dat trung): neu gio nay da dat roi thi tra ve luon ma don
     *               ma khong tru kho lan nua. Co the rong -> dung gio dang mo cua User.
     * @return ma don hang (cartId)
     */
    public String placeCodOrder(int userId, String cartId, String receiverName, String receiverPhone,
                                String shippingAddress, String note, String paymentMethod) throws Exception {
        // ----- 1. Validate du lieu nhap (server-side) -----
        receiverName = trim(receiverName);
        receiverPhone = trim(receiverPhone);
        shippingAddress = trim(shippingAddress);
        note = trim(note);

        if (receiverName.isEmpty()) {
            throw new Exception("Vui long nhap ho ten nguoi nhan.");
        }
        if (receiverName.length() > MAX_NAME) {
            throw new Exception("Ho ten qua dai (toi da " + MAX_NAME + " ky tu).");
        }
        if (!PHONE_PATTERN.matcher(receiverPhone).matches()) {
            throw new Exception("So dien thoai khong hop le (vi du 0912345678 hoac +84912345678).");
        }
        if (shippingAddress.isEmpty()) {
            throw new Exception("Vui long nhap dia chi giao hang.");
        }
        if (shippingAddress.length() > MAX_ADDRESS) {
            throw new Exception("Dia chi qua dai (toi da " + MAX_ADDRESS + " ky tu).");
        }
        if (note.length() > MAX_NOTE) {
            throw new Exception("Ghi chu qua dai (toi da " + MAX_NOTE + " ky tu).");
        }
        if (!PAYMENT_COD.equals(paymentMethod)) {
            throw new Exception("Hien tai chi ho tro thanh toan khi nhan hang (COD).");
        }

        // ----- 2. Transaction JDBC -----
        Connection conn = null;
        try {
            conn = DBConnection_24162022.getConnection();
            conn.setAutoCommit(false);

            String id = trim(cartId);
            if (id.isEmpty()) {
                id = cartDAO.findOpenCartId(conn, userId);
                if (id == null) {
                    throw new Exception("Gio hang dang trong, khong the dat hang.");
                }
            }

            // Khoa dong Cart: 2 request dat hang dong thoi se xep hang cho nhau
            Cart_24162022 cart = orderDAO.lockCart(conn, id, userId);
            if (cart == null) {
                throw new Exception("Gio hang khong ton tai hoac khong thuoc ve ban.");
            }
            if (cart.getStatus() >= Cart_24162022.STATUS_ORDERED) {
                // Da dat roi (F5 / bam 2 lan) -> khong tru kho lai, tra ve ma don cu
                conn.rollback();
                return id;
            }

            List<CartItem_24162022> items = cartDAO.findItemsByCartId(conn, id); // da ORDER BY productId
            if (items.isEmpty()) {
                throw new Exception("Gio hang dang trong, khong the dat hang.");
            }

            double total = 0;
            for (CartItem_24162022 it : items) {
                Product_24162022 p = orderDAO.lockProduct(conn, it.getProductId());
                if (p == null || p.getStatus() != 1) {
                    throw new Exception("San pham \"" + it.getProductName() + "\" khong con duoc ban.");
                }
                if (orderDAO.decreaseStock(conn, it.getProductId(), it.getQuantity()) == 0) {
                    throw new Exception("San pham \"" + it.getProductName() + "\" khong du so luong (con "
                            + p.getStock() + ", ban dat " + it.getQuantity() + ").");
                }
                total += it.getLineTotal();
            }

            int updated = orderDAO.markOrdered(conn, id, userId, receiverName, receiverPhone,
                    shippingAddress, note.isEmpty() ? null : note, PAYMENT_COD, total);
            if (updated == 0) {
                throw new Exception("Khong the hoan tat don hang, vui long thu lai.");
            }
            conn.commit();
            return id;
        } catch (SQLException e) {
            rollbackQuietly(conn);
            throw new Exception("Loi co so du lieu khi dat hang: " + e.getMessage());
        } catch (Exception e) {
            rollbackQuietly(conn);
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException ignored) {
                    // bo qua
                }
            }
            DBConnection_24162022.close(conn);
        }
    }

    /** Lay don hang (kem cac dong) - chi tra ve neu don thuoc dung User. */
    public Cart_24162022 getOrderOfUser(int userId, String cartId) throws Exception {
        if (trim(cartId).isEmpty()) {
            throw new Exception("Thieu ma don hang.");
        }
        Cart_24162022 order = orderDAO.findOrderOfUser(cartId.trim(), userId);
        if (order == null) {
            throw new Exception("Don hang khong ton tai hoac khong thuoc ve ban.");
        }
        order.setItems(cartDAO.findItemsByCartId(order.getCartId()));
        return order;
    }

    /** Lich su don hang cua User (moi nhat truoc). */
    public List<Cart_24162022> getOrdersOfUser(int userId) throws Exception {
        return orderDAO.findOrdersByUser(userId);
    }

    private void rollbackQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ignored) {
                // bo qua
            }
        }
    }

    private String trim(String s) {
        return s == null ? "" : s.trim();
    }
}
