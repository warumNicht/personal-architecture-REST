import {sendXmlHttpRequest} from "./http-requests.js";

function fetchCategories(selectElement) {
    let urlParts = window.location.toString().split('/');
    let categoryId = urlParts[urlParts.length - 1];

    sendXmlHttpRequest('GET', '/fetch/categories/all').then(function (res) {
        res.forEach(function (category) {
            const isSelected = categoryId == category.id ? 'selected ' : '';
            selectElement.append('<option ' + isSelected + 'value="' + category.id + '">' + category.name + '</option>');
        });
        if (window.showAllCategories) {
            showAllCategories();
        }
    }).catch(function (error) {
        console.log(error);
    });
}

function fetchCategoriesDropdown(selectElement) {
    let urlParts = window.location.toString().split('/');
    let categoryId = urlParts[urlParts.length - 1];

    sendXmlHttpRequest('GET', '/fetch/categories/all').then(function (res) {
        res.forEach(function (category) {
            const isSelected = categoryId == category.id ? 'selected ' : '';
            const href = getLocale(window.location.href) + 'projects/category/' + category.id;
            const content = '<div class="option-holder">' + category.name + '</div>';
            selectElement.append('<a href="' + href + '" ' + (isSelected ? 'class="selected-item"' : '') + '>' + content + '</a>');
        });
        if (window.showAllCategories) {
           showAllCategories();
        }

    }).catch(function (error) {
        console.log(error);
    });
}

function getLocale(url) {
    const regex = /^.*\/(fr|en|bg|es|de)\//g;
    const found = url.match(regex);
    return found[0];
}

export {fetchCategories, getLocale, fetchCategoriesDropdown}