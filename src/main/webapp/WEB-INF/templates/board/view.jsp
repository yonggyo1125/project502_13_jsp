<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="util" tagdir="/WEB-INF/tags/utils" %>
<layout:main>
    <section class="layout-width">
        <jsp:include page="_header.jsp" />

        <div class='subject'>
            <c:if test="${! empty data.category}">
                [${data.category}]
            </c:if>
            ${data.subject}
        </div>
        <div class='post-info'>
            <div class='left'>
                ${data.poster}
                (${data.memberSeq > 0 ? data.email : '비회원'})
            </div>
            <div class='right'>
                IP: ${data.ip} /
                DATE: <util:formatDate value="${data.regDt}" pattern="yyyy.MM.dd HH:mm" />
            </div>
        </div>
        <div class='content'>
            ${data.content}
        </div>
        <div class='links'>
            <a href="<c:url value='/board/list/${data.BId}' />">글목록</a>
            <a href="<c:url value='/board/write/${data.BId}' />">글쓰기</a>
            <a href="<c:url value='/board/update/${data.seq}' />">글수정</a>
            <a href="<c:url value='/board/delete/${data.seq}' />" onclick="return alert('정말 삭제하시겠습니까?');">글삭제</a>
        </div>
    </section>

    <c:if test="${items != null && !items.isEmpty()}">
        <section class="layout-width">
            <jsp:include page="_list.jsp" />
        </section>
    </c:if>
</layout:main>