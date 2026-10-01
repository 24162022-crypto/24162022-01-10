<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="vi_VN"/>
<html>
<head><title>Trang chu Seller</title></head>
<body>
    <div class="card">
        <h2>Trang chu Seller</h2>
        <p>Xin chao <b><c:out value="${sessionScope.fullname}"/></b> - Ma cua hang: <b>${sessionScope.sellerId}</b></p>
    </div>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-error"><c:out value="${errorMessage}"/></div>
    </c:if>

    <table class="data-table">
        <tr><th>ID</th><th>Ten san pham</th><th>Ma SP</th><th>Danh muc</th><th>Gia</th><th>Amount</th><th>Stock</th></tr>
        <c:forEach items="${products}" var="p">
            <tr>
                <td>${p.productId}</td>
                <td><a href="<c:url value='/product'><c:param name='id' value='${p.productId}'/></c:url>"><c:out value="${p.productName}"/></a></td>
                <td>${p.productCode}</td>
                <td><c:out value="${p.categoryName}"/></td>
                <td><fmt:formatNumber value="${p.price}" pattern="#,##0"/></td>
                <td>${p.amount}</td>
                <td>${p.stock}</td>
            </tr>
        </c:forEach>
        <c:if test="${empty products}">
            <tr><td colspan="7">Cua hang chua co san pham.</td></tr>
        </c:if>
    </table>
</body>
</html>
