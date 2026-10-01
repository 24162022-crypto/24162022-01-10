<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<fmt:setLocale value="vi_VN"/>
<html>
<head><title>Don hang cua toi</title></head>
<body>
    <h2>Don hang cua toi</h2>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-error"><c:out value="${errorMessage}"/></div>
    </c:if>

    <c:choose>
        <c:when test="${empty orders}">
            <div class="card">
                <p>Ban chua co don hang nao.</p>
                <a class="btn" href="<c:url value='/products'/>">Mua hang ngay</a>
            </div>
        </c:when>
        <c:otherwise>
            <table class="data-table cart-table">
                <tr>
                    <th>Ma don</th>
                    <th>Ngay dat</th>
                    <th>Nguoi nhan</th>
                    <th class="num">So mat hang</th>
                    <th class="num">Tong tien</th>
                    <th>Thanh toan</th>
                    <th>Thao tac</th>
                </tr>
                <c:forEach items="${orders}" var="o">
                    <tr>
                        <td><c:out value="${fn:substring(o.cartId, 0, 8)}"/>...</td>
                        <td><fmt:formatDate value="${o.buyDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                        <td><c:out value="${o.receiverName}"/></td>
                        <td class="num">${o.itemCount}</td>
                        <td class="num"><fmt:formatNumber value="${o.totalAmount}" pattern="#,##0"/> VND</td>
                        <td>COD - cho giao</td>
                        <td class="actions">
                            <a class="btn" href="<c:url value='/orders'><c:param name='id' value='${o.cartId}'/></c:url>">Xem chi tiet</a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>
</body>
</html>
