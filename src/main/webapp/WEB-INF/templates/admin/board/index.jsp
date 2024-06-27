<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<layout:admin title="게시판 목록">
<h1>게시판 목록</h1>

<table class="table-rows">
    <thead>
        <tr>
            <th width='180'>게시판 ID</th>
            <th>게시판 이름</th>
            <th width='250'></th>
        </tr>
    </thead>
    <tbody>
        <c:if test="${items == null || items.isEmpty()}">
            <tr>
                <td colspan='3' class='no-data'>등록된 게시판이 없습니다.</td>
            </tr>
        </c:if>
        <c:if test="${items != null && !items.isEmpty()}">
            <c:forEach var="item" items="${items}">
                <tr>
                    <td>${item.BId}</td>
                    <td>${item.BName}</td>
                    <td>
                        <a href="<c:url value='/admin/board/update/${item.BId}' />">
                            수정하기
                        </a>
                        <a href="<c:url value='/board/list/${item.BId}' />" target="_blank">
                            게시글 목록
                        </a>
                    </td>
                </tr>
            </c:forEach>
        </c:if>
    </tbody>
</table>
</layout:admin>