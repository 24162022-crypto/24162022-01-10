<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head><title>Xac thuc OTP</title></head>
<body>
    <div class="card auth-card">
        <h2>Xac thuc ma OTP</h2>
        <p>Ma OTP kich hoat tai khoan <b>${username}</b> da duoc gui toi email dang ky.
           Vui long kiem tra hop thu (ke ca muc Spam) va nhap ma gom 6 chu so ben duoi.</p>

        <div class="alert alert-error" style="${empty errorMessage ? 'display:none;' : ''}">${errorMessage}</div>
        <div class="alert alert-success" style="${empty successMessage ? 'display:none;' : ''}">${successMessage}</div>

        <form method="post" action="<%= request.getContextPath() %>/verify-otp">
            <div class="form-group">
                <label>Ma OTP</label>
                <input type="text" name="code" maxlength="6" pattern="\d{6}" required autofocus>
            </div>
            <button type="submit" class="btn">Xac thuc</button>
        </form>

        <form method="post" action="<%= request.getContextPath() %>/verify-otp" style="margin-top:10px;">
            <input type="hidden" name="action" value="resend">
            <button type="submit" class="btn btn-secondary">Gui lai ma OTP</button>
        </form>
    </div>
</body>
</html>
