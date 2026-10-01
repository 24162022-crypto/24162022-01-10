package vn.hcmute.webpr.entity;

import java.util.List;

/** Ket qua phan trang dung chung cho cac trang danh sach (Cau 5). */
public class PageResult_24162022<T> {

    private final List<T> items;
    private final int page;
    private final int pageSize;
    private final int totalItems;

    public PageResult_24162022(List<T> items, int page, int pageSize, int totalItems) {
        this.items = items;
        this.page = page;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
    }

    public List<T> getItems() {
        return items;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public int getTotalPages() {
        return Math.max(1, (int) Math.ceil((double) totalItems / pageSize));
    }
}
