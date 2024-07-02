<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>

<layout:main title="포켓몬 카드 선택">
    <div class='content-box'>
        <h1>대전 결과</h1>

        <div class='play-box'>
            <div class='item user'>
                <img src='${user.frontImage}' alt="${item.nameKr}">
                <div class='p-name'>${item.nameKr}</div>
                 <div class="p-point">
                    <fmt:formatNumber value="${user.weight * user.height * user.baseExperience}" />점
                 </div>
            </div>

            <div class='item computer'>
               <img src='${computer.frontImage}' alt="${computer.nameKr}">
               <div class='p-name'>${computer.nameKr}</div>
                <div class="p-point">
                    <fmt:formatNumber value="${computer.weight * computer.height * computer.baseExperience}" />점
                </div>
            </div>
        </div>
        <div>
            대전 결과 : ${result.title}
        </div>
        <a href="<c:url value='/pokemon/game/step1' />">다시 게임하기</a>
    </div>
</layout:main>