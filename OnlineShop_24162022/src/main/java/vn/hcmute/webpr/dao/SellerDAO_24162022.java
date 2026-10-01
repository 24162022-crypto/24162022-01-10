package vn.hcmute.webpr.dao;

import vn.hcmute.webpr.entity.Seller_24162022;
import vn.hcmute.webpr.util.DBConnection_24162022;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** DATA ACCESS LAYER (DAO) - doc bang Seller (gom san pham theo cua hang o Cau 3, form User o Cau 5). */
public class SellerDAO_24162022 {

    public List<Seller_24162022> findAll() throws SQLException {
        String sql = "SELECT * FROM Seller ORDER BY sellerId";
        List<Seller_24162022> list = new ArrayList<>();
        try (Connection conn = DBConnection_24162022.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Seller_24162022 s = new Seller_24162022();
                s.setSellerId(rs.getInt("sellerId"));
                s.setSellername(rs.getString("sellername"));
                s.setImages(rs.getString("images"));
                s.setStatus(rs.getInt("status"));
                list.add(s);
            }
        }
        return list;
    }
}
