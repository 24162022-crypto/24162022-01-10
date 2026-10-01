package vn.hcmute.webpr.dao;

import vn.hcmute.webpr.entity.CartItem_24162022;
import vn.hcmute.webpr.util.DBConnection_24162022;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * DATA ACCESS LAYER (DAO) - thao tac gio hang (bang Cart status = 0 va CartItem).
 * Tat ca cau lenh dung PreparedStatement. Moi thao tac sua/xoa deu rang buoc theo cartId
 * (va userId khi can) de User khong sua duoc gio cua nguoi khac.
 */
public class CartDAO_24162022 {

    private static final String SELECT_ITEM =
            "SELECT ci.cartItemId, ci.quantity, ci.unitPrice, ci.productId, ci.cartId, " +
            "p.productName, p.images, p.stock, p.status AS productStatus " +
            "FROM CartItem ci JOIN Product p ON ci.productId = p.productId ";

    /** Tim ma gio dang mo (status = 0) cua User; tra ve null neu chua co. */
    public String findOpenCartId(int userId) throws SQLException {
        try (Connection conn = DBConnection_24162022.getConnection()) {
            return findOpenCartId(conn, userId);
        }
    }

    public String findOpenCartId(Connection conn, int userId) throws SQLException {
        String sql = "SELECT cartId FROM Cart WHERE userId = ? AND status = 0 ORDER BY cartId LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        }
    }

    /** Tao gio moi (status = 0) cho User, tra ve cartId (UUID). */
    public String createCart(int userId) throws SQLException {
        String cartId = UUID.randomUUID().toString();
        String sql = "INSERT INTO Cart(cartId, userId, buyDate, status) VALUES (?, ?, NULL, 0)";
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cartId);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
        return cartId;
    }

    /** Cac dong cua 1 gio/don (kem thong tin san pham), sap xep theo ma san pham de thu tu on dinh. */
    public List<CartItem_24162022> findItemsByCartId(String cartId) throws SQLException {
        try (Connection conn = DBConnection_24162022.getConnection()) {
            return findItemsByCartId(conn, cartId);
        }
    }

    public List<CartItem_24162022> findItemsByCartId(Connection conn, String cartId) throws SQLException {
        String sql = SELECT_ITEM + "WHERE ci.cartId = ? ORDER BY ci.productId";
        List<CartItem_24162022> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cartId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    /** Tim dong theo (gio, san pham) - dung de cong don khi them trung san pham. */
    public CartItem_24162022 findItemByCartAndProduct(String cartId, int productId) throws SQLException {
        String sql = SELECT_ITEM + "WHERE ci.cartId = ? AND ci.productId = ?";
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cartId);
            ps.setInt(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    /**
     * Tim dong gio theo cartItemId NHUNG chi khi dong do thuoc gio dang mo cua dung User
     * (chong sua/xoa gio cua nguoi khac). Tra ve null neu khong hop le.
     */
    public CartItem_24162022 findItemOfUser(int userId, String cartItemId) throws SQLException {
        String sql = SELECT_ITEM + "JOIN Cart c ON ci.cartId = c.cartId " +
                "WHERE ci.cartItemId = ? AND c.userId = ? AND c.status = 0";
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cartItemId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public void insertItem(String cartId, int productId, int quantity, double unitPrice) throws SQLException {
        String sql = "INSERT INTO CartItem(cartItemId, quantity, unitPrice, productId, cartId) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, UUID.randomUUID().toString());
            ps.setInt(2, quantity);
            ps.setDouble(3, unitPrice);
            ps.setInt(4, productId);
            ps.setString(5, cartId);
            ps.executeUpdate();
        }
    }

    /** Cap nhat so luong + don gia (khi cong don them san pham). */
    public int updateQuantityAndPrice(String cartItemId, String cartId, int quantity, double unitPrice) throws SQLException {
        String sql = "UPDATE CartItem SET quantity = ?, unitPrice = ? WHERE cartItemId = ? AND cartId = ?";
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setDouble(2, unitPrice);
            ps.setString(3, cartItemId);
            ps.setString(4, cartId);
            return ps.executeUpdate();
        }
    }

    /** Cap nhat so luong 1 dong (giu nguyen don gia). */
    public int updateQuantity(String cartItemId, String cartId, int quantity) throws SQLException {
        String sql = "UPDATE CartItem SET quantity = ? WHERE cartItemId = ? AND cartId = ?";
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setString(2, cartItemId);
            ps.setString(3, cartId);
            return ps.executeUpdate();
        }
    }

    public int deleteItem(String cartItemId, String cartId) throws SQLException {
        String sql = "DELETE FROM CartItem WHERE cartItemId = ? AND cartId = ?";
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cartItemId);
            ps.setString(2, cartId);
            return ps.executeUpdate();
        }
    }

    /** Xoa toan bo dong cua gio (giu lai ban ghi Cart). */
    public int deleteAllItems(String cartId) throws SQLException {
        String sql = "DELETE FROM CartItem WHERE cartId = ?";
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cartId);
            return ps.executeUpdate();
        }
    }

    /** So mat hang (so dong) trong gio dang mo cua User - hien tren header. */
    public int countLines(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM CartItem ci JOIN Cart c ON ci.cartId = c.cartId " +
                "WHERE c.userId = ? AND c.status = 0";
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    private CartItem_24162022 mapRow(ResultSet rs) throws SQLException {
        CartItem_24162022 it = new CartItem_24162022();
        it.setCartItemId(rs.getString("cartItemId"));
        it.setQuantity(rs.getInt("quantity"));
        it.setUnitPrice(rs.getDouble("unitPrice"));
        it.setProductId(rs.getInt("productId"));
        it.setCartId(rs.getString("cartId"));
        it.setProductName(rs.getString("productName"));
        it.setImages(rs.getString("images"));
        it.setStock(rs.getInt("stock"));
        it.setProductStatus(rs.getInt("productStatus"));
        return it;
    }
}
