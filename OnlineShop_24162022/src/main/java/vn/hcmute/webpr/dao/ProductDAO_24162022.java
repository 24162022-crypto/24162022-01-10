package vn.hcmute.webpr.dao;

import vn.hcmute.webpr.entity.Product_24162022;
import vn.hcmute.webpr.util.DBConnection_24162022;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DATA ACCESS LAYER (DAO) - doc bang Product (JOIN Category, Seller)
 * phuc vu trang danh sach san pham theo cua hang (Cau 3) va trang chi tiet (Cau 4).
 */
public class ProductDAO_24162022 {

    private static final String SELECT_JOIN =
            "SELECT p.*, c.categoryName, s.sellername FROM Product p " +
            "LEFT JOIN Category c ON p.categoryId = c.categoryId " +
            "LEFT JOIN Seller s ON p.sellerId = s.sellerId ";

    /** Tat ca san pham dang hien thi (status = 1), sap xep theo ma cua hang de gom nhom. */
    public List<Product_24162022> findAllActiveOrderBySeller() throws SQLException {
        String sql = SELECT_JOIN + "WHERE p.status = 1 ORDER BY p.sellerId, p.productId";
        List<Product_24162022> list = new ArrayList<>();
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    /** San pham cua 1 cua hang (dung cho trang chu Seller). */
    public List<Product_24162022> findBySellerId(int sellerId) throws SQLException {
        String sql = SELECT_JOIN + "WHERE p.sellerId = ? ORDER BY p.productId";
        List<Product_24162022> list = new ArrayList<>();
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public Product_24162022 findById(int productId) throws SQLException {
        String sql = SELECT_JOIN + "WHERE p.productId = ?";
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        }
    }

    /** Dem so san pham thuoc 1 danh muc (kiem tra truoc khi xoa Category o Cau 5). */
    public int countByCategoryId(int categoryId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Product WHERE categoryId = ?";
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    private Product_24162022 mapRow(ResultSet rs) throws SQLException {
        Product_24162022 p = new Product_24162022();
        p.setProductId(rs.getInt("productId"));
        p.setProductName(rs.getString("productName"));
        p.setProductCode(rs.getLong("productCode"));
        p.setCategoryId(rs.getInt("categoryId"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getDouble("price"));
        p.setAmount(rs.getInt("amount"));
        p.setStock(rs.getInt("stock"));
        p.setImages(rs.getString("images"));
        p.setWishlist(rs.getInt("wishlist"));
        p.setStatus(rs.getInt("status"));
        p.setCreateDate(rs.getDate("createDate"));
        p.setSellerId(rs.getInt("sellerId"));
        p.setCategoryName(rs.getString("categoryName"));
        p.setSellername(rs.getString("sellername"));
        return p;
    }
}
