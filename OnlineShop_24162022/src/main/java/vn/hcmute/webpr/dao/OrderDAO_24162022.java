package vn.hcmute.webpr.dao;

import vn.hcmute.webpr.entity.Cart_24162022;
import vn.hcmute.webpr.entity.Product_24162022;
import vn.hcmute.webpr.util.DBConnection_24162022;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DATA ACCESS LAYER (DAO) - don hang (Cart co status >= 1).
 * Cac ham nhan tham so Connection duoc goi trong 1 transaction do OrderService quan ly
 * (setAutoCommit(false) / commit / rollback), DAO KHONG tu commit va KHONG tu dong ket noi.
 */
public class OrderDAO_24162022 {

    /** Khoa dong Cart (FOR UPDATE) cua dung User; null neu khong ton tai / khong phai cua User. */
    public Cart_24162022 lockCart(Connection conn, String cartId, int userId) throws SQLException {
        String sql = "SELECT * FROM Cart WHERE cartId = ? AND userId = ? FOR UPDATE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cartId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapCart(rs) : null;
            }
        }
    }

    /** Khoa dong Product (FOR UPDATE) de doc status/stock chinh xac trong transaction. */
    public Product_24162022 lockProduct(Connection conn, int productId) throws SQLException {
        String sql = "SELECT productId, productName, status, stock FROM Product WHERE productId = ? FOR UPDATE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                Product_24162022 p = new Product_24162022();
                p.setProductId(rs.getInt("productId"));
                p.setProductName(rs.getString("productName"));
                p.setStatus(rs.getInt("status"));
                p.setStock(rs.getInt("stock"));
                return p;
            }
        }
    }

    /** Tru ton kho an toan: chi tru khi con du. Tra ve 0 neu khong du so luong. */
    public int decreaseStock(Connection conn, int productId, int quantity) throws SQLException {
        String sql = "UPDATE Product SET stock = stock - ? WHERE productId = ? AND stock >= ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            return ps.executeUpdate();
        }
    }

    /** Chuyen gio thanh don: status 0 -> 1, luu thong tin nhan hang va tong tien. */
    public int markOrdered(Connection conn, String cartId, int userId, String receiverName,
                           String receiverPhone, String shippingAddress, String note,
                           String paymentMethod, double totalAmount) throws SQLException {
        String sql = "UPDATE Cart SET status = 1, buyDate = NOW(), receiverName = ?, receiverPhone = ?, " +
                "shippingAddress = ?, note = ?, paymentMethod = ?, totalAmount = ? " +
                "WHERE cartId = ? AND userId = ? AND status = 0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, receiverName);
            ps.setString(2, receiverPhone);
            ps.setString(3, shippingAddress);
            ps.setString(4, note);
            ps.setString(5, paymentMethod);
            ps.setDouble(6, totalAmount);
            ps.setString(7, cartId);
            ps.setInt(8, userId);
            return ps.executeUpdate();
        }
    }

    /** Don hang (status >= 1) cua dung User; null neu khong ton tai hoac cua nguoi khac. */
    public Cart_24162022 findOrderOfUser(String cartId, int userId) throws SQLException {
        String sql = "SELECT * FROM Cart WHERE cartId = ? AND userId = ? AND status >= 1";
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cartId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapCart(rs) : null;
            }
        }
    }

    /** Lich su don hang cua User (status >= 1), moi nhat truoc. */
    public List<Cart_24162022> findOrdersByUser(int userId) throws SQLException {
        String sql = "SELECT c.*, (SELECT COUNT(*) FROM CartItem ci WHERE ci.cartId = c.cartId) AS itemCount " +
                "FROM Cart c WHERE c.userId = ? AND c.status >= 1 ORDER BY c.buyDate DESC, c.cartId";
        List<Cart_24162022> list = new ArrayList<>();
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cart_24162022 c = mapCart(rs);
                    c.setItemCount(rs.getInt("itemCount"));
                    list.add(c);
                }
            }
        }
        return list;
    }

    private Cart_24162022 mapCart(ResultSet rs) throws SQLException {
        Cart_24162022 c = new Cart_24162022();
        c.setCartId(rs.getString("cartId"));
        c.setUserId(rs.getInt("userId"));
        c.setBuyDate(rs.getTimestamp("buyDate"));
        c.setStatus(rs.getInt("status"));
        c.setReceiverName(rs.getString("receiverName"));
        c.setReceiverPhone(rs.getString("receiverPhone"));
        c.setShippingAddress(rs.getString("shippingAddress"));
        c.setNote(rs.getString("note"));
        c.setPaymentMethod(rs.getString("paymentMethod"));
        c.setTotalAmount(rs.getDouble("totalAmount"));
        return c;
    }
}
