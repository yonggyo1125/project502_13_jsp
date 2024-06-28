<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:url var="mainUrl" value="/" />
<c:url var="listUrl" value="/board/list/${board.BId}" />
<div class="location">
    <a href="${mainUrl}">HOME</a>
    &gt;
    <a href="${listUrl}">${board.BName}</a>
</div>

<h1>${board.BName}</h1>