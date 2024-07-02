<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${items != null && !items.isEmpty()}">
<form name="frmList" method="POST" action="<c:url value='/pokemon/popup' />" target="ifrmProcess">
<ul>
<c:forEach var="item" items="${items}">
    <li>
        <input type="radio" name="seq" value="${item.seq}">
        <img src="${item.frontImage}" alt="${item.nameKr}">
        <div>${item.nameKr}</div>
    </li>
</c:forEach>
</ul>
<button type="submit" onclick="return confirm('정말 변경 하시겠습니까?');">프로필 이미지 변경</button>
</form>
</c:if>