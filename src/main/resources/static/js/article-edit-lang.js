import { createJsonFromInputs, sendXmlHttpRequest } from "./http-requests.js";
import { removeOldErrors, showFieldErrors } from "./functions.js";

$(document).ready(function () {
    const button = document.getElementById("submit-button");
    button.onclick = function () {
        const inputs = document.querySelectorAll('main input');
        const data = createJsonFromInputs(inputs);
        const json = JSON.stringify(data);

        sendXmlHttpRequest('PATCH', '/admin/articles/edit', json).then(function (res) {
            if (typeof (res) === 'string') {
                window.location = res;
            } else {
                removeOldErrors(['title', 'mainImageName', 'content']);
                showFieldErrors(res)
            }
        }
        );
    };
});

