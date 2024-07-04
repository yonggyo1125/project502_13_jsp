<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:url var="downloadUrl" value="/file/download" />
<script type="text/html" id="attach-tpl">
    <span class="file-item">
        <a href="${downloadUrl}/[seq]">
            [fileName]
        </a>
        <i class="xi-close remove" data-seq=[seq]></i>
    </span>
</script>

<script type="text/html" id="editor-tpl">
    <span class="file-item">
        <a href="${downloadUrl}/[seq]">
            [fileName]
        </a>
        <i class="xi-file-upload insert-editor" data-url="[fileUrl]"></i>
        <i class="xi-close remove" data-seq=[seq]></i>
    </span>
</script>
