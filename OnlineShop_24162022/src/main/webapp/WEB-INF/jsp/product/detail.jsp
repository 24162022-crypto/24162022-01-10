<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="vi_VN"/>
<html>
<head><title>Chi tiet san pham</title></head>
<body>
    <h2>Chi tiet san pham</h2>

    <jsp:include page="/WEB-INF/jsp/common/cart-flash.jsp"/>

    <c:choose>
        <c:when test="${not empty errorMessage}">
            <div class="alert alert-error"><c:out value="${errorMessage}"/></div>
        </c:when>
        <c:otherwise>
            <table class="product-table">
                <tr>
                    <td class="img-cell">
                        <c:set var="img" value="${product.images}" scope="request"/>
                        <jsp:include page="/WEB-INF/jsp/common/product-image.jsp"/>
                    </td>
                    <td class="info-cell">
                        <div><b>Ten san pham:</b> <c:out value="${product.productName}"/></div>
                        <div><b>Ma san pham:</b> ${product.productCode}</div>
                        <div><b>Danh muc:</b> <c:out value="${product.categoryName}"/></div>
                        <div><b>Gia:</b> <fmt:formatNumber value="${product.price}" pattern="#,##0"/> VND</div>
                        <div><b>Amount:</b> ${product.amount}</div>
                        <div><b>Ton kho:</b> ${product.stock}</div>
                        <div><b>Description:</b> <c:out value="${product.description}"/></div>
                    </td>
                </tr>
            </table>

            <%-- Them vao gio hang: chi hien nut cho role User; khach chua dang nhap duoc dan toi trang dang nhap --%>
            <c:choose>
                <c:when test="${product.status != 1}">
                    <div class="alert alert-error">San pham hien khong duoc ban.</div>
                </c:when>
                <c:when test="${product.stock <= 0}">
                    <div class="alert alert-error">San pham da het hang.</div>
                </c:when>
                <c:when test="${sessionScope.role eq 'User'}">
                    <form method="post" action="<c:url value='/cart'/>" class="add-cart-form">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="productId" value="${product.productId}">
                        <input type="hidden" name="back" value="product">
                        <label>So luong:
                            <input type="number" name="quantity" class="qty-input" value="1" min="1"
                                   max="${product.stock > 99 ? 99 : product.stock}" required>
                        </label>
                        <button type="submit" class="btn btn-checkout">Them vao gio hang</button>
                        <a class="btn btn-secondary" href="<c:url value='/cart'/>">Xem gio hang</a>
                    </form>
                </c:when>
                <c:when test="${empty sessionScope.userId}">
                    <a class="btn btn-checkout" href="<c:url value='/login'><c:param name='next' value='/product?id=${product.productId}'/></c:url>">Dang nhap de mua hang</a>
                </c:when>
                <c:otherwise>
                    <div class="muted">Chi tai khoan User moi mua duoc hang.</div>
                </c:otherwise>
            </c:choose>
        </c:otherwise>
    </c:choose>

    <br><br>

    <a class="btn btn-secondary" href="<c:url value='/products'/>">&laquo; Quay lai danh sach</a>
</body>
</html>
