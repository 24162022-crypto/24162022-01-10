package vn.hcmute.webpr.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Trang demo khu vuc /admin/* de kiem tra Decorator "admin" cua Sitemesh
 * hoat dong dung (layout rieng cho Admin). Chuc nang quan tri that (CRUD)
 * se duoc trien khai o Cau 5, kem AdminAuthFilter kiem tra quyen.
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet_24162022 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/admin/dashboard.jsp").forward(req, resp);
    }
}
