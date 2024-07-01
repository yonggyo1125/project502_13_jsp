<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<c:url var="searchUrl" value='/board/list/${board.BId}' />

<layout:main title="${board.BName}">
<section class="layout-width">
    <jsp:include page="_header.jsp" />
    <div class='btns'>
        <a href="<c:url value='/board/write/${board.BId}' />">글쓰기</a>
    </div>
    <jsp:include page="_list.jsp" />

    <div class="search-box">
        <form name='frmSearch' method='get' autocomplate='off'>
            <select name='sopt'>
                <option value='ALL'${param.sopt == 'ALL' || empty param.sopt ? ' selected':''}>통합검색</option>
                <option value='SUBJECT'${param.sopt == 'SUBJECT' ? ' selected':''}>제목</option>
                <option value='CONTENT'${param.sopt == 'CONTENT' ? ' selected':''}>내용</option>
                <option value='SUBJECT_CONTENT'${param.sopt == 'SUBJECT_CONTENT' ? ' selected':''}>제목+내용</option>
                <option value='NAME'${param.sopt == 'NAME' ? ' selected':''}>이름</option>
            </select>
            <input type="text" name="skey" value="${param.skey}" placeholder="검색어를 입력하세요.">
            <button type='submit'>검색하기</button>
        </form>
    </div>
</section>
</layout:main>
