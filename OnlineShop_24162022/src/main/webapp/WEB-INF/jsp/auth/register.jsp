<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head><title>Dang ky tai khoan</title></head>
<body>
    <div class="card auth-card">
        <h2>Dang ky tai khoan</h2>

        <div class="alert alert-error" style="${empty errorMessage ? 'display:none;' : ''}">${errorMessage}</div>

        <form method="post" action="<%= request.getContextPath() %>/register">
            <div class="form-group">
                <label>Ten dang nhap</label>
                <input type="text" name="username" value="${username}" required autofocus>
            </div>
            <div class="form-group">
                <label>Email (dung de nhan ma OTP)</label>
                <input type="email" name="email" value="${email}" required>
            </div>
            <div class="form-group">
                <label>Ho ten</label>
                <input type="text" name="fullname" value="${fullname}" required>
            </div>
            <div class="form-group">
                <label>So dien thoai</label>
                <input type="text" name="phone" value="${phone}">
            </div>
            <div class="form-group">
                <label>Mat khau</label>
                <input type="password" name="password" required>
            </div>
            <div class="form-group">
                <label>Xac nhan mat khau</label>
                <input type="password" name="confirmPassword" required>
            </div>
            <button type="submit" class="btn">Dang ky</button>
        </form>

        <p style="margin-top:14px;">Da co tai khoan? <a href="<%= request.getContextPath() %>/login">Dang nhap</a></p>
    </div>
</body>
</html>
