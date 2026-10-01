package vn.hcmute.webpr.service;

import vn.hcmute.webpr.dao.CategoryDAO_24162022;
import vn.hcmute.webpr.dao.ProductDAO_24162022;
import vn.hcmute.webpr.entity.Category_24162022;
import vn.hcmute.webpr.entity.PageResult_24162022;

/**
 * BUSINESS LAYER (SERVICE) - CRUD Category co phan trang (Cau 5).
 * Cac phuong thuc nem Exception voi thong bao tieng Viet de Controller hien thi.
 */
public class CategoryService_24162022 {

    public static final int PAGE_SIZE = 5;

    private final CategoryDAO_24162022 categoryDAO = new CategoryDAO_24162022();
    private final ProductDAO_24162022 productDAO = new ProductDAO_24162022();

    public PageResult_24162022<Category_24162022> getPage(int page) throws Exception {
        int total = categoryDAO.count();
        int totalPages = Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));
        int current = Math.min(Math.max(1, page), totalPages);
        return new PageResult_24162022<>(
                categoryDAO.findPage((current - 1) * PAGE_SIZE, PAGE_SIZE), current, PAGE_SIZE, total);
    }

    public Category_24162022 findById(int categoryId) throws Exception {
        Category_24162022 c = categoryDAO.findById(categoryId);
        if (c == null) {
            throw new Exception("Danh muc khong ton tai.");
        }
        return c;
    }

    /** Them moi neu categoryId = 0, nguoc lai cap nhat. */
    public void save(Category_24162022 c) throws Exception {
        if (c.getCategoryName() == null || c.getCategoryName().trim().isEmpty()) {
            throw new Exception("Vui long nhap ten danh muc.");
        }
        c.setCategoryName(c.getCategoryName().trim());
        if (c.getCategoryId() == 0) {
            categoryDAO.insert(c);
        } else if (!categoryDAO.update(c)) {
            throw new Exception("Danh muc khong ton tai.");
        }
    }

    public void delete(int categoryId) throws Exception {
        if (productDAO.countByCategoryId(categoryId) > 0) {
            throw new Exception("Khong the xoa: danh muc dang co san pham.");
        }
        if (!categoryDAO.delete(categoryId)) {
            throw new Exception("Danh muc khong ton tai.");
        }
    }
}
