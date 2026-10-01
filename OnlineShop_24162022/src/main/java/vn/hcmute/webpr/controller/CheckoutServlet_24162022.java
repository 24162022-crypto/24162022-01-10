package vn.hcmute.webpr.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.hcmute.webpr.entity.Cart_24162022;
import vn.hcmute.webpr.service.CartService_24162022;
import vn.hcmute.webpr.service.OrderService_24162022;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Presentation Layer - Controller Thanh toan COD (role User, da duoc UserAuthFilter_24162022 bao ve).
 * GET  /checkout -> tom tat gio + form thong tin nhan hang (gio trong -> ve /cart)
 * POST /checkout -> validate + dat hang (transaction) -> redirect /order-success?id=... (PRG)
 */
@WebServlet("/checkout")
public class CheckoutServlet_24162022 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final CartService_24162022 cartService = new CartService_24162022();
    private final OrderService_24162022 orderService = new OrderService_24162022();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        int userId = (Integer) session.getAttribute("userId");
        try {
            Cart_24162022 cart = cartService.getCart(userId);
            if (cart.getItems().isEmpty()) {
                session.setAttribute(CartServlet_24162022.FLASH_ERROR, "Gio hang dang trong, hay chon san pham truoc khi thanh toan.");
                resp.sendRedirect(req.getContextPath() + "/cart");
                return;
            }
            req.setAttribute("cart", cart);
            // gia tri mac dinh cua form
            req.setAttribute("receiverName", session.getAttribute("fullname"));
            req.setAttribute("receiverPhone", orderService.getUserPhone(userId));
            req.setAttribute("paymentMethod", OrderService_24162022.PAYMENT_COD);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Khong the tai trang thanh toan: " + e.getMessage());
        }
        showForm(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        int userId = (Integer) session.getAttribute("userId");

        String cartId = req.getParameter("cartId");
        String receiverName = req.getParameter("receiverName");
        String receiverPhone = req.getParameter("receiverPhone");
        String shippingAddress = req.getParameter("shippingAddress");
        String note = req.getParameter("note");
        String paymentMethod = req.getParameter("paymentMethod");

        try {
            String orderId = orderService.placeCodOrder(userId, cartId, receiverName, receiverPhone,
                    shippingAddress, note, paymentMethod);
            session.setAttribute("cartCount", 0);
            resp.sendRedirect(req.getContextPath() + "/order-success?id="
                    + URLEncoder.encode(orderId, StandardCharsets.UTF_8));
        } catch (Exception e) {
            // Loi: quay lai form, giu du lieu da nhap, hien thong bao
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("receiverName", receiverName);
            req.setAttribute("receiverPhone", receiverPhone);
            req.setAttribute("shippingAddress", shippingAddress);
            req.setAttribute("note", note);
            req.setAttribute("paymentMethod", OrderService_24162022.PAYMENT_COD);
            try {
                Cart_24162022 cart = cartService.getCart(userId);
                if (cart.getItems().isEmpty()) {
                    session.setAttribute(CartServlet_24162022.FLASH_ERROR, e.getMessage());
                    resp.sendRedirect(req.getContextPath() + "/cart");
                    return;
                }
                req.setAttribute("cart", cart);
            } catch (Exception ex) {
                req.setAttribute("errorMessage", e.getMessage() + " (" + ex.getMessage() + ")");
            }
            showForm(req, resp);
        }
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("pageTitle", "Thanh toan");
        req.getRequestDispatcher("/WEB-INF/jsp/order/checkout.jsp").forward(req, resp);
    }
}
