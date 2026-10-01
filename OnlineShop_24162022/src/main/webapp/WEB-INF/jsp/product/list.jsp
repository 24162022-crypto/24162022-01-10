<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="vi_VN"/>
<html>
<head><title>San pham</title></head>
<body>
    <h2>Danh sach san pham theo cua hang</h2>

    <jsp:include page="/WEB-INF/jsp/common/cart-flash.jsp"/>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-error"><c:out value="${errorMessage}"/></div>
    </c:if>
    <c:if test="${empty errorMessage and empty sellers}">
        <div class="card">Chua co san pham nao.</div>
    </c:if>

    <c:forEach items="${sellers}" var="s">
        <table class="product-table">
            <tr>
                <th colspan="2">
                    Ma cua hang: ${s.sellerId}
                    <span class="muted">(<c:out value="${s.sellername}"/> - ${s.products.size()} san pham)</span>
                </th>
            </tr>
            <c:forEach items="${s.products}" var="p">
                <tr id="p${p.productId}">
                    <td class="img-cell">
                        <c:set var="img" value="${p.images}" scope="request"/>
                        <jsp:include page="/WEB-INF/jsp/common/product-image.jsp"/>
                    </td>
                    <td class="info-cell">
                        <div><b>Ten san pham:</b>
                            <a href="<c:url value='/product'><c:param name='id' value='${p.productId}'/></c:url>"><c:out value="${p.productName}"/></a>
                        </div>
                        <div><b>Ma san pham:</b> ${p.productCode}</div>
                        <div><b>Danh muc:</b> <c:out value="${p.categoryName}"/></div>
                        <div><b>Gia:</b> <fmt:formatNumber value="${p.price}" pattern="#,##0"/> VND</div>
                        <div><b>Amount:</b> ${p.amount}</div>
                        <c:if test="${p.status == 1 and p.stock > 0}">
                            <c:choose>
                                <c:when test="${sessionScope.role eq 'User'}">
                                    <form method="post" action="<c:url value='/cart'/>" class="inline-form">
                                        <input type="hidden" name="action" value="add">
                                        <input type="hidden" name="productId" value="${p.productId}">
                                        <input type="hidden" name="quantity" value="1">
                                        <input type="hidden" name="back" value="products">
                                        <button type="submit" class="btn btn-checkout">Them vao gio</button>
                                    </form>
                                </c:when>
                                <c:when test="${empty sessionScope.userId}">
                                    <a class="btn btn-checkout" href="<c:url value='/login'><c:param name='next' value='/products'/></c:url>">Dang nhap de mua</a>
                                </c:when>
                            </c:choose>
                        </c:if>
                        <c:if test="${p.stock <= 0}"><div class="cart-warn">Het hang</div></c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:forEach>
</body>
</html>
