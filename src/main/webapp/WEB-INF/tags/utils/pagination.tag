<%@ tag body-content="empty" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${pagination != null}">
    <div class='pagination'>
        <c:if test="${pagination.prevRangePage > 0}">
            <a href="<c:url value='${pagination.baseURL}page=1' />">처음</a>
            <a href="<c:url value='${pagination.baseURL}page=${pagination.prevRangePage}' />">이전</a>
        </c:if>
        <c:forEach var="page" items="${pagination.pages}">
            <a class="page${pagination.page == Integer.parseInt(page[0])?' on':''}" href="<c:url value='${page[1]}' />">${page[0]}</a>
        </c:forEach>
        <c:if test="${pagination.nextRangePage > 0}">
            <a href="<c:url value='${pagination.baseURL}page=${pagination.nextRangePage}' />">다음</a>
            <a href="<c:url value='${pagination.baseURL}page=${pagination.totalPages}' />">마지막</a>
        </c:if>
    </div>
</c:if>
