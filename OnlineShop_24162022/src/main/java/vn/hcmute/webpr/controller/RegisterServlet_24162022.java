package vn.hcmute.webpr.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.hcmute.webpr.service.UserService_24162022;

import java.io.IOException;

/**
 * Presentation Layer - Controller cho chuc nang Dang ky tai khoan,
 * kich hoat bang ma OTP gui qua email (Cau 2).
 */
@WebServlet("/register")
public class RegisterServlet_24162022 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UserService_24162022 userService = new UserService_24162022();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("pageTitle", "Dang ky tai khoan");
        req.getRequestDispatcher("/WEB-INF/jsp/auth/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String fullname = req.getParameter("fullname");
        String phone = req.getParameter("phone");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        // Giu lai du lieu da nhap (tru mat khau) de hien thi lai neu co loi
        req.setAttribute("username", username);
        req.setAttribute("email", email);
        req.setAttribute("fullname", fullname);
        req.setAttribute("phone", phone);
        req.setAttribute("pageTitle", "Dang ky tai khoan");

        if (password == null || password.isEmpty() || !password.equals(confirmPassword)) {
            req.setAttribute("errorMessage", "Mat khau xac nhan khong khop.");
            req.getRequestDispatcher("/WEB-INF/jsp/auth/register.jsp").forward(req, resp);
            return;
        }

        try {
            userService.register(username, email, fullname, phone, password);

            HttpSession session = req.getSession();
            session.setAttribute("pendingUsername", username);

            resp.sendRedirect(req.getContextPath() + "/verify-otp");
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/auth/register.jsp").forward(req, resp);
        }
    }
}
