<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<h2>기본 설정<h2>
<table class="table-cols">
    <tr>
        <th width='150'>게시판 ID</th>
        <td>
            <input type="text" name="bId">
        </td>
    </tr>
    <tr>
        <th>게시판 이름</th>
        <td>
            <input type="text" name="bName">
        </td>
    </tr>
    <tr>
        <th>1페이지 행수</th>
        <td>
            <input type="number" name="rowsPerPage" min="1">
        </td>
    </tr>
</table>