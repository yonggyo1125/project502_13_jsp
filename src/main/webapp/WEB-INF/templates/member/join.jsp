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
        <form name="frmJoin" method="POST" action="${actionUrl}" autocomplete="off" target="ifrmProcess">
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
            <dl>
                <dt>
                    <fmt:message key="회원명" />
                </dt>
                <dd>
                    <input type="text" name="userName">
                </dd>
            </dl>
            <div class="terms">
                <div class="tit">
                    <fmt:message key="약관_동의" />
                </div>
                <div class="termsContent">약관 내용...</div>

                <input type="checkbox" name="termsAgree" value="true" id="termsAgree">
                <label for="termsAgree">
                    <fmt:message key="약관에_동의합니다." />
                </label>
            </div>
            <div class="button-group">
                <button type="reset">
                    <fmt:message key="다시입력" />
                </button>
                <button type="submit">
                    <fmt:message key="가입하기" />
                </button>
            </div>
        </form>
    </section>
</layout:main>