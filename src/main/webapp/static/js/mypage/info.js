function callbackProfile(data) {
   const { popup } = commonLib;

   const rootUrl = document.querySelector("meta[name='rootUrl']").content;

   const url = `${rootUrl}pokemon/popup/${data.seq}`;
   popup.open(url, 600, 600);
}