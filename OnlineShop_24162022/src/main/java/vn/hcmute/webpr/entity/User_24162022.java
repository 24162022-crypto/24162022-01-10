package vn.hcmute.webpr.entity;

/**
 * MODEL / ENTITY - tuong ung bang Users.
 * roleName khong phai cot rieng trong bang Users, duoc lay tu JOIN voi UserRoles
 * khi doc du lieu (xem UserDAO_24162022).
 */
public class User_24162022 {

    private int userId;
    private String username;
    private String email;
    private String fullname;
    private String password;
    private String images;
    private String phone;
    private int status;   // 0 = chua kich hoat (cho OTP), 1 = da kich hoat
    private String code;  // ma OTP dang cho xac thuc (null sau khi kich hoat)
    private int roleId;
    private Integer sellerId; // co the null neu tai khoan khong gan voi Seller nao
    private String roleName;  // lay tu bang UserRoles (JOIN)

    public User_24162022() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
