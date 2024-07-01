<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="util" tagdir="/WEB-INF/tags/utils" %>

<ul class="list-items">
<c:if test="${items == null || items.isEmpty()}">
    <li class='no-data'>조회된 게시글이 없습니다.</li>
</c:if>

<c:if test="${items != null && !items.isEmpty()}">
<c:forEach var="item" items="${items}">
    <li>
        <a href="<c:url value='/board/view/${item.seq}' />" class='subject'>
            ${item.subject}
        </a>
        <div class='post-info'>
            ${item.poster}(${item.memberSeq > 0 ? item.email : '비회원'})
            <util:formatDate value='${item.regDt}' pattern='yyyy.MM.dd HH:mm' />
        </div>
    </li>
</c:forEach>
</c:if>
</ul>

<util:pagination />