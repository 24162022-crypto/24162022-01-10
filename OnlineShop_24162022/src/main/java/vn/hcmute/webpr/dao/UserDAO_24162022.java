package vn.hcmute.webpr.dao;

import vn.hcmute.webpr.entity.User_24162022;
import vn.hcmute.webpr.util.DBConnection_24162022;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * DATA ACCESS LAYER (DAO) - thao tac truc tiep voi bang Users / UserRoles
 * bang JDBC (PreparedStatement), phuc vu chuc nang Dang ky (OTP) / Dang nhap o Cau 2
 * va CRUD + phan trang User o Cau 5.
 */
public class UserDAO_24162022 {

    public boolean existsByUsername(String username) throws SQLException {
        String sql = "SELECT userId FROM Users WHERE username = ?";
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT userId FROM Users WHERE email = ?";
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /** Lay roleId theo ten vai tro (VD: "User", "Seller", "Admin"), tra -1 neu khong tim thay. */
    public int getRoleIdByName(String roleName) throws SQLException {
        String sql = "SELECT roleId FROM UserRoles WHERE roleName = ?";
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roleName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("roleId");
                }
                return -1;
            }
        }
    }

    /** Them tai khoan moi, mac dinh status = 0 (cho kich hoat bang OTP). */
    public int insertUser(User_24162022 user) throws SQLException {
        String sql = "INSERT INTO Users(username, email, fullname, password, phone, status, code, roleId) " +
                "VALUES (?, ?, ?, ?, ?, 0, ?, ?)";
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getFullname());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getPhone());
            ps.setString(6, user.getCode());
            ps.setInt(7, user.getRoleId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    /** Cap nhat lai ma OTP moi (dung khi nguoi dung bam "Gui lai ma"). */
    public boolean updateOtpCode(String username, String newCode) throws SQLException {
        String sql = "UPDATE Users SET code = ? WHERE username = ?";
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newCode);
            ps.setString(2, username);
            return ps.executeUpdate() > 0;
        }
    }

    /** Kich hoat tai khoan neu ma OTP khop va tai khoan dang o trang thai chua kich hoat. */
    public boolean activateByUsernameAndCode(String username, String code) throws SQLException {
        String sql = "UPDATE Users SET status = 1, code = NULL WHERE username = ? AND code = ? AND status = 0";
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, code);
            return ps.executeUpdate() > 0;
        }
    }

    /** Tim tai khoan theo username, kem roleName (JOIN UserRoles) - dung cho Dang nhap. */
    public User_24162022 findByUsername(String username) throws SQLException {
        String sql = "SELECT u.*, r.roleName FROM Users u " +
                "JOIN UserRoles r ON u.roleId = r.roleId WHERE u.username = ?";
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        }
    }

    // ===================== CRUD + PHAN TRANG (Cau 5) =====================

    public List<User_24162022> findPage(int offset, int limit) throws SQLException {
        String sql = "SELECT u.*, r.roleName FROM Users u " +
                "LEFT JOIN UserRoles r ON u.roleId = r.roleId ORDER BY u.userId LIMIT ? OFFSET ?";
        List<User_24162022> list = new ArrayList<>();
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
        String sql = "SELECT COUNT(*) FROM Users";
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    public User_24162022 findById(int userId) throws SQLException {
        String sql = "SELECT u.*, r.roleName FROM Users u " +
                "LEFT JOIN UserRoles r ON u.roleId = r.roleId WHERE u.userId = ?";
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        }
    }

    /** Kiem tra trung username/email voi tai khoan KHAC (dung khi cap nhat). */
    public boolean existsByUsernameExcept(String username, int userId) throws SQLException {
        return existsExcept("username", username, userId);
    }

    public boolean existsByEmailExcept(String email, int userId) throws SQLException {
        return existsExcept("email", email, userId);
    }

    private boolean existsExcept(String column, String value, int userId) throws SQLException {
        String sql = "SELECT userId FROM Users WHERE " + column + " = ? AND userId <> ?";
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, value);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /** Admin them tai khoan moi (chon san status/role/seller, khong qua OTP). */
    public void insertByAdmin(User_24162022 u) throws SQLException {
        String sql = "INSERT INTO Users(username, email, fullname, password, images, phone, status, roleId, sellerid) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getFullname());
            ps.setString(4, u.getPassword());
            ps.setString(5, u.getImages());
            ps.setString(6, u.getPhone());
            ps.setInt(7, u.getStatus());
            ps.setInt(8, u.getRoleId());
            setNullableInt(ps, 9, u.getSellerId());
            ps.executeUpdate();
        }
    }

    public boolean update(User_24162022 u) throws SQLException {
        String sql = "UPDATE Users SET username = ?, email = ?, fullname = ?, password = ?, images = ?, " +
                "phone = ?, status = ?, roleId = ?, sellerid = ? WHERE userId = ?";
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getFullname());
            ps.setString(4, u.getPassword());
            ps.setString(5, u.getImages());
            ps.setString(6, u.getPhone());
            ps.setInt(7, u.getStatus());
            ps.setInt(8, u.getRoleId());
            setNullableInt(ps, 9, u.getSellerId());
            ps.setInt(10, u.getUserId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int userId) throws SQLException {
        String sql = "DELETE FROM Users WHERE userId = ?";
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        }
    }

    private void setNullableInt(PreparedStatement ps, int index, Integer value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.INTEGER);
        } else {
            ps.setInt(index, value);
        }
    }

    private User_24162022 mapRow(ResultSet rs) throws SQLException {
        User_24162022 u = new User_24162022();
        u.setUserId(rs.getInt("userId"));
        u.setUsername(rs.getString("username"));
        u.setEmail(rs.getString("email"));
        u.setFullname(rs.getString("fullname"));
        u.setPassword(rs.getString("password"));
        u.setImages(rs.getString("images"));
        u.setPhone(rs.getString("phone"));
        u.setStatus(rs.getInt("status"));
        u.setCode(rs.getString("code"));
        u.setRoleId(rs.getInt("roleId"));
        int sellerId = rs.getInt("sellerid");
        u.setSellerId(rs.wasNull() ? null : sellerId);
        u.setRoleName(rs.getString("roleName"));
        return u;
    }
}
