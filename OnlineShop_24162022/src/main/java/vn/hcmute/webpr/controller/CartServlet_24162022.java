package vn.hcmute.webpr.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.hcmute.webpr.service.CartService_24162022;

import java.io.IOException;

/**
 * Presentation Layer - Controller Gio hang (role User, da duoc UserAuthFilter_24162022 bao ve).
 * GET  /cart                       -> xem gio hang
 * POST /cart action=add            -> them san pham (productId, quantity, back)
 * POST /cart action=update         -> sua so luong 1 dong (cartItemId, quantity)
 * POST /cart action=remove         -> xoa 1 dong (cartItemId)
 * POST /cart action=clear          -> xoa toan bo gio
 * Moi thao tac ghi dung Post-Redirect-Get, thong bao qua Session (cartFlashSuccess / cartFlashError).
 */
@WebServlet("/cart")
public class CartServlet_24162022 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public static final String FLASH_SUCCESS = "cartFlashSuccess";
    public static final String FLASH_ERROR = "cartFlashError";

    private final CartService_24162022 cartService = new CartService_24162022();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int userId = currentUserId(req);
        try {
            req.setAttribute("cart", cartService.getCart(userId));
            refreshCartCount(req.getSession(), userId);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Khong the tai gio hang: " + e.getMessage());
        }
        req.setAttribute("pageTitle", "Gio hang");
        req.getRequestDispatcher("/WEB-INF/jsp/cart/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        int userId = currentUserId(req);
        String action = req.getParameter("action");
        String redirectPath = "/cart";

        try {
            String message;
            if ("add".equals(action)) {
                message = cartService.addToCart(userId, req.getParameter("productId"), req.getParameter("quantity"));
                redirectPath = backPath(req.getParameter("back"), req.getParameter("productId"));
            } else if ("update".equals(action)) {
                message = cartService.updateQuantity(userId, req.getParameter("cartItemId"), req.getParameter("quantity"));
            } else if ("remove".equals(action)) {
                message = cartService.removeItem(userId, req.getParameter("cartItemId"));
            } else if ("clear".equals(action)) {
                message = cartService.clearCart(userId);
            } else {
                throw new Exception("Thao tac khong hop le.");
            }
            session.setAttribute(FLASH_SUCCESS, message);
        } catch (Exception e) {
            session.setAttribute(FLASH_ERROR, e.getMessage());
            // loi khi them: van quay lai trang nguoi dung dang dung de thay thong bao
            if ("add".equals(action)) {
                redirectPath = backPath(req.getParameter("back"), req.getParameter("productId"));
            }
        }
        refreshCartCount(session, userId);
        resp.sendRedirect(req.getContextPath() + redirectPath);
    }

    /**
     * Chi cho phep quay lai cac trang noi bo da biet (khong nhan URL tu form de tranh open redirect):
     * back=product -> /product?id=..., back=products -> /products#p<id>, mac dinh -> /cart.
     */
    private String backPath(String back, String productIdStr) {
        int productId;
        try {
            productId = Integer.parseInt(productIdStr == null ? "" : productIdStr.trim());
        } catch (NumberFormatException e) {
            return "/cart";
        }
        if ("product".equals(back)) {
            return "/product?id=" + productId;
        }
        if ("products".equals(back)) {
            return "/products#p" + productId;
        }
        return "/cart";
    }

    private int currentUserId(HttpServletRequest req) {
        return (Integer) req.getSession().getAttribute("userId");
    }

    /** Cap nhat so mat hang trong gio luu o Session de header hien thi nhanh. */
    private void refreshCartCount(HttpSession session, int userId) {
        try {
            session.setAttribute("cartCount", cartService.countItems(userId));
        } catch (Exception ignored) {
            // giu nguyen gia tri cu neu loi DB
        }
    }
}
