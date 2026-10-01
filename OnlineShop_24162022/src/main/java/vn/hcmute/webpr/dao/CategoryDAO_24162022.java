package vn.hcmute.webpr.dao;

import vn.hcmute.webpr.entity.Category_24162022;
import vn.hcmute.webpr.util.DBConnection_24162022;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** DATA ACCESS LAYER (DAO) - CRUD + phan trang cho bang Category (Cau 5). */
public class CategoryDAO_24162022 {

    public List<Category_24162022> findPage(int offset, int limit) throws SQLException {
        String sql = "SELECT * FROM Category ORDER BY categoryId LIMIT ? OFFSET ?";
        List<Category_24162022> list = new ArrayList<>();
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Category";
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    public Category_24162022 findById(int categoryId) throws SQLException {
        String sql = "SELECT * FROM Category WHERE categoryId = ?";
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        }
    }

    public void insert(Category_24162022 c) throws SQLException {
        String sql = "INSERT INTO Category(categoryName, images, status) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCategoryName());
            ps.setString(2, c.getImages());
            ps.setInt(3, c.getStatus());
            ps.executeUpdate();
        }
    }

    public boolean update(Category_24162022 c) throws SQLException {
        String sql = "UPDATE Category SET categoryName = ?, images = ?, status = ? WHERE categoryId = ?";
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCategoryName());
            ps.setString(2, c.getImages());
            ps.setInt(3, c.getStatus());
            ps.setInt(4, c.getCategoryId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int categoryId) throws SQLException {
        String sql = "DELETE FROM Category WHERE categoryId = ?";
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            return ps.executeUpdate() > 0;
        }
    }

    private Category_24162022 mapRow(ResultSet rs) throws SQLException {
        Category_24162022 c = new Category_24162022();
        c.setCategoryId(rs.getInt("categoryId"));
        c.setCategoryName(rs.getString("categoryName"));
        c.setImages(rs.getString("images"));
        c.setStatus(rs.getInt("status"));
        return c;
    }
}
