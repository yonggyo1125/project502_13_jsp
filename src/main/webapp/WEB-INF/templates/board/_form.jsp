<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="util" tagdir="/WEB-INF/tags/utils" %>
<dl>
    <dt>작성자</dt>
    <dd>
        <input type="text" name="poster" value="${isLogin ? loggedMember.userName : ''}">
        <c:if test="${isAdmin}">
            <input type="checkbox" name="notice" value="true" id="notice">
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

