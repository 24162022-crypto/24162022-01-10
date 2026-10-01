package vn.hcmute.webpr.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.hcmute.webpr.service.OrderService_24162022;

import java.io.IOException;

/**
 * Presentation Layer - Lich su don hang cua User (tuy chon).
 * GET /orders          -> danh sach don (status >= 1, moi nhat truoc)
 * GET /orders?id=<id>  -> chi tiet 1 don (chi chu don xem duoc)
 */
@WebServlet("/orders")
public class OrderHistoryServlet_24162022 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final OrderService_24162022 orderService = new OrderService_24162022();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int userId = (Integer) req.getSession().getAttribute("userId");
        String id = req.getParameter("id");
        try {
            if (id != null && !id.trim().isEmpty()) {
                req.setAttribute("order", orderService.getOrderOfUser(userId, id));
                req.setAttribute("pageTitle", "Chi tiet don hang");
                req.getRequestDispatcher("/WEB-INF/jsp/order/detail.jsp").forward(req, resp);
                return;
            }
            req.setAttribute("orders", orderService.getOrdersOfUser(userId));
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
            if (id != null && !id.trim().isEmpty()) {
                req.setAttribute("pageTitle", "Chi tiet don hang");
                req.getRequestDispatcher("/WEB-INF/jsp/order/detail.jsp").forward(req, resp);
                return;
            }
        }
        req.setAttribute("pageTitle", "Don hang cua toi");
        req.getRequestDispatcher("/WEB-INF/jsp/order/list.jsp").forward(req, resp);
    }
}
