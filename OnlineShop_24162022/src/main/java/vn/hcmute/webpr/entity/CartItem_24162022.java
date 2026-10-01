package vn.hcmute.webpr.entity;

/**
 * MODEL / ENTITY - tuong ung bang CartItem (1 dong trong gio hang / don hang).
 * Cac truong productName, images, stock, productStatus lay tu JOIN voi Product
 * (khong phai cot cua CartItem) - xem CartDAO_24162022.
 */
public class CartItem_24162022 {

    /** Gioi han toi da so luong cho 1 dong (quy tac them ngoai ton kho). */
    public static final int MAX_QUANTITY_PER_LINE = 99;

    private String cartItemId;
    private int quantity;
    private double unitPrice;   // gia tai thoi diem them vao gio
    private int productId;
    private String cartId;

    private String productName; // JOIN Product
    private String images;      // JOIN Product
    private int stock;          // JOIN Product (ton kho hien tai)
    private int productStatus;  // JOIN Product (status = 1: dang ban)

    public CartItem_24162022() {
    }

    /** Thanh tien cua dong = don gia * so luong (lam tron VND). */
    public double getLineTotal() {
        return Math.round(unitPrice * quantity);
    }

    /** So luong toi da cho phep nhap: min(ton kho, 99). */
    public int getMaxQuantity() {
        return Math.max(0, Math.min(stock, MAX_QUANTITY_PER_LINE));
    }

    /** Dong con hop le de thanh toan: san pham dang ban va du ton kho. */
    public boolean isAvailable() {
        return productStatus == 1 && stock >= quantity && quantity >= 1;
    }

    public String getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(String cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(int productStatus) {
        this.productStatus = productStatus;
    }
}
