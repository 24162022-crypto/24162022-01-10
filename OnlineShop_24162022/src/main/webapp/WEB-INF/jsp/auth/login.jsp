<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head><title>Dang nhap</title></head>
<body>
    <div class="card auth-card">
        <h2>Dang nhap</h2>

        <div class="alert alert-error" style="${empty errorMessage ? 'display:none;' : ''}">${errorMessage}</div>
        <div class="alert alert-success" style="${empty successMessage ? 'display:none;' : ''}">${successMessage}</div>

        <form method="post" action="<%= request.getContextPath() %>/login">
            <div class="form-group">
                <label>Ten dang nhap</label>
                <input type="text" name="username" value="${username}" required autofocus>
            </div>
            <div class="form-group">
                <label>Mat khau</label>
                <input type="password" name="password" required>
            </div>
            <button type="submit" class="btn">Dang nhap</button>
        </form>

        <p style="margin-top:14px;">Chua co tai khoan? <a href="<%= request.getContextPath() %>/register">Dang ky ngay</a></p>
    </div>
</body>
</html>
