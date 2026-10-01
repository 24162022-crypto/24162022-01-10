package vn.hcmute.webpr.util;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

/**
 * Lop tien ich gui email (SMTP) - dung de gui ma OTP kich hoat tai khoan o Cau 2.
 */
public class MailUtil_24162022 {

    // ===== TODO: CHINH 2 THONG SO NAY THEO TAI KHOAN GMAIL DUNG DE GUI OTP =====
    // Luu y: phai bat "2-Step Verification" cho tai khoan Gmail, sau do tao
    // "App Password" (16 ky tu) tai https://myaccount.google.com/apppasswords
    // KHONG dung mat khau dang nhap Gmail thong thuong o day.
    // Neu chua sua 2 dong ben duoi, gui mail se that bai (Gmail tu choi xac thuc);
    // UserService_24162022 se fallback bang cach IN MA OTP RA CONSOLE TOMCAT
    // de ban van test duoc luong dang ky/xac thuc trong khi cho cau hinh SMTP that.
    private static final String SMTP_EMAIL = "your_email@gmail.com";
    private static final String SMTP_APP_PASSWORD = "your_16_char_app_password";

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";

    public static void sendOtpEmail(String toEmail, String fullname, String otpCode) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_EMAIL, SMTP_APP_PASSWORD);
            }
        });

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SMTP_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("[OnlineShop_24162022] Ma xac thuc OTP kich hoat tai khoan");

        String content =
                "Xin chao " + fullname + ",\n\n" +
                "Ma OTP kich hoat tai khoan cua ban la: " + otpCode + "\n" +
                "Vui long nhap ma nay tren trang xac thuc de kich hoat tai khoan.\n" +
                "Vui long khong chia se ma nay cho bat ky ai.\n\n" +
                "Neu ban khong thuc hien dang ky nay, vui long bo qua email nay.\n\n" +
                "Tran trong,\nOnlineShop_24162022";

        message.setText(content, "UTF-8");

        Transport.send(message);
    }
}
