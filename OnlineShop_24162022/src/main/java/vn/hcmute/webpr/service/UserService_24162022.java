package vn.hcmute.webpr.service;

import vn.hcmute.webpr.dao.SellerDAO_24162022;
import vn.hcmute.webpr.dao.UserDAO_24162022;
import vn.hcmute.webpr.dao.UserRoleDAO_24162022;
import vn.hcmute.webpr.entity.PageResult_24162022;
import vn.hcmute.webpr.entity.Seller_24162022;
import vn.hcmute.webpr.entity.UserRole_24162022;
import vn.hcmute.webpr.entity.User_24162022;
import vn.hcmute.webpr.util.MailUtil_24162022;

import java.security.SecureRandom;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

/**
 * BUSINESS LAYER (SERVICE) - nghiep vu Dang ky (kich hoat bang OTP qua mail),
 * xac thuc OTP, Dang nhap (Cau 2) va CRUD User co phan trang (Cau 5).
 * Cac phuong thuc nem Exception voi thong bao tieng Viet
 * de Controller (Servlet) hien thi truc tiep cho nguoi dung.
 */
public class UserService_24162022 {

    public static final int PAGE_SIZE = 5;

    private final UserDAO_24162022 userDAO = new UserDAO_24162022();
    private final UserRoleDAO_24162022 userRoleDAO = new UserRoleDAO_24162022();
    private final SellerDAO_24162022 sellerDAO = new SellerDAO_24162022();
    private static final SecureRandom RANDOM = new SecureRandom();

    private String generateOtp() {
        int otp = 100000 + RANDOM.nextInt(900000); // 6 chu so, tu 100000 - 999999
        return String.valueOf(otp);
    }

    /**
     * Dang ky tai khoan moi (vai tro mac dinh: "User"), sinh ma OTP va gui qua email.
     */
    public void register(String username, String email, String fullname, String phone, String password) throws Exception {
        if (isBlank(username) || isBlank(email) || isBlank(fullname) || isBlank(password)) {
            throw new Exception("Vui long nhap day du thong tin bat buoc.");
        }
        if (userDAO.existsByUsername(username)) {
            throw new Exception("Ten dang nhap da ton tai.");
        }
        if (userDAO.existsByEmail(email)) {
            throw new Exception("Email da duoc su dung cho tai khoan khac.");
        }

        int roleId = userDAO.getRoleIdByName("User");
        if (roleId == -1) {
            throw new Exception("He thong chua cau hinh vai tro 'User' trong bang UserRoles.");
        }

        String otp = generateOtp();

        User_24162022 user = new User_24162022();
        user.setUsername(username);
        user.setEmail(email);
        user.setFullname(fullname);
        user.setPassword(password);
        user.setPhone(phone);
        user.setCode(otp);
        user.setRoleId(roleId);

        userDAO.insertUser(user);

        // Neu chua cau hinh SMTP that (MailUtil_24162022.SMTP_EMAIL/SMTP_APP_PASSWORD),
        // gui mail se that bai. De KHONG chan luong dang ky/test cua sinh vien,
        // loi gui mail chi duoc ghi ra console (OTP in kem) thay vi nem Exception len Servlet.
        // => Sau khi cau hinh SMTP that, email se duoc gui binh thuong va nhanh nay se khong con xay ra.
        try {
            MailUtil_24162022.sendOtpEmail(email, fullname, otp);
        } catch (Exception mailEx) {
            System.out.println("========================================================");
            System.out.println("[CANH BAO] Gui email that bai (kiem tra SMTP_EMAIL / SMTP_APP_PASSWORD trong MailUtil_24162022).");
            System.out.println("[FALLBACK] Ma OTP cua tai khoan '" + username + "' (" + email + ") la: " + otp);
            System.out.println("Chi tiet loi: " + mailEx.getMessage());
            System.out.println("========================================================");
        }
    }

    /** Gui lai ma OTP moi cho tai khoan chua kich hoat. */
    public void resendOtp(String username) throws Exception {
        User_24162022 user = userDAO.findByUsername(username);
        if (user == null) {
            throw new Exception("Tai khoan khong ton tai.");
        }
        if (user.getStatus() == 1) {
            throw new Exception("Tai khoan da duoc kich hoat truoc do.");
        }
        String otp = generateOtp();
        userDAO.updateOtpCode(username, otp);
        try {
            MailUtil_24162022.sendOtpEmail(user.getEmail(), user.getFullname(), otp);
        } catch (Exception mailEx) {
            System.out.println("[FALLBACK] Ma OTP moi cua tai khoan '" + username + "' la: " + otp
                    + " (gui mail that bai: " + mailEx.getMessage() + ")");
        }
    }

