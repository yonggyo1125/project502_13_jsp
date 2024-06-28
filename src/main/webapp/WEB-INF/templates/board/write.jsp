<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<c:url var="listLink" value="/board/list/${board.BId}" />
<layout:main title="${board.BName}">
    <h1>
        <a href="${listLink}">
            ${board.BName}
        </a>
    </h1>

</layout:main>