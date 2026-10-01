package vn.hcmute.webpr.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.hcmute.webpr.service.ProductService_24162022;

import java.io.IOException;

/**
 * Presentation Layer - Controller hien thi tat ca san pham gom theo tung cua hang
 * (theo ma cua hang SellerID) - Cau 3.
 */
@WebServlet("/products")
public class ProductListServlet_24162022 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ProductService_24162022 productService = new ProductService_24162022();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("sellers", productService.getProductsGroupedBySeller());
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Khong the tai danh sach san pham: " + e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/jsp/product/list.jsp").forward(req, resp);
    }
}
