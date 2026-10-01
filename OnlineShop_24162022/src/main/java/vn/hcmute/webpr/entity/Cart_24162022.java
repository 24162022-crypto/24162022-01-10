package vn.hcmute.webpr.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * MODEL / ENTITY - tuong ung bang Cart.
 * status = 0: gio hang dang mo; status = 1: da dat hang (thanh toan COD, cho giao).
 */
public class Cart_24162022 {

    public static final int STATUS_OPEN = 0;
    public static final int STATUS_ORDERED = 1;

    private String cartId;
    private int userId;
    private Timestamp buyDate;
    private int status;
    private String receiverName;
    private String receiverPhone;
    private String shippingAddress;
    private String note;
    private String paymentMethod;
    private double totalAmount;   // chi co gia tri sau khi dat hang
    private int itemCount;        // so dong san pham (dung cho danh sach don hang)

    private List<CartItem_24162022> items = new ArrayList<>();

    public Cart_24162022() {
    }

    /** Tong tien tinh tu cac dong hien co trong gio. */
    public double getTotal() {
        double sum = 0;
        for (CartItem_24162022 it : items) {
            sum += it.getLineTotal();
        }
        return sum;
    }

    /** Tong so luong (cong don so luong cua moi dong). */
    public int getTotalQuantity() {
        int sum = 0;
        for (CartItem_24162022 it : items) {
            sum += it.getQuantity();
        }
        return sum;
    }

    /** Co it nhat 1 dong khong con hop le (het hang / ngung ban / vuot ton kho). */
    public boolean isHasUnavailableItem() {
        for (CartItem_24162022 it : items) {
            if (!it.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Timestamp buyDate) {
        this.buyDate = buyDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public List<CartItem_24162022> getItems() {
        return items;
    }

    public void setItems(List<CartItem_24162022> items) {
        this.items = items;
    }
}
