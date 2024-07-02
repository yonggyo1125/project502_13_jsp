<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<layout:main title="포켓몬 카드 선택">
    <div class='content-box'>
        <h1>포켓몬 카드 선택</h1>

        <form name="frmStep1" method="POST" action="<c:url value='/pokemon/game/step2' />" autocomplete="off">
            <ul class='card-items'>
                <c:forEach var="item" items="${items}">
                    <li class='item'>
                        <input type="radio" name="seq" value="${item.seq}" onclick="frmStep1.submit();" id="seq_${item.seq}">
                        <label for="seq_${item.seq}">
                            <img src="${item.frontImage}" alt="${item.nameKr}">
                            <div class="p-name">${item.nameKr}</div>
                        </label>
                    </li>
                </c:forEach>
            </ul>
       </form>
    </div>
</layout:main>