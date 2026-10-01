<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="isNew" value="${category.categoryId == 0}"/>
<html>
<head><title>${isNew ? 'Them danh muc' : 'Sua danh muc'}</title></head>
<body>
    <div class="card form-card">
        <h2>${isNew ? 'Them danh muc' : 'Sua danh muc #'}${isNew ? '' : category.categoryId}</h2>

        <c:if test="${not empty errorMessage}"><div class="alert alert-error"><c:out value="${errorMessage}"/></div></c:if>

        <form method="post" action="<c:url value='/admin/categories'/>">
            <input type="hidden" name="action" value="save">
            <input type="hidden" name="categoryId" value="${category.categoryId}">
            <input type="hidden" name="page" value="<c:out value='${param.page}'/>">

            <div class="form-group">
                <label>Ten danh muc *</label>
                <input type="text" name="categoryName" value="<c:out value='${category.categoryName}'/>" required>
            </div>
            <div class="form-group">
                <label>Anh (URL hoac ten file trong /assets/images)</label>
                <input type="text" name="images" value="<c:out value='${category.images}'/>">
            </div>
            <div class="form-group">
                <label>Trang thai</label>
                <select name="status">
                    <option value="1" ${category.status == 1 ? 'selected' : ''}>Hien thi</option>
                    <option value="0" ${category.status == 0 ? 'selected' : ''}>An</option>
                </select>
            </div>

            <button type="submit" class="btn">Luu</button>
            <a class="btn btn-secondary" href="<c:url value='/admin/categories'><c:param name='page' value='${param.page}'/></c:url>">Huy</a>
        </form>
    </div>
</body>
</html>
