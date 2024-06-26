<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="util" tagdir="/WEB-INF/tags/utils" %>
<layout:main>
    <h1>${status} 접근 권한이 없습니다.</h1>
    <h2>${method} ${requestUrl}</h2>

    <util:memberOnly>
        <a href="<c:url value='/' />">확인</a>
    </util:memberOnly>
    <util:guestOnly>
        <a href="<c:url value='/member/login' />">로그인</a>
    </util:guestOnly>


</layout:main>