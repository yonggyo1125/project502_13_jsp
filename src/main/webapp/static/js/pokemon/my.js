window.addEventListener("DOMContentLoaded", function() {
    const rootUrl = document.querySelector("meta[name='rootUrl']").content;
    const redirectUrl = `${location.pathName}${location.search}`;

    const items = document.getElementsByClassName("my-pokemon");
    for (const el of items) {
        el.addEventListener("click", function() {

        });
    }
});