<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title><sitemesh:write property="title" /> - OnlineShop</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style_24162022.css">
    <sitemesh:write property="head" />
</head>
<body>
    <%@ include file="header.jsp" %>

    <main class="content">
        <sitemesh:write property="body" />
    </main>

    <%@ include file="footer.jsp" %>
</body>
</html>
