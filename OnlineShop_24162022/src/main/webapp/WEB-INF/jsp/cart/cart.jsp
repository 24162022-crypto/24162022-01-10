<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="vi_VN"/>
<html>
<head><title>Gio hang</title></head>
<body>
    <h2>Gio hang cua ban</h2>

    <jsp:include page="/WEB-INF/jsp/common/cart-flash.jsp"/>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-error"><c:out value="${errorMessage}"/></div>
    </c:if>

    <c:choose>
        <c:when test="${empty cart.items}">
            <div class="card">
                <p>Gio hang cua ban dang trong.</p>
                <a class="btn" href="<c:url value='/products'/>">Tiep tuc mua hang</a>
            </div>
        </c:when>
        <c:otherwise>
            <table class="data-table cart-table">
                <tr>
                    <th>Anh</th>
                    <th>San pham</th>
                    <th class="num">Don gia</th>
                    <th>So luong</th>
                    <th class="num">Thanh tien</th>
                    <th>Thao tac</th>
                </tr>
                <c:forEach items="${cart.items}" var="item">
                    <tr>
                        <td class="thumb">
                            <c:set var="img" value="${item.images}" scope="request"/>
                            <jsp:include page="/WEB-INF/jsp/common/product-image.jsp"/>
                        </td>
                        <td>
                            <a href="<c:url value='/product'><c:param name='id' value='${item.productId}'/></c:url>"><c:out value="${item.productName}"/></a>
                            <c:choose>
                                <c:when test="${item.productStatus != 1}"><div class="cart-warn">San pham hien khong duoc ban, hay xoa khoi gio.</div></c:when>
                                <c:when test="${item.stock <= 0}"><div class="cart-warn">San pham da het hang, hay xoa khoi gio.</div></c:when>
                                <c:when test="${item.quantity > item.stock}"><div class="cart-warn">Ton kho chi con ${item.stock}, hay giam so luong.</div></c:when>
                                <c:otherwise><div class="muted">Ton kho: ${item.stock}</div></c:otherwise>
                            </c:choose>
                        </td>
                        <td class="num"><fmt:formatNumber value="${item.unitPrice}" pattern="#,##0"/> VND</td>
                        <td>
                            <form method="post" action="<c:url value='/cart'/>" class="inline-form">
                                <input type="hidden" name="action" value="update">
                                <input type="hidden" name="cartItemId" value="<c:out value='${item.cartItemId}'/>">
                                <input type="number" name="quantity" class="qty-input" value="${item.quantity}" min="1" max="${item.maxQuantity}" required>
                                <button type="submit" class="btn">Cap nhat</button>
                            </form>
                        </td>
                        <td class="num"><fmt:formatNumber value="${item.lineTotal}" pattern="#,##0"/> VND</td>
                        <td class="actions">
                            <form method="post" action="<c:url value='/cart'/>" class="inline-form"
                                  onsubmit="return confirm('Xoa san pham nay khoi gio hang?');">
                                <input type="hidden" name="action" value="remove">
                                <input type="hidden" name="cartItemId" value="<c:out value='${item.cartItemId}'/>">
                                <button type="submit" class="btn btn-danger">Xoa</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <tr class="total-row">
                    <td colspan="4" class="num">
                        Tong so mat hang: <b>${cart.items.size()}</b> (tong so luong: ${cart.totalQuantity}) &nbsp; - &nbsp; Tong tien:
                    </td>
                    <td class="num"><b><fmt:formatNumber value="${cart.total}" pattern="#,##0"/> VND</b></td>
                    <td></td>
                </tr>
            </table>

            <div class="cart-actions">
                <a class="btn btn-secondary" href="<c:url value='/products'/>">&laquo; Tiep tuc mua hang</a>
                <form method="post" action="<c:url value='/cart'/>" class="inline-form"
                      onsubmit="return confirm('Xoa toan bo gio hang?');">
                    <input type="hidden" name="action" value="clear">
                    <button type="submit" class="btn btn-danger">Xoa toan bo gio hang</button>
                </form>
                <a class="btn btn-checkout" href="<c:url value='/checkout'/>">Thanh toan &raquo;</a>
            </div>
        </c:otherwise>
    </c:choose>
</body>
</html>
