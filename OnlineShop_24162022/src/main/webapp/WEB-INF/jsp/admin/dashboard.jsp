<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head><title>Dashboard</title></head>
<body>
    <div class="card">
        <h2>Trang quan tri (Admin Dashboard)</h2>
        <p>Chon chuc nang quan tri o thanh ben trai:</p>
        <ul>
            <li><a href="<%= request.getContextPath() %>/admin/users">Quan ly Nguoi dung</a> - them, xem, sua, xoa tai khoan (co phan trang)</li>
            <li><a href="<%= request.getContextPath() %>/admin/categories">Quan ly Danh muc</a> - them, xem, sua, xoa danh muc (co phan trang)</li>
        </ul>
    </div>
</body>
</html>
