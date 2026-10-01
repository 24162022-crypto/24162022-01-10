package vn.hcmute.webpr.service;

import vn.hcmute.webpr.dao.CartDAO_24162022;
import vn.hcmute.webpr.dao.ProductDAO_24162022;
import vn.hcmute.webpr.entity.Cart_24162022;
import vn.hcmute.webpr.entity.CartItem_24162022;
import vn.hcmute.webpr.entity.Product_24162022;

/**
 * BUSINESS LAYER (SERVICE) - nghiep vu Gio hang cho role User.
 * Quy tac: 1 User co toi da 1 gio dang mo (Cart.status = 0); so luong moi dong
 * 1 <= quantity <= min(Product.stock, 99); chi them san pham status = 1 va stock > 0.
 * Moi loi nghiep vu duoc nem ra Exception kem message tieng Viet khong dau.
 * Gia va ton kho luon doc lai tu DB, khong tin du lieu tu form.
 */
public class CartService_24162022 {

    /** Khoa dung chung de tranh tao 2 gio dang mo cho cung 1 User khi bam them 2 lan lien tiep. */
    private static final Object CART_LOCK = new Object();

    private final CartDAO_24162022 cartDAO = new CartDAO_24162022();
    private final ProductDAO_24162022 productDAO = new ProductDAO_24162022();

    /** Them san pham vao gio. Neu da co thi cong don; tong vuot gioi han -> tu choi (khong tu cat). */
    public String addToCart(int userId, String productIdStr, String quantityStr) throws Exception {
        int productId = parsePositiveInt(productIdStr, "Ma san pham khong hop le.");
        int quantity = parsePositiveInt(quantityStr, "So luong khong hop le (phai la so nguyen >= 1).");

        Product_24162022 p = productDAO.findById(productId);
        if (p == null) {
            throw new Exception("San pham khong ton tai.");
        }
        if (p.getStatus() != 1) {
            throw new Exception("San pham \"" + p.getProductName() + "\" hien khong duoc ban.");
        }
        if (p.getStock() <= 0) {
            throw new Exception("San pham \"" + p.getProductName() + "\" da het hang.");
        }
        int limit = Math.min(p.getStock(), CartItem_24162022.MAX_QUANTITY_PER_LINE);

        int total;
        synchronized (CART_LOCK) {
            String cartId = cartDAO.findOpenCartId(userId);
            if (cartId == null) {
                cartId = cartDAO.createCart(userId);
            }
            CartItem_24162022 existing = cartDAO.findItemByCartAndProduct(cartId, productId);
            int current = (existing == null) ? 0 : existing.getQuantity();
            total = current + quantity;
            if (total > limit) {
                throw new Exception("Khong the them: trong gio da co " + current + ", them " + quantity
                        + " se vuot muc toi da cho phep " + limit + " (ton kho " + p.getStock()
                        + ", toi da " + CartItem_24162022.MAX_QUANTITY_PER_LINE + " moi dong).");
            }
            if (existing == null) {
                cartDAO.insertItem(cartId, productId, quantity, p.getPrice());
            } else {
                // cong don + cap nhat don gia theo gia hien tai cua san pham
                cartDAO.updateQuantityAndPrice(existing.getCartItemId(), cartId, total, p.getPrice());
            }
        }
        return "Da them \"" + p.getProductName() + "\" vao gio hang (so luong trong gio: "
                + total + ").";
    }

    /**
     * Sua so luong 1 dong. So luong khong hop le (chu, rong, am, vuot ton kho/99) -> nem loi,
     * du lieu cu giu nguyen. QUY UOC: so luong = 0 duoc hieu la xoa dong.
     */
    public String updateQuantity(int userId, String cartItemId, String quantityStr) throws Exception {
        CartItem_24162022 item = requireOwnedItem(userId, cartItemId);

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr == null ? "" : quantityStr.trim());
        } catch (NumberFormatException e) {
            throw new Exception("So luong khong hop le (phai la so nguyen).");
        }
        if (quantity < 0) {
            throw new Exception("So luong khong duoc am.");
        }
        if (quantity == 0) {
            cartDAO.deleteItem(item.getCartItemId(), item.getCartId());
            return "Da xoa \"" + item.getProductName() + "\" khoi gio hang (so luong = 0).";
        }
        if (item.getProductStatus() != 1) {
            throw new Exception("San pham \"" + item.getProductName() + "\" hien khong duoc ban, hay xoa khoi gio.");
        }
        int limit = item.getMaxQuantity();
        if (quantity > limit) {
            throw new Exception("So luong toi da cho \"" + item.getProductName() + "\" la " + limit
                    + " (ton kho " + item.getStock() + ").");
        }
        cartDAO.updateQuantity(item.getCartItemId(), item.getCartId(), quantity);
        return "Da cap nhat so luong \"" + item.getProductName() + "\" thanh " + quantity + ".";
    }

    /** Xoa 1 dong khoi gio (chi xoa duoc dong thuoc gio cua chinh User). */
    public String removeItem(int userId, String cartItemId) throws Exception {
        CartItem_24162022 item = requireOwnedItem(userId, cartItemId);
        cartDAO.deleteItem(item.getCartItemId(), item.getCartId());
        return "Da xoa \"" + item.getProductName() + "\" khoi gio hang.";
    }

    /** Xoa toan bo cac dong trong gio dang mo. */
    public String clearCart(int userId) throws Exception {
        String cartId = cartDAO.findOpenCartId(userId);
        if (cartId == null || cartDAO.deleteAllItems(cartId) == 0) {
            throw new Exception("Gio hang dang trong.");
        }
        return "Da xoa toan bo gio hang.";
    }

    /** Lay gio dang mo (kem cac dong). Chua co gio -> tra ve Cart rong (khong ghi DB). */
    public Cart_24162022 getCart(int userId) throws Exception {
        Cart_24162022 cart = new Cart_24162022();
        cart.setUserId(userId);
        String cartId = cartDAO.findOpenCartId(userId);
        if (cartId != null) {
            cart.setCartId(cartId);
            cart.setItems(cartDAO.findItemsByCartId(cartId));
        }
        return cart;
    }

    /** So mat hang (so dong) trong gio - hien tren header. */
    public int countItems(int userId) throws Exception {
        return cartDAO.countLines(userId);
    }

    private CartItem_24162022 requireOwnedItem(int userId, String cartItemId) throws Exception {
        if (cartItemId == null || cartItemId.trim().isEmpty()) {
            throw new Exception("Dong gio hang khong hop le.");
        }
        CartItem_24162022 item = cartDAO.findItemOfUser(userId, cartItemId.trim());
        if (item == null) {
            throw new Exception("Dong gio hang khong ton tai hoac khong thuoc gio hang cua ban.");
        }
        return item;
    }

    private int parsePositiveInt(String s, String errorMessage) throws Exception {
        try {
            int v = Integer.parseInt(s == null ? "" : s.trim());
            if (v < 1) {
                throw new Exception(errorMessage);
            }
            return v;
        } catch (NumberFormatException e) {
            throw new Exception(errorMessage);
        }
    }
}
