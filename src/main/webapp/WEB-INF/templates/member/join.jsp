<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<fmt:setBundle basename="messages.commons" />
<fmt:message var="pageTitle" key='회원가입' />
<c:url var="actionUrl" value="/member/join" />

<layout:main title="${pageTitle}">
    <section class="content-box">
        <h1>${pageTitle}</h1>
        <form name="frmJoin" method="POST" action="${actionUrl}" autocomplete="off">
            <dl>
                <dt>
                    <fmt:message key="이메일" />
                </dt>
                <dd>
                    <input type="text" name="email">
                </dd>
            </dl>
            <dl>
                <dt>
                    <fmt:message key="비밀번호" />
                </dt>
                <dd>
                    <input type="password" name="password">
                </dd>
            </dl>
            <dl>
                <dt>
                    <fmt:message key="비밀번호_확인" />
                </dt>
                <dd>
                    <input type="password" name="confirmPassword">
                </dd>
            </dl>
        </form>
    </section>
</layout:main>