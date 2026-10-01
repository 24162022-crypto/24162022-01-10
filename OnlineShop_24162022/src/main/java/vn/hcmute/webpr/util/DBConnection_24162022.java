package vn.hcmute.webpr.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Lop tien ich ket noi Database (JDBC - MySQL) - Data Access Layer
 */
public class DBConnection_24162022 {

    // ===== TODO: CHINH 4 THONG SO NAY DUNG VOI MYSQL TREN MAY BAN =====
    // Neu PASSWORD sai -> moi thao tac DB (dang ky, dang nhap ke ca tai khoan
    // admin/123456 co san trong db_24162022.sql) se nem SQLException.
    // Nho: chay file src/main/resources/db_24162022.sql tren MySQL truoc khi test.
    private static final String URL =
            "jdbc:mysql://localhost:3306/OnlineShop_24162022?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Ho_Chi_Minh&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "123456"; // <-- doi thanh mat khau MySQL that cua ban
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void close(java.lang.AutoCloseable... closeables) {
        for (java.lang.AutoCloseable c : closeables) {
            if (c != null) {
                try { c.close(); } catch (Exception ignored) {}
            }
        }
    }
}
