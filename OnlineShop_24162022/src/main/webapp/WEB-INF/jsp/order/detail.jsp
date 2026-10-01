<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Chi tiet don hang</title></head>
<body>
    <h2>Chi tiet don hang</h2>
    <c:choose>
        <c:when test="${not empty errorMessage}">
            <div class="alert alert-error"><c:out value="${errorMessage}"/></div>
        </c:when>
        <c:otherwise>
            <jsp:include page="/WEB-INF/jsp/order/order-info.jsp"/>
        </c:otherwise>
    </c:choose>
    <a class="btn btn-secondary" href="<c:url value='/orders'/>">&laquo; Danh sach don hang</a>
</body>
</html>
