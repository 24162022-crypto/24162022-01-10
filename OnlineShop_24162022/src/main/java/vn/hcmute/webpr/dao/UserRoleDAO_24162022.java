package vn.hcmute.webpr.dao;

import vn.hcmute.webpr.entity.UserRole_24162022;
import vn.hcmute.webpr.util.DBConnection_24162022;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** DATA ACCESS LAYER (DAO) - doc bang UserRoles (dung cho form them/sua User o Cau 5). */
public class UserRoleDAO_24162022 {

    public List<UserRole_24162022> findAll() throws SQLException {
        String sql = "SELECT * FROM UserRoles ORDER BY roleId";
        List<UserRole_24162022> list = new ArrayList<>();
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                UserRole_24162022 r = new UserRole_24162022();
                r.setRoleId(rs.getInt("roleId"));
                r.setRoleName(rs.getString("roleName"));
                list.add(r);
            }
        }
        return list;
    }
}
