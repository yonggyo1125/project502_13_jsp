const profile = {
    /**
    * 사이트 contextPath 조회
    *
    */
    getRootUrl() {
        const rootUrl = document.querySelector("meta[name='rootUrl']").content;

        return rootUrl;
    },
    /**
    * 무작위 포켓몬 조회
    *
    */
    random() {
        const rootUrl = profile.getRootUrl();
        const apiUrl = `${rootUrl}api/pokemon/random`;
        console.log(apiUrl);
        fetch(apiUrl)
            .then((res) => res.json())
            .then(data => {
                if (typeof callbackProfile == 'function') {
                    callbackProfile(data);
                }
            });
    }
};


window.addEventListener("DOMContentLoaded", function() {
    const el = document.getElementById("generate-profile-image");
    if (el) {
        el.addEventListener("click", profile.random);
    }
});