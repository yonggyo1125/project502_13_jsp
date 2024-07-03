window.addEventListener("DOMContentLoaded", function() {
    const rootUrl = document.querySelector("meta[name='rootUrl']").content;
    const redirectUrl = `${location.pathname}${location.search}`;

    const items = document.getElementsByClassName("my-pokemon");
    for (const el of items) {
        el.addEventListener("click", function() {
            const classList = el.classList;
            if(classList.contains("guest")) { // 비회원으로 클릭한 경우
                // 로그인 페이지로 이동
                location.href= rootUrl + "member/login?redirectUrl=" + redirectUrl;
                return;
            }


        });
    }
});