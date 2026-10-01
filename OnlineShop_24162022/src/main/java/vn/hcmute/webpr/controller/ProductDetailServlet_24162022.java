package vn.hcmute.webpr.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.hcmute.webpr.service.ProductService_24162022;

import java.io.IOException;

/**
 * Presentation Layer - Controller trang chi tiet 1 san pham (Cau 4),
 * mo khi bam vao ten san pham o trang danh sach (Cau 3). URL: /product?id=...
 */
@WebServlet("/product")
public class ProductDetailServlet_24162022 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ProductService_24162022 productService = new ProductService_24162022();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("product", productService.getProductDetail(id));
        } catch (NumberFormatException e) {
            req.setAttribute("errorMessage", "Ma san pham khong hop le.");
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/jsp/product/detail.jsp").forward(req, resp);
    }
}
