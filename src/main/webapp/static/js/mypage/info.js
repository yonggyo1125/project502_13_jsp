window.addEventListener("DOMContentLoaded", function() {
    const buttons = document.querySelectorAll("#frmList .button-action");
    for (const el of buttons) {
        el.addEventListener("click", function() {
            const mode = this.dataset.mode;
            const modeStr = mode == 'update' ? '수정' : '삭제';
            if (!confirm(`정말 ${modeStr} 하겠습니까?`)) {
                return;
            }


        });
    }
});

function callbackProfile(data) {
   const { popup } = commonLib;

   const rootUrl = document.querySelector("meta[name='rootUrl']").content;

   const url = `${rootUrl}pokemon/popup/${data.seq}`;
   popup.open(url, 600, 600);
}
