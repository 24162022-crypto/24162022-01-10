package vn.hcmute.webpr.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.hcmute.webpr.entity.User_24162022;
import vn.hcmute.webpr.filter.UserAuthFilter_24162022;
import vn.hcmute.webpr.service.CartService_24162022;
import vn.hcmute.webpr.service.UserService_24162022;

import java.io.IOException;

/**
 * Presentation Layer - Controller cho chuc nang Dang nhap (su dung Session).
 * Dang nhap thanh cong -> vao trang chu tuong ung vai tro (User / Seller / Admin).
 * That bai -> quay lai trang dang nhap kem thong bao loi (Cau 2).
 */
@WebServlet("/login")
public class LoginServlet_24162022 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UserService_24162022 userService = new UserService_24162022();
    private final CartService_24162022 cartService = new CartService_24162022();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if ("1".equals(req.getParameter("activated"))) {
            req.setAttribute("successMessage", "Kich hoat tai khoan thanh cong. Vui long dang nhap.");
        }
        // Tham so next (vi du tu nut "Dang nhap de mua" o trang san pham): luu URL de quay lai sau khi dang nhap
        String next = req.getParameter("next");
        if (isSafeInternalPath(next)) {
            req.getSession().setAttribute(UserAuthFilter_24162022.REDIRECT_AFTER_LOGIN, next);
        }
        req.setAttribute("pageTitle", "Dang nhap");
        req.getRequestDispatcher("/WEB-INF/jsp/auth/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            User_24162022 user = userService.login(username, password);

            HttpSession session = req.getSession();
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("fullname", user.getFullname());
            session.setAttribute("role", user.getRoleName());
            if (user.getSellerId() != null) {
                session.setAttribute("sellerId", user.getSellerId());
            }

            String role = user.getRoleName();

            // URL dinh quay lai (do UserAuthFilter / tham so next luu); chi dung cho role User
            String returnUrl = (String) session.getAttribute(UserAuthFilter_24162022.REDIRECT_AFTER_LOGIN);
            session.removeAttribute(UserAuthFilter_24162022.REDIRECT_AFTER_LOGIN);

            // So mat hang trong gio hien tren header (chi role User)
            session.removeAttribute("cartCount");
            if ("User".equalsIgnoreCase(role)) {
                try {
                    session.setAttribute("cartCount", cartService.countItems(user.getUserId()));
                } catch (Exception ignored) {
                    session.setAttribute("cartCount", 0);
                }
            }

            if ("User".equalsIgnoreCase(role) && isSafeInternalPath(returnUrl)) {
                resp.sendRedirect(req.getContextPath() + returnUrl);
            } else if ("Admin".equalsIgnoreCase(role)) {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
            } else if ("Seller".equalsIgnoreCase(role)) {
                resp.sendRedirect(req.getContextPath() + "/seller/home");
            } else {
                resp.sendRedirect(req.getContextPath() + "/home");
            }
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("username", username);
            req.setAttribute("pageTitle", "Dang nhap");
            req.getRequestDispatcher("/WEB-INF/jsp/auth/login.jsp").forward(req, resp);
        }
    }

    /** Chi chap nhan duong dan noi bo dang "/xxx" (chong open redirect, chong xuong dong). */
    private boolean isSafeInternalPath(String path) {
        return path != null
                && path.length() > 1
                && path.startsWith("/")
                && !path.startsWith("//")
                && !path.startsWith("/\\")
                && path.indexOf('\r') < 0
                && path.indexOf('\n') < 0;
    }
}
