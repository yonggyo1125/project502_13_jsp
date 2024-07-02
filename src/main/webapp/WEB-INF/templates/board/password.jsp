<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<layout:main title="게시글 비밀번호 확인">
    <section class="content-box">
        <h1>비밀번호 확인</h1>

        <form name="frmCheck" method="POST" action="<c:url value='/board/password' />" target="ifrmProcess" autocomplete="off">
            <input type="hidden" name="seq" value="${seq}">
            <input type="password" name="password">
            <button type="submit">확인하기</button>
        </form>
    </section>
</layout:main>