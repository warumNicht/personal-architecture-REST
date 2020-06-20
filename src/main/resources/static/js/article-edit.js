import {sendXmlHttpRequest} from "./http-requests.js";
import {getLocale} from "./fetch-functions.js";
import {showAllCategories} from "./functions.js";

window.getArticleId = function () {
    const urlParts = window.location.pathname.split('/');
    return urlParts[urlParts.length - 1];
};

$(document).ready(function () {
    const catChangeButton = $('#categoryChange');

    catChangeButton.click(function () {
        const selectedCatId = $('#cat option:selected').val();
        console.log(selectedCatId);

        const credentials = {
            header: csrf.headerName,
            content: csrf.token
        };

        sendXmlHttpRequest('PATCH', `/admin/articles/change-category/${getArticleId()}`, selectedCatId, credentials).then(res => {
            const responseDiv = $('#message');
            responseDiv.append(`Category for article: ${res.title} successfully changed from ${res.oldCategoryName} to ${res.newCategoryName}!`);
            catChangeButton.attr('disabled', true);
            setTimeout(function () {
                responseDiv.empty();
            }, 4000);
        }).catch(error => {
            console.log(error)
        });
    })


});


window.showImages = function showImages() {
    const imageDiv = $("#image-container");
    const imageButton = $("#show-hide-images");
    if (imageDiv.html()) {
        if (imageButton[0].innerText === 'Show images') {
            imageDiv.show();
            imageButton[0].innerText = 'Hide images';
        } else {
            imageDiv.hide();
            imageButton[0].innerText = 'Show images';
        }
        return;
    }
    imageButton.text('Hide images');
    const urlParts = window.location.pathname.split('/');
    const articleId = getArticleId();
    sendXmlHttpRequest('GET', '/fetch/images/' + articleId).then(function (res) {
            if (res.length === 0) {
                imageDiv.append('<p>No images</p>');
            }
            res.forEach((image) => {
                const currentImageDiv = $('<div></div>');
                currentImageDiv.append(`<img src="${image.url}">`);
                const names = image.localImageNames;
                for (var key in names) {
                    currentImageDiv.append(`<div><span>${key}</span> ${names[key]}</div>`);
                }
                currentImageDiv.append(`<button class="btn btn-info" onclick="editImage(${image.id})">Edit</button>`)
                imageDiv.append(currentImageDiv);
            });
        }
    );
};

window.editImage = function (id) {
    const locale = getLocale(location.href);
    window.location = `${locale}admin/images/edit/${id}`;
};

window.showAllCategories = showAllCategories;

