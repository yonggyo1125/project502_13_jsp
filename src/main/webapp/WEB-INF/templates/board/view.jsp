<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
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
                IP: ${data.ip} / DATE: ${data.regDt}
            </div>
        </div>
        <div class='content'>
            ${data.content}
        </div>
    </section>
</layout:main>