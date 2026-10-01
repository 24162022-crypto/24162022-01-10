<%-- Hien thi anh san pham: images la URL day du (http...) hoac ten file trong /assets/images/.
     Dung: <c:set var="img" value="${p.images}" scope="request"/> roi include file nay. --%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:choose>
    <c:when test="${empty img}"><c:url var="imgSrc" value="/assets/images/no-image.svg"/></c:when>
    <c:when test="${fn:startsWith(img, 'http')}"><c:set var="imgSrc" value="${img}"/></c:when>
    <c:otherwise><c:url var="imgSrc" value="/assets/images/${img}"/></c:otherwise>
</c:choose>
<img src="${fn:escapeXml(imgSrc)}" alt="" class="product-img">
