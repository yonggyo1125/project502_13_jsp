window.addEventListener("DOMContentLoaded", function() {
    const rootUrl = document.querySelector("meta[name='rootUrl']").content;
    const redirectUrl = `${location.pathname.replace(rootUrl, '/')}${location.search}`;

    const items = document.getElementsByClassName("my-pokemon");
    for (const el of items) {
        el.addEventListener("click", function() {
            const classList = this.classList;
            if(classList.contains("guest")) { // 비회원으로 클릭한 경우
                // 로그인 페이지로 이동
                location.href= rootUrl + "member/login?redirectUrl=" + redirectUrl;
                return;
            }

            // 서버 반영 처리
            const seq = this.dataset.seq;

            fetch(`${rootUrl}api/pokemon/my/${seq}`)
                .then(res => res.text())
                .then(text => {
                    if (text == 'true') {
                        classList.toggle('on');
                    }
                });

        });
    }
});