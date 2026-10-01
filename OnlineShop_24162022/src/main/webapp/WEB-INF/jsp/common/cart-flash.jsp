<%-- Hien thong bao flash cua gio hang/dat hang (luu trong Session) dung 1 lan roi xoa. --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${not empty sessionScope.cartFlashSuccess}">
    <div class="alert alert-success"><c:out value="${sessionScope.cartFlashSuccess}"/></div>
    <c:remove var="cartFlashSuccess" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.cartFlashError}">
    <div class="alert alert-error"><c:out value="${sessionScope.cartFlashError}"/></div>
    <c:remove var="cartFlashError" scope="session"/>
</c:if>
