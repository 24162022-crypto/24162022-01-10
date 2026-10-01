<%-- Phan dung chung: thong tin don hang (attribute request "order") cho trang xac nhan va chi tiet don. --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="vi_VN"/>
<div class="card">
    <div><b>Ma don hang:</b> <c:out value="${order.cartId}"/></div>
    <div><b>Ngay dat:</b> <fmt:formatDate value="${order.buyDate}" pattern="dd/MM/yyyy HH:mm"/></div>
    <div><b>Trang thai:</b> Da dat hang - cho giao</div>
    <div><b>Nguoi nhan:</b> <c:out value="${order.receiverName}"/></div>
    <div><b>So dien thoai:</b> <c:out value="${order.receiverPhone}"/></div>
    <div><b>Dia chi giao hang:</b> <c:out value="${order.shippingAddress}"/></div>
    <c:if test="${not empty order.note}"><div><b>Ghi chu:</b> <c:out value="${order.note}"/></div></c:if>
    <div><b>Phuong thuc thanh toan:</b> Thanh toan khi nhan hang (COD)</div>
</div>

<table class="data-table cart-table">
    <tr>
        <th>Anh</th>
        <th>San pham</th>
        <th class="num">Don gia</th>
        <th class="num">So luong</th>
        <th class="num">Thanh tien</th>
    </tr>
    <c:forEach items="${order.items}" var="item">
        <tr>
            <td class="thumb">
                <c:set var="img" value="${item.images}" scope="request"/>
                <jsp:include page="/WEB-INF/jsp/common/product-image.jsp"/>
            </td>
            <td><a href="<c:url value='/product'><c:param name='id' value='${item.productId}'/></c:url>"><c:out value="${item.productName}"/></a></td>
            <td class="num"><fmt:formatNumber value="${item.unitPrice}" pattern="#,##0"/> VND</td>
            <td class="num">${item.quantity}</td>
            <td class="num"><fmt:formatNumber value="${item.lineTotal}" pattern="#,##0"/> VND</td>
        </tr>
    </c:forEach>
    <tr class="total-row">
        <td colspan="4" class="num">Tong tien (thanh toan khi nhan hang):</td>
        <td class="num"><b><fmt:formatNumber value="${order.totalAmount}" pattern="#,##0"/> VND</b></td>
    </tr>
</table>
