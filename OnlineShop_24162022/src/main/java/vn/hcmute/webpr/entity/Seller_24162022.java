package vn.hcmute.webpr.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * MODEL / ENTITY - tuong ung bang Seller.
 * products khong phai cot trong bang, dung de gom san pham theo cua hang (Cau 3).
 */
public class Seller_24162022 {

    private int sellerId;
    private String sellername;
    private String images;
    private int status;
    private List<Product_24162022> products = new ArrayList<>();

    public Seller_24162022() {
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellername() {
        return sellername;
    }

    public void setSellername(String sellername) {
        this.sellername = sellername;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Product_24162022> getProducts() {
        return products;
    }

    public void setProducts(List<Product_24162022> products) {
        this.products = products;
    }
}
