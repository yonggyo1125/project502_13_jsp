<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<h2>기본 설정</h2>
<table class="table-cols">
    <tr>
        <th width='150'>게시판 ID</th>
        <td>
            <c:if test="${data.mode == 'update'}">
                ${data.BId}
                <input type="hidden" name="bId" value="${data.BId}">
            </c:if>
            <c:if test="${data.mode != 'update'}">
                <input type="text" name="bId">
            </c:if>
        </td>
    </tr>
    <tr>
        <th>게시판 이름</th>
        <td>
            <input type="text" name="bName" value="${data.BName}">
        </td>
    </tr>
    <tr>
        <th>1페이지 행수</th>
        <td>
            <input type="number" name="rowsPerPage" min="1" value="${data.rowsPerPage}">
        </td>
    </tr>
    <tr>
        <th>사용여부</th>
        <td>
            <input type="radio" name="active" value="true" id="active_true"${data.active ? ' checked':''}>
            <label for="active_true">사용</label>

            <input type="radio" name="active" value="false" id="active_false"${data.active? '' : ' checked'}>
            <label for="active_false">미사용</label>
        </td>
    </tr>
</table>

<h2>분류 설정</h2>
<table class="table-cols">
    <tr>
        <th width="150">사용여부</th>
        <td>
            <input type="radio" name="activeCategory" value="true" id="activeCategory_true"${data.activeCategory?' checked':''}>
            <label for="activeCategory_true">사용</label>

            <input type="radio" name="activeCategory" value="false" id="activeCategory_false"${data.activeCategory?'':' checked'}>
            <label for="activeCategory_false">미사용</label>
        </td>
    </tr>
    <tr>
        <th>분류</th>
        <td>
            <textarea name="category">${data.category}</textarea>
            <div class="message">분류가 여러개이면 줄개행을 통해 입력</div>
        </td>
    </tr>
</table>

<h2>권한 설정</h2>
<table class="table-cols">
    <tr>
        <th width='150'>글쓰기/글수정</th>
        <td>
            <input type="radio" name="authority" value="ALL" id="authority_ALL"${data.authority == 'ALL' ? ' checked':''}>
            <label for="authority_ALL">
                비회원 + 회원 + 관리자
            </label>

            <input type="radio" name="authority" value="USER" id="authority_USER"${data.authority == 'USER' ? ' checked':''}>
            <label for="authority_USER">
                회원 + 관리자
            </label>

            <input type="radio" name="authority" value="ADMIN" id="authority_ADMIN"${data.authority == 'ADMIN' ? ' checked':''}>
            <label for="authority_ADMIN">
                관리자
            </label>
        </td>
    </tr>
</table>