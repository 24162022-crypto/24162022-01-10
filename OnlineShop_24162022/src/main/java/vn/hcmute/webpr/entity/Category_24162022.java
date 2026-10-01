package vn.hcmute.webpr.entity;

/** MODEL / ENTITY - tuong ung bang Category. */
public class Category_24162022 {

    private int categoryId;
    private String categoryName;
    private String images;
    private int status; // 1 = hien thi, 0 = an

    public Category_24162022() {
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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
}
