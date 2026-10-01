<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Khong co quyen</title></head>
<body>
    <div class="card">
        <h2>Khong co quyen truy cap</h2>
        <div class="alert alert-error">Chuc nang gio hang va thanh toan chi danh cho tai khoan co vai tro User.</div>
        <a class="btn btn-secondary" href="<c:url value='/products'/>">&laquo; Ve danh sach san pham</a>
        <a class="btn" href="<c:url value='/home'/>">Trang chu</a>
    </div>
</body>
</html>
