<%@ tag body-content="scriptless" %>
<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="util" tagdir="/WEB-INF/tags/utils" %>
<%@ attribute name="title" %>
<fmt:setBundle basename="messages.commons" />
<c:url var="cssUrl" value="/css/" />
<c:url var="jsUrl" value="/js/" />
<c:url var="homeUrl" value="/admin" />
<c:url var="logoUrl" value="/images/logo.png" />

<layout:common title="${title}">
    <jsp:attribute name="header">
        <div class="left">
            <a href="${homeUrl}">
                <img src="${logoUrl}" alt="로고">
            </a>
        </div>
        <div class="right">

        </div>
    </jsp:attribute>
    <jsp:attribute name="commonCss">
        <link rel="stylesheet" type="text/css" href="${cssUrl}admin/style.css">
    </jsp:attribute>
    <jsp:attribute name="commonJs">
        <script src="${jsUrl}admin/common.js"></script>
    </jsp:attribute>
    <jsp:body>
        <!-- 주 메뉴 -->
        <aside class="side-menu">
            <a href="${homeUrl}/member" class="menu${menuCode == 'member' ? ' on':''}">회원관리</a>
            <a href="${homeUrl}/board" class="menu${menuCode == 'board' ? ' on':''}">게시판 관리</a>
        </aside>
        <!-- 내용 영역 -->
        <section class="main-content">
            <c:if test="${subMenus != null && !subMenus.isEmpty()}">
            <nav class="sub-menu">
                <c:forEach var="menu" items="${subMenus}">
                    <a href="<c:url value='${menu[1]}' />">
                        ${menu[0]}
                    </a>
                </c:forEach>
            </nav>
            </c:if>

            <jsp:doBody />
        </section>
    </jsp:body>

</layout:common>