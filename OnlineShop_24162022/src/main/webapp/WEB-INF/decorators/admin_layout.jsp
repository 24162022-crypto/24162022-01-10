<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title><sitemesh:write property="title" /> - OnlineShop Admin</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style_24162022.css">
    <sitemesh:write property="head" />
</head>
<body>
    <%@ include file="header.jsp" %>

    <div class="admin-layout">
        <aside class="admin-sidebar">
            <h3>Trang quan tri</h3>
            <ul>
                <li><a href="<%= request.getContextPath() %>/admin/dashboard">Dashboard</a></li>
                <li><a href="<%= request.getContextPath() %>/admin/users">Quan ly Nguoi dung</a></li>
                <li><a href="<%= request.getContextPath() %>/admin/categories">Quan ly Danh muc</a></li>
            </ul>
        </aside>
        <main class="admin-content">
            <sitemesh:write property="body" />
        </main>
    </div>

    <%@ include file="footer.jsp" %>
</body>
</html>
