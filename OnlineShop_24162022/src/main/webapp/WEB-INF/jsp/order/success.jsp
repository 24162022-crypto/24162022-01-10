<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Dat hang thanh cong</title></head>
<body>
    <c:choose>
        <c:when test="${not empty errorMessage}">
            <h2>Don hang</h2>
            <div class="alert alert-error"><c:out value="${errorMessage}"/></div>
        </c:when>
        <c:otherwise>
            <h2>Dat hang thanh cong</h2>
            <div class="alert alert-success">Cam on ban da dat hang! Don hang cua ban se duoc giao va thanh toan khi nhan hang (COD).</div>
            <jsp:include page="/WEB-INF/jsp/order/order-info.jsp"/>
        </c:otherwise>
    </c:choose>

    <a class="btn btn-secondary" href="<c:url value='/products'/>">Tiep tuc mua hang</a>
    <a class="btn" href="<c:url value='/orders'/>">Don hang cua toi</a>
</body>
</html>
