package vn.hcmute.webpr.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Bao ve chuc nang mua hang: /cart, /checkout, /order-success, /orders (chi role = "User").
 * - Chua dang nhap -> luu URL dinh quay lai vao Session (redirectAfterLogin) roi chuyen sang /login.
 * - Da dang nhap nhung khong phai User (Admin/Seller) -> hien trang thong bao khong co quyen.
 * Cung phong cach voi AdminAuthFilter_24162022.
 * Luu y: url-pattern phai liet ke ro tung duong dan (khong dung "/order*" vi Servlet khong ho tro).
 */
@WebFilter(urlPatterns = {"/cart", "/cart/*", "/checkout", "/order-success", "/orders", "/orders/*"})
public class UserAuthFilter_24162022 implements Filter {

    /** Ten attribute Session chua URL (tinh tu context path) can quay lai sau khi dang nhap. */
    public static final String REDIRECT_AFTER_LOGIN = "redirectAfterLogin";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false);
        Object userId = (session != null) ? session.getAttribute("userId") : null;
        String role = (session != null) ? (String) session.getAttribute("role") : null;

        // 1. Chua dang nhap
        if (userId == null) {
            String target;
            if ("GET".equalsIgnoreCase(request.getMethod())) {
                String path = request.getRequestURI().substring(request.getContextPath().length());
                String query = request.getQueryString();
                target = (query == null) ? path : path + "?" + query;
            } else {
                // POST chua dang nhap: khong the lap lai POST, chi quay ve trang gio hang
                target = "/cart";
            }
            request.getSession().setAttribute(REDIRECT_AFTER_LOGIN, target);
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 2. Da dang nhap nhung khong phai User
        if (!"User".equalsIgnoreCase(role)) {
            request.setAttribute("pageTitle", "Khong co quyen");
            request.getRequestDispatcher("/WEB-INF/jsp/common/user-only.jsp").forward(request, response);
            return;
        }

        chain.doFilter(req, res);
    }
}
