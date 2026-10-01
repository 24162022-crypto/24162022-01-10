<%-- Thanh phan trang dung chung (Cau 5). Can 2 request attribute:
     pageResult (PageResult_24162022) va baseUrl (VD: /admin/users) --%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${pageResult.totalPages > 1}">
    <div class="pagination">
        <c:choose>
            <c:when test="${pageResult.page > 1}">
                <a href="<c:url value='${baseUrl}'><c:param name='page' value='${pageResult.page - 1}'/></c:url>">&laquo; Truoc</a>
            </c:when>
            <c:otherwise><span class="disabled">&laquo; Truoc</span></c:otherwise>
        </c:choose>

        <c:forEach begin="1" end="${pageResult.totalPages}" var="i">
            <c:choose>
                <c:when test="${i == pageResult.page}"><span class="active">${i}</span></c:when>
                <c:otherwise>
                    <a href="<c:url value='${baseUrl}'><c:param name='page' value='${i}'/></c:url>">${i}</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>

        <c:choose>
            <c:when test="${pageResult.page < pageResult.totalPages}">
                <a href="<c:url value='${baseUrl}'><c:param name='page' value='${pageResult.page + 1}'/></c:url>">Sau &raquo;</a>
            </c:when>
            <c:otherwise><span class="disabled">Sau &raquo;</span></c:otherwise>
        </c:choose>
    </div>
</c:if>
<p class="muted">Tong so: ${pageResult.totalItems} ban ghi - Trang ${pageResult.page}/${pageResult.totalPages}</p>
