<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="util" tagdir="/WEB-INF/tags/utils" %>
<c:url var="searchUrl" value="/pokemon/game/log" />
<layout:main title="포켓몬 게임 기록">
    <div class='content-box'>
        <form name="frmSearch" method="get" action="${actionUrl}" autocomplete="off">
            <input type="text" name="skey" value="${param.skey}">
            <button type="submit">검색</button>
        </form>
        <table>
            <thead>
                <tr>
                    <th colspan="2">플레이어</th>
                    <th>컴퓨터</th>
                    <th>대전결과</th>
                    <th>대전일시</th>
                </tr>
            </thead>
            <tbody>
            <c:forEach var="item" items="${items}">
                <tr>
                    <td>
                        ${item.userName}
                        <div>(${item.email})</div>
                    </td>
                    <td>
                        <a href="<c:url value='/pokemon/${item.userSeq}' />">
                            <img src="${item.userPimg}" width='50'>
                        </a>
                        <div>${item.userPname}</div>
                        <div>
                            <fmt:formatNumber value="${item.userScore}" />
                        </div>
                    </td>
                    <td>
                        <a href="<c:url value='/pokemon/${item.comSeq}' />">
                            <img src="${item.comPimg}" width='50'>
                        </a>
                        <div>${item.comPname}</div>
                        <div>
                            <fmt:formatNumber value="${item.comScore}" />
                        </div>
                    </td>
                    <td>${item.gameResult.title}</td>
                    <td>
                        <util:formatDate value="${item.regDt}" pattern="yyyy.MM.dd HH:mm" />
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <util:pagination />
    </div>
</layout:main>