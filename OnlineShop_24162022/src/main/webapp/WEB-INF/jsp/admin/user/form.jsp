<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="isNew" value="${user.userId == 0}"/>
<html>
<head><title>${isNew ? 'Them nguoi dung' : 'Sua nguoi dung'}</title></head>
<body>
    <div class="card form-card">
        <h2>${isNew ? 'Them nguoi dung' : 'Sua nguoi dung #'}${isNew ? '' : user.userId}</h2>

        <c:if test="${not empty errorMessage}"><div class="alert alert-error"><c:out value="${errorMessage}"/></div></c:if>

        <form method="post" action="<c:url value='/admin/users'/>">
            <input type="hidden" name="action" value="save">
            <input type="hidden" name="userId" value="${user.userId}">
            <input type="hidden" name="page" value="<c:out value='${param.page}'/>">

            <div class="form-group">
                <label>Ten dang nhap *</label>
                <input type="text" name="username" value="<c:out value='${user.username}'/>" required>
            </div>
            <div class="form-group">
                <label>Email *</label>
                <input type="email" name="email" value="<c:out value='${user.email}'/>" required>
            </div>
            <div class="form-group">
                <label>Ho ten *</label>
                <input type="text" name="fullname" value="<c:out value='${user.fullname}'/>" required>
            </div>
            <div class="form-group">
                <label>Mat khau ${isNew ? '*' : '(de trong neu giu nguyen)'}</label>
                <input type="password" name="password" ${isNew ? 'required' : ''}>
            </div>
            <div class="form-group">
                <label>So dien thoai</label>
                <input type="text" name="phone" value="<c:out value='${user.phone}'/>">
            </div>
            <div class="form-group">
                <label>Anh dai dien (URL hoac ten file)</label>
                <input type="text" name="images" value="<c:out value='${user.images}'/>">
            </div>
            <div class="form-group">
                <label>Vai tro</label>
                <select name="roleId">
                    <c:forEach items="${roles}" var="r">
                        <option value="${r.roleId}" ${r.roleId == user.roleId ? 'selected' : ''}><c:out value="${r.roleName}"/></option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label>Cua hang (chi dung cho vai tro Seller)</label>
                <select name="sellerId">
                    <option value="0">-- Khong --</option>
                    <c:forEach items="${sellers}" var="s">
                        <option value="${s.sellerId}" ${s.sellerId == user.sellerId ? 'selected' : ''}>${s.sellerId} - <c:out value="${s.sellername}"/></option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label>Trang thai</label>
                <select name="status">
                    <option value="1" ${user.status == 1 ? 'selected' : ''}>Kich hoat</option>
                    <option value="0" ${user.status == 0 ? 'selected' : ''}>Chua kich hoat</option>
                </select>
            </div>

            <button type="submit" class="btn">Luu</button>
            <a class="btn btn-secondary" href="<c:url value='/admin/users'><c:param name='page' value='${param.page}'/></c:url>">Huy</a>
        </form>
    </div>
</body>
</html>
