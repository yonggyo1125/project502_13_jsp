<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="util" tagdir="/WEB-INF/tags/utils" %>
<layout:main>
    <section class="layout-width">
        <div class='subject'>
            ${data.subject}
        </div>
        <div class='post-info'>
            <div class='left'>
                ${data.poster}
                (${data.memberSeq > 0 ? data.email : '비회원'})
            </div>
            <div class='right'>
                IP: ${data.ip} /
                DATE: <util:formatDate value="${data.regDt}" pattern="yyyy.MM.dd HH:mm" />
            </div>
        </div>
        <div class='content'>
            ${data.content}
        </div>
    </section>
</layout:main>