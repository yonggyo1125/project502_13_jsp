<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="util" tagdir="/WEB-INF/tags/utils" %>

<input type="hidden" name="bId" value="${data.BId}">
<input type="hidden" name="gId" value="${data.GId}">

<c:if test="${board.activeCategory == 1}">
    <dl>
        <dt>분류 선택</dt>
        <dd>
            <c:forEach var="category" items="${board.categories}" varStatus="status">

                <input type="radio" name="category" value="${category}" id="category-${status.index}"${data.category != null && category.trim().equals(data.category) ? ' checked':''}>
                <label for="category-${status.index}">${category}</label>
            </c:forEach>
        </dd>
    </dl>
</c:if>

<dl>
    <dt>작성자</dt>
    <dd>
        <c:if test="${data.mode == 'update'}">
            <input type="text" name="poster" value="${data.poster}">
        </c:if>
        <c:if test="${data.mode != 'update'}">
            <input type="text" name="poster" value="${loggedMember != null ? loggedMember.userName : ''}">
        </c:if>
        <c:if test="${isAdmin}">
            <input type="checkbox" name="notice" value="true" id="notice"${data.notice ? ' checked':''}>
            <label for="notice">
                공지글
            </label>
        </c:if>
    </dd>
</dl>
<util:guestOnly>
<dl>
    <dt>비밀번호</dt>
    <dd>
        <input type="password" name="guestPassword" placeholder="글 수정, 삭제 비밀번호">
    </dd>
</dl>
</util:guestOnly>
<dl>
    <dt>제목</dt>
    <dd>
        <input type="text" name="subject" value="${data.subject}">
    </dd>
</dl>
<dl>
    <dt>내용</dt>
    <dd>
        <textarea name="content" id="content">${data.content}</textarea>
    </dd>
</dl>
<dl>
    <dt>이미지 첨부</dt>
    <dd>
        <button type='button' class="file-upload editor">이미지 선택</button>
        <div class='attach-files' id="attach-files-editor"></div>
    </dd>
</dl>
<dl>
    <dt>파일 첨부</dt>
    <dd>
        <button type='button' class='file-upload'>파일 선택</button>
        <div class='attach-files' id="attach-files-attach"></div>
    </dd>
</dl>

<jsp:include page="../commons/_file_tpl.jsp" />
