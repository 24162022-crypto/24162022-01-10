package vn.hcmute.webpr.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.hcmute.webpr.service.OrderService_24162022;

import java.io.IOException;

/**
 * Presentation Layer - Trang xac nhan dat hang thanh cong: GET /order-success?id=<cartId>.
 * Chi chu don hang moi xem duoc (OrderService kiem tra userId trong Session).
 */
@WebServlet("/order-success")
public class OrderSuccessServlet_24162022 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final OrderService_24162022 orderService = new OrderService_24162022();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int userId = (Integer) req.getSession().getAttribute("userId");
        try {
            req.setAttribute("order", orderService.getOrderOfUser(userId, req.getParameter("id")));
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
        }
        req.setAttribute("pageTitle", "Dat hang thanh cong");
        req.getRequestDispatcher("/WEB-INF/jsp/order/success.jsp").forward(req, resp);
    }
}
