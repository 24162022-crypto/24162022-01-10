package vn.hcmute.webpr.service;

import vn.hcmute.webpr.dao.ProductDAO_24162022;
import vn.hcmute.webpr.entity.Product_24162022;
import vn.hcmute.webpr.entity.Seller_24162022;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * BUSINESS LAYER (SERVICE) - nghiep vu San pham:
 * gom san pham theo tung cua hang (Cau 3) va lay chi tiet 1 san pham (Cau 4).
 */
public class ProductService_24162022 {

    private final ProductDAO_24162022 productDAO = new ProductDAO_24162022();

    /**
     * Gom tat ca san pham dang ban theo ma cua hang (SellerID).
     * @return danh sach Seller, moi Seller chua danh sach san pham cua minh
     */
    public List<Seller_24162022> getProductsGroupedBySeller() throws Exception {
        Map<Integer, Seller_24162022> groups = new LinkedHashMap<>();
        for (Product_24162022 p : productDAO.findAllActiveOrderBySeller()) {
            Seller_24162022 seller = groups.get(p.getSellerId());
            if (seller == null) {
                seller = new Seller_24162022();
                seller.setSellerId(p.getSellerId());
                seller.setSellername(p.getSellername());
                groups.put(p.getSellerId(), seller);
            }
            seller.getProducts().add(p);
        }
        return new ArrayList<>(groups.values());
    }

    public List<Product_24162022> getProductsBySeller(int sellerId) throws Exception {
        return productDAO.findBySellerId(sellerId);
    }

    public Product_24162022 getProductDetail(int productId) throws Exception {
        Product_24162022 p = productDAO.findById(productId);
        if (p == null) {
            throw new Exception("San pham khong ton tai.");
        }
        return p;
    }
}
