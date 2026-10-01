<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Quan ly Nguoi dung</title></head>
<body>
    <div class="toolbar">
        <h2>Quan ly Nguoi dung</h2>
        <a class="btn" href="<c:url value='/admin/users?action=create&page=${pageResult.page}'/>">+ Them nguoi dung</a>
    </div>

    <c:if test="${not empty flashSuccess}"><div class="alert alert-success"><c:out value="${flashSuccess}"/></div></c:if>
    <c:if test="${not empty flashError}"><div class="alert alert-error"><c:out value="${flashError}"/></div></c:if>
    <c:if test="${not empty errorMessage}"><div class="alert alert-error"><c:out value="${errorMessage}"/></div></c:if>

    <table class="data-table">
        <tr>
            <th>ID</th><th>Username</th><th>Email</th><th>Ho ten</th><th>Dien thoai</th>
            <th>Vai tro</th><th>Seller ID</th><th>Trang thai</th><th>Thao tac</th>
        </tr>
        <c:forEach items="${pageResult.items}" var="u">
            <tr>
                <td>${u.userId}</td>
                <td><c:out value="${u.username}"/></td>
                <td><c:out value="${u.email}"/></td>
                <td><c:out value="${u.fullname}"/></td>
                <td><c:out value="${u.phone}"/></td>
                <td><c:out value="${u.roleName}"/></td>
                <td>${u.sellerId}</td>
                <td>${u.status == 1 ? 'Kich hoat' : 'Chua kich hoat'}</td>
                <td class="actions">
                    <a class="btn" href="<c:url value='/admin/users?action=edit&id=${u.userId}&page=${pageResult.page}'/>">Sua</a>
                    <form method="post" action="<c:url value='/admin/users'/>"
                          onsubmit="return confirm('Xoa nguoi dung nay?');">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${u.userId}">
                        <input type="hidden" name="page" value="${pageResult.page}">
                        <button type="submit" class="btn btn-danger">Xoa</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty pageResult.items}">
            <tr><td colspan="9">Chua co nguoi dung.</td></tr>
        </c:if>
    </table>

    <c:set var="baseUrl" value="/admin/users" scope="request"/>
    <jsp:include page="/WEB-INF/jsp/common/pagination.jsp"/>
</body>
</html>