    /** Xac thuc ma OTP de kich hoat tai khoan. */
    public void verifyOtp(String username, String code) throws Exception {
        if (isBlank(code)) {
            throw new Exception("Vui long nhap ma OTP.");
        }
        boolean ok = userDAO.activateByUsernameAndCode(username, code.trim());
        if (!ok) {
            throw new Exception("Ma OTP khong dung hoac tai khoan da duoc kich hoat truoc do.");
        }
    }

    /**
     * Dang nhap: kiem tra ton tai tai khoan, mat khau va trang thai kich hoat.
     * @return User_24162022 (da kem roleName) neu dang nhap thanh cong
     */
    public User_24162022 login(String username, String password) throws Exception {
        if (isBlank(username) || isBlank(password)) {
            throw new Exception("Vui long nhap day du ten dang nhap va mat khau.");
        }
        User_24162022 user = userDAO.findByUsername(username);
        if (user == null) {
            throw new Exception("Tai khoan khong ton tai.");
        }
        if (!user.getPassword().equals(password)) {
            throw new Exception("Mat khau khong dung.");
        }
        if (user.getStatus() != 1) {
            throw new Exception("Tai khoan chua duoc kich hoat. Vui long kiem tra email de lay ma OTP.");
        }
        return user;
    }

    // ===================== CRUD + PHAN TRANG (Cau 5) =====================

    public PageResult_24162022<User_24162022> getPage(int page) throws Exception {
        int total = userDAO.count();
        int totalPages = Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));
        int current = Math.min(Math.max(1, page), totalPages);
        return new PageResult_24162022<>(
                userDAO.findPage((current - 1) * PAGE_SIZE, PAGE_SIZE), current, PAGE_SIZE, total);
    }

    public User_24162022 findById(int userId) throws Exception {
        User_24162022 u = userDAO.findById(userId);
        if (u == null) {
            throw new Exception("Nguoi dung khong ton tai.");
        }
        return u;
    }

    public List<UserRole_24162022> getAllRoles() throws Exception {
        return userRoleDAO.findAll();
    }

    public List<Seller_24162022> getAllSellers() throws Exception {
        return sellerDAO.findAll();
    }

    /**
     * Them moi neu userId = 0, nguoc lai cap nhat.
     * Khi cap nhat, de trong mat khau nghia la giu nguyen mat khau cu.
     */
    public void save(User_24162022 u) throws Exception {
        if (isBlank(u.getUsername()) || isBlank(u.getEmail()) || isBlank(u.getFullname())) {
            throw new Exception("Vui long nhap day du ten dang nhap, email va ho ten.");
        }
        boolean isNew = u.getUserId() == 0;
        if (isNew) {
            if (isBlank(u.getPassword())) {
                throw new Exception("Vui long nhap mat khau.");
            }
            if (userDAO.existsByUsername(u.getUsername())) {
                throw new Exception("Ten dang nhap da ton tai.");
            }
            if (userDAO.existsByEmail(u.getEmail())) {
                throw new Exception("Email da duoc su dung cho tai khoan khac.");
            }
            userDAO.insertByAdmin(u);
        } else {
            User_24162022 old = findById(u.getUserId());
            if (userDAO.existsByUsernameExcept(u.getUsername(), u.getUserId())) {
                throw new Exception("Ten dang nhap da ton tai.");
            }
            if (userDAO.existsByEmailExcept(u.getEmail(), u.getUserId())) {
                throw new Exception("Email da duoc su dung cho tai khoan khac.");
            }
            if (isBlank(u.getPassword())) {
                u.setPassword(old.getPassword());
            }
            userDAO.update(u);
        }
    }

    public void delete(int userId, int currentUserId) throws Exception {
        if (userId == currentUserId) {
            throw new Exception("Khong the xoa tai khoan dang dang nhap.");
        }
        try {
            if (!userDAO.delete(userId)) {
                throw new Exception("Nguoi dung khong ton tai.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new Exception("Khong the xoa: nguoi dung da co gio hang/don hang.");
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
