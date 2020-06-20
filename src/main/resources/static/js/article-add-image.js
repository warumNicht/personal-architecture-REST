import { sendXmlHttpRequest } from "./http-requests.js";
import { removeOldErrors, showFieldErrors } from "./functions.js";

$(document).ready(function () {
    const button = document.getElementById("submit-button");
    button.onclick = function () {
        const selectLang = document.getElementById('lang');
        const lang = selectLang.options[selectLang.selectedIndex].value;
        const data = {
            id: article.id,
            lang: lang,
            image: {
                url: document.getElementById('url').value,
                name: document.getElementById('name').value
            }
        };
        const credentials = {
            header: tokenHeader,
            content: token
        };
        const json = JSON.stringify(data);
        sendXmlHttpRequest('PUT', location.href, json, credentials).then(function (res) {

            if (typeof (res) === 'string') {
                window.location = res;
            } else {
                removeOldErrors(['image.url', 'image.name']);
                showFieldErrors(res)
            }
        }
        );
    };
});