<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<c:url var="infoUrl" value="/mypage/info" />
<layout:main title="마이페이지">
<a href="${infoUrl}">회원정보 수정</a>
</layout:main>