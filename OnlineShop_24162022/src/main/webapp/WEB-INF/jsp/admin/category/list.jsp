<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Quan ly Danh muc</title></head>
<body>
    <div class="toolbar">
        <h2>Quan ly Danh muc</h2>
        <a class="btn" href="<c:url value='/admin/categories?action=create&page=${pageResult.page}'/>">+ Them danh muc</a>
    </div>

    <c:if test="${not empty flashSuccess}"><div class="alert alert-success"><c:out value="${flashSuccess}"/></div></c:if>
    <c:if test="${not empty flashError}"><div class="alert alert-error"><c:out value="${flashError}"/></div></c:if>
    <c:if test="${not empty errorMessage}"><div class="alert alert-error"><c:out value="${errorMessage}"/></div></c:if>

    <table class="data-table">
        <tr><th>ID</th><th>Anh</th><th>Ten danh muc</th><th>Trang thai</th><th>Thao tac</th></tr>
        <c:forEach items="${pageResult.items}" var="cat">
            <tr>
                <td>${cat.categoryId}</td>
                <td class="thumb">
                    <c:set var="img" value="${cat.images}" scope="request"/>
                    <jsp:include page="/WEB-INF/jsp/common/product-image.jsp"/>
                </td>
                <td><c:out value="${cat.categoryName}"/></td>
                <td>${cat.status == 1 ? 'Hien thi' : 'An'}</td>
                <td class="actions">
                    <a class="btn" href="<c:url value='/admin/categories?action=edit&id=${cat.categoryId}&page=${pageResult.page}'/>">Sua</a>
                    <form method="post" action="<c:url value='/admin/categories'/>"
                          onsubmit="return confirm('Xoa danh muc nay?');">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${cat.categoryId}">
                        <input type="hidden" name="page" value="${pageResult.page}">
                        <button type="submit" class="btn btn-danger">Xoa</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty pageResult.items}">
            <tr><td colspan="5">Chua co danh muc.</td></tr>
        </c:if>
    </table>

    <c:set var="baseUrl" value="/admin/categories" scope="request"/>
    <jsp:include page="/WEB-INF/jsp/common/pagination.jsp"/>
</body>
</html>
