package vn.hcmute.webpr.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.hcmute.webpr.entity.User_24162022;
import vn.hcmute.webpr.service.UserService_24162022;

import java.io.IOException;

/**
 * Presentation Layer - Controller CRUD User co phan trang (Cau 5).
 * GET  /admin/users?page=n          -> danh sach (xem)
 * GET  /admin/users?action=create   -> form them
 * GET  /admin/users?action=edit&id= -> form sua
 * POST /admin/users action=save     -> them / cap nhat
 * POST /admin/users action=delete   -> xoa
 * (Da duoc AdminAuthFilter_24162022 bao ve - chi Admin truy cap duoc)
 */
@WebServlet("/admin/users")
public class AdminUserServlet_24162022 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UserService_24162022 userService = new UserService_24162022();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("create".equals(action)) {
                User_24162022 u = new User_24162022();
                u.setStatus(1);
                req.setAttribute("user", u);
                showForm(req, resp);
                return;
            }
            if ("edit".equals(action)) {
                req.setAttribute("user", userService.findById(parseInt(req.getParameter("id"), 0)));
                showForm(req, resp);
                return;
            }
            req.setAttribute("pageResult", userService.getPage(parseInt(req.getParameter("page"), 1)));
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
        }
        moveFlash(req);
        req.getRequestDispatcher("/WEB-INF/jsp/admin/user/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        String page = req.getParameter("page");
        HttpSession session = req.getSession();

        if ("delete".equals(action)) {
            try {
                int currentUserId = (Integer) session.getAttribute("userId");
                userService.delete(parseInt(req.getParameter("id"), 0), currentUserId);
                session.setAttribute("flashSuccess", "Da xoa nguoi dung.");
            } catch (Exception e) {
                session.setAttribute("flashError", e.getMessage());
            }
            resp.sendRedirect(req.getContextPath() + "/admin/users?page=" + parseInt(page, 1));
            return;
        }

        User_24162022 u = new User_24162022();
        u.setUserId(parseInt(req.getParameter("userId"), 0));
        u.setUsername(trim(req.getParameter("username")));
        u.setEmail(trim(req.getParameter("email")));
        u.setFullname(trim(req.getParameter("fullname")));
        u.setPassword(req.getParameter("password"));
        u.setPhone(trim(req.getParameter("phone")));
        u.setImages(trim(req.getParameter("images")));
        u.setStatus(parseInt(req.getParameter("status"), 1));
        u.setRoleId(parseInt(req.getParameter("roleId"), 0));
        int sellerId = parseInt(req.getParameter("sellerId"), 0);
        u.setSellerId(sellerId == 0 ? null : sellerId);
        try {
            userService.save(u);
            session.setAttribute("flashSuccess",
                    u.getUserId() == 0 ? "Da them nguoi dung moi." : "Da cap nhat nguoi dung.");
            resp.sendRedirect(req.getContextPath() + "/admin/users?page=" + parseInt(page, 1));
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("user", u);
            showForm(req, resp);
        }
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("roles", userService.getAllRoles());
            req.setAttribute("sellers", userService.getAllSellers());
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/jsp/admin/user/form.jsp").forward(req, resp);
    }

    /** Chuyen thong bao (luu tam trong Session sau redirect) sang request de hien thi 1 lan. */
    private void moveFlash(HttpServletRequest req) {
        HttpSession session = req.getSession();
        for (String key : new String[]{"flashSuccess", "flashError"}) {
            Object msg = session.getAttribute(key);
            if (msg != null) {
                req.setAttribute(key, msg);
                session.removeAttribute(key);
            }
        }
    }

    private String trim(String s) {
        return s == null ? null : s.trim();
    }

    private int parseInt(String s, int defaultValue) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
