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
 * Presentation Layer - Controller xac thuc ma OTP (gui tu RegisterServlet)
 * de kich hoat tai khoan vua dang ky (Cau 2).
 */
@WebServlet("/verify-otp")
public class VerifyOtpServlet_24162022 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UserService_24162022 userService = new UserService_24162022();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pendingUsername = getPendingUsername(req);
        if (pendingUsername == null) {
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }
        req.setAttribute("pageTitle", "Xac thuc OTP");
        req.setAttribute("username", pendingUsername);
        req.getRequestDispatcher("/WEB-INF/jsp/auth/verify-otp.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pendingUsername = getPendingUsername(req);
        if (pendingUsername == null) {
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }

        String action = req.getParameter("action");
        if ("resend".equals(action)) {
            try {
                userService.resendOtp(pendingUsername);
                req.setAttribute("successMessage", "Da gui lai ma OTP moi qua email.");
            } catch (Exception e) {
                req.setAttribute("errorMessage", e.getMessage());
            }
            req.setAttribute("pageTitle", "Xac thuc OTP");
            req.setAttribute("username", pendingUsername);
            req.getRequestDispatcher("/WEB-INF/jsp/auth/verify-otp.jsp").forward(req, resp);
            return;
        }

        String code = req.getParameter("code");
        try {
            userService.verifyOtp(pendingUsername, code);
            req.getSession().removeAttribute("pendingUsername");
            resp.sendRedirect(req.getContextPath() + "/login?activated=1");
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("pageTitle", "Xac thuc OTP");
            req.setAttribute("username", pendingUsername);
            req.getRequestDispatcher("/WEB-INF/jsp/auth/verify-otp.jsp").forward(req, resp);
        }
    }

    private String getPendingUsername(HttpServletRequest req) {
        HttpSession session = req.getSession();
        return (String) session.getAttribute("pendingUsername");
    }
}
