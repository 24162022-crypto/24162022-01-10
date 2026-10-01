package vn.hcmute.webpr.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.hcmute.webpr.service.ProductService_24162022;

import java.io.IOException;

/**
 * Trang chu cho tai khoan vai tro "Seller" sau khi dang nhap thanh cong (Cau 2):
 * hien thi cac san pham cua cua hang gan voi tai khoan (Users.sellerid).
 */
@WebServlet("/seller/home")
public class SellerHomeServlet_24162022 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ProductService_24162022 productService = new ProductService_24162022();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Integer sellerId = (Integer) req.getSession().getAttribute("sellerId");
        if (sellerId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        try {
            req.setAttribute("products", productService.getProductsBySeller(sellerId));
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/jsp/seller/home.jsp").forward(req, resp);
    }
}
