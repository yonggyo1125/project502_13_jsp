window.addEventListener("DOMContentLoaded", function() {

    ClassicEditor.create(document.getElementById("content"), {
            height: 350
        })
        .then((editor) => {
            window.editor = editor;
        });

    /* 파일 업로드 버튼 클릭 처리 S */
    const fileUploadButtons = document.getElementsByClassName("file-upload");
    // <input type='file'>
    const fileEl = document.createElement("input");
    fileEl.type = 'file';
    fileEl.multiple = true;

    // gid
    const gidEl = document.querySelector("input[name='gId']");
    const gid = gidEl ? gidEl.value : Date.now();

    for (const el of fileUploadButtons) {
        el.addEventListener("click", function() {
            // editor - 에디터에 추가될 이미지, attach - 첨부 파일
            const location = this.classList.contains("editor") ? "editor" : "attach";
            fileEl.location = location;
            fileEl.value = "";
            fileEl.click();
        });
    }
    /* 파일 업로드 버튼 클릭 처리 E */

    /* 파일 탐색기에서 선택 처리 S */
    fileEl.addEventListener("change", function(e) {

        try {
            const files = e.target.files; // 업로드한 파일 목록

            // 에디터에 첨부되는 이미지이므로 형식 체크
            if (fileEl.location == 'editor') {
                for (const file of files) {
                    // 이미지 형식의 파일이 아닌 경우
                    if (file.type.indexOf("image/") == -1) {
                        throw new Error("이미지 형식만 업로드 하세요.");
                    }
                }
            }

            // 파일 업로드
            fileManager.upload(files, gid, fileEl.location);

        } catch (err) {
            alert(err.message);
        }

    });
    /* 파일 탐색기에서 선택 처리 E */
});

/**
* 파일 업로드 후 후속 처리
*
*/
function callbackFileUpload(files) {
    if (files.length == 0) {
        return;
    }

    const targetEditor = document.getElementById("attach-files-editor");
    const targetAttach = document.getElementById("attach-files-attach");

    const editorTpl = document.getElementById("editor-tpl").innerHTML;
    const attachTpl = document.getElementById("attach-tpl").innerHTML;

    const domParser = new DOMParser();

    const source = [];
    for (const file of files) {
        const location = file.location;
        let html, target;
        if (location == 'editor') { // 에디터에 이미지 추가
            source.push(file.fileUrl);
            html = editorTpl;
            target = targetEditor;
        } else { // 파일 첨부
            html = attachTpl;
            target = targetAttach;
        }

        html = html.replace(/\[seq\]/g, file.seq)
                   .replace(/\[fileName\]/g, file.fileName)
                   .replace(/\[fileUrl\]/g, file.fileUrl);

        const dom = domParser.parseFromString(html, "text/html");
        const span = dom.querySelector("span");
        target.appendChild(span);
    }

    // 에디터에 이미지 추가
    if (source.length > 0) {
        editor.execute('insertImage', { source });
    }
}