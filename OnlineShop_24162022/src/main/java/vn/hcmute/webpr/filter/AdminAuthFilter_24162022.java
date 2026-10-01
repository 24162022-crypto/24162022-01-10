package vn.hcmute.webpr.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Bao ve khu vuc /admin/* : chi cho phep truy cap khi Session da dang nhap
 * va co role = "Admin". Neu chua dang nhap hoac khong phai Admin -> quay ve
 * trang dang nhap (dung yeu cau Cau 1: "Trang quan tri (admin moi co chuc nang nay)").
 */
@WebFilter("/admin/*")
public class AdminAuthFilter_24162022 implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false);
        String role = (session != null) ? (String) session.getAttribute("role") : null;

        if (session == null || !"Admin".equalsIgnoreCase(role)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        chain.doFilter(req, res);
    }
}
