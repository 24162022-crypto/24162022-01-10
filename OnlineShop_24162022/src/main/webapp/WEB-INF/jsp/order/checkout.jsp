<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="vi_VN"/>
<html>
<head><title>Thanh toan</title></head>
<body>
    <h2>Thanh toan don hang</h2>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-error"><c:out value="${errorMessage}"/></div>
    </c:if>

    <c:if test="${not empty cart}">
        <h3>Tom tat gio hang</h3>
        <table class="data-table cart-table">
            <tr>
                <th>San pham</th>
                <th class="num">Don gia</th>
                <th class="num">So luong</th>
                <th class="num">Thanh tien</th>
            </tr>
            <c:forEach items="${cart.items}" var="item">
                <tr>
                    <td>
                        <c:out value="${item.productName}"/>
                        <c:if test="${not item.available}"><div class="cart-warn">San pham khong con du hang hoac ngung ban - hay quay lai gio hang de chinh sua.</div></c:if>
                    </td>
                    <td class="num"><fmt:formatNumber value="${item.unitPrice}" pattern="#,##0"/> VND</td>
                    <td class="num">${item.quantity}</td>
                    <td class="num"><fmt:formatNumber value="${item.lineTotal}" pattern="#,##0"/> VND</td>
                </tr>
            </c:forEach>
            <tr class="total-row">
                <td colspan="3" class="num">Tong tien (${cart.items.size()} mat hang):</td>
                <td class="num"><b><fmt:formatNumber value="${cart.total}" pattern="#,##0"/> VND</b></td>
            </tr>
        </table>

        <div class="card form-card">
            <h3>Thong tin nhan hang</h3>
            <form method="post" action="<c:url value='/checkout'/>">
                <input type="hidden" name="cartId" value="<c:out value='${cart.cartId}'/>">

                <div class="form-group">
                    <label>Ho ten nguoi nhan *</label>
                    <input type="text" name="receiverName" maxlength="100" required value="<c:out value='${receiverName}'/>">
                </div>
                <div class="form-group">
                    <label>So dien thoai *</label>
                    <input type="text" name="receiverPhone" maxlength="12" required
                           pattern="^(0|\+84)\d{9}$" title="Vi du: 0912345678 hoac +84912345678"
                           value="<c:out value='${receiverPhone}'/>">
                </div>
                <div class="form-group">
                    <label>Dia chi giao hang *</label>
                    <input type="text" name="shippingAddress" maxlength="300" required value="<c:out value='${shippingAddress}'/>">
                </div>
                <div class="form-group">
                    <label>Ghi chu (tuy chon)</label>
                    <input type="text" name="note" maxlength="300" value="<c:out value='${note}'/>">
                </div>

                <div class="form-group">
                    <label>Phuong thuc thanh toan</label>
                    <label class="radio-row"><input type="radio" name="paymentMethod" value="COD" checked> Thanh toan khi nhan hang (COD)</label>
                    <label class="radio-row disabled"><input type="radio" name="paymentMethod" value="BANK" disabled> Chuyen khoan ngan hang (sap co)</label>
                    <label class="radio-row disabled"><input type="radio" name="paymentMethod" value="EWALLET" disabled> Vi dien tu (sap co)</label>
                </div>

                <a class="btn btn-secondary" href="<c:url value='/cart'/>">&laquo; Quay lai gio hang</a>
                <button type="submit" class="btn btn-checkout">Dat hang</button>
            </form>
        </div>
    </c:if>

    <c:if test="${empty cart}">
        <a class="btn btn-secondary" href="<c:url value='/cart'/>">&laquo; Quay lai gio hang</a>
    </c:if>
</body>
</html>
