package vn.hcmute.webpr.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.hcmute.webpr.entity.Category_24162022;
import vn.hcmute.webpr.service.CategoryService_24162022;

import java.io.IOException;

/**
 * Presentation Layer - Controller CRUD Category co phan trang (Cau 5).
 * GET  /admin/categories?page=n          -> danh sach (xem)
 * GET  /admin/categories?action=create   -> form them
 * GET  /admin/categories?action=edit&id= -> form sua
 * POST /admin/categories action=save     -> them / cap nhat
 * POST /admin/categories action=delete   -> xoa
 * (Da duoc AdminAuthFilter_24162022 bao ve - chi Admin truy cap duoc)
 */
@WebServlet("/admin/categories")
public class AdminCategoryServlet_24162022 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final CategoryService_24162022 categoryService = new CategoryService_24162022();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("create".equals(action)) {
                Category_24162022 c = new Category_24162022();
                c.setStatus(1);
                req.setAttribute("category", c);
                showForm(req, resp);
                return;
            }
            if ("edit".equals(action)) {
                req.setAttribute("category", categoryService.findById(parseInt(req.getParameter("id"), 0)));
                showForm(req, resp);
                return;
            }
            req.setAttribute("pageResult", categoryService.getPage(parseInt(req.getParameter("page"), 1)));
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
        }
        moveFlash(req);
        req.getRequestDispatcher("/WEB-INF/jsp/admin/category/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        String page = req.getParameter("page");
        HttpSession session = req.getSession();

        if ("delete".equals(action)) {
            try {
                categoryService.delete(parseInt(req.getParameter("id"), 0));
                session.setAttribute("flashSuccess", "Da xoa danh muc.");
            } catch (Exception e) {
                session.setAttribute("flashError", e.getMessage());
            }
            resp.sendRedirect(req.getContextPath() + "/admin/categories?page=" + parseInt(page, 1));
            return;
        }

        Category_24162022 c = new Category_24162022();
        c.setCategoryId(parseInt(req.getParameter("categoryId"), 0));
        c.setCategoryName(req.getParameter("categoryName"));
        c.setImages(req.getParameter("images"));
        c.setStatus(parseInt(req.getParameter("status"), 1));
        try {
            categoryService.save(c);
            session.setAttribute("flashSuccess",
                    c.getCategoryId() == 0 ? "Da them danh muc moi." : "Da cap nhat danh muc.");
            resp.sendRedirect(req.getContextPath() + "/admin/categories?page=" + parseInt(page, 1));
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("category", c);
            showForm(req, resp);
        }
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/admin/category/form.jsp").forward(req, resp);
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

    private int parseInt(String s, int defaultValue) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
