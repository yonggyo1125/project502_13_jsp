<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<c:url var="actionUrl" value="/mypage/info" />

<layout:main title="회원정보 수정">
    <section class="layout-width">
        <form name="frmSave" method="POST" action="${actionUrl}" target="ifrmProcess" autocomplete="off">
            <dl>
                <dt>이메일</dt>
                <dd>${loggedMember.email}</dd>
            </dl>
            <dl>
                <dt>회원명</dt>
                <dd>
                    <input type="text" name="userName" value="${loggedMember.userName}">
                </dd>
            </dl>
            <dl>
                <dt>비밀번호</dt>
                <dd>
                    <input type="password" name="password">
                </dd>
            </dl>
            <dl>
                <dt>비밀번호 확인</dt>
                <dd>
                    <input type="password" name="confirmPassword">
                </dd>
            </dl>
            <dl>
                <dt>프로필 이미지</dt>
                <dd>
                    <c:if test="${myProfile != null}">
                       <div class='profile'>
                            <img src="${myProfile.frontImage}" alt="${myProfile.nameKr}">
                            <div>${myProfile.nameKr}</div>
                       </div>
                    </c:if>
                    <button type='button' id="generate-profile-image">
                    랜덤 프로필 이미지
                    </button>
                </dd>
            </dl>
            <div class='button-group'>
                <button type="reset">다시입력</button>
                <button type="submit">수정하기</button>
            </div>
        </form>
    </section>
</layout:main>