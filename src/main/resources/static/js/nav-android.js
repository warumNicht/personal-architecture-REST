function sendXmlHttpRequest(method, url, data, token) {
    return new Promise(function (resolve, reject) {
        try {
            const req = new XMLHttpRequest();
            req.open(method, url, true);
            req.setRequestHeader('Content-type', 'application/json; charset=utf-8');
            if (token) {
                req.setRequestHeader(token.header, token.content);
            }
            req.onreadystatechange = function () {
                if (this.readyState === 4) {
                    const jsonResult = JSON.parse(this.response);
                    if (jsonResult.error) {
                        reject(jsonResult);
                    }
                    resolve(jsonResult);
                }
            };
            req.send(data);
        } catch (e) {
            reject(e);
        }
    });
}


function fetchCategoriesDropdown(selectElement) {
    const urlParts = window.location.toString().split('/');
    const categoryId = urlParts[urlParts.length - 1];

    sendXmlHttpRequest('GET', '/fetch/categories/all').then(function (res) {
        console.log(res);
        res.forEach(function (category) {
            const isSelected = categoryId == category.id ? 'selected ' : '';
            const href = getLocale(window.location.href) + 'projects/category/' + category.id;
            selectElement.append('<a href="' + href + '" ' + (isSelected ? 'class="selected-item"' : '') + '>' + category.name + '</a>');
        });

    }).catch(function (error) {
        console.log(error);
    });
}

function getLocale(url) {
    const regex = /^.*\/(fr|en|bg|es|de)\//g;
    const found = url.match(regex);
    return found[0];
}


//test for touch events support and if not supported, attach .no-touch class to the HTML tag.

if (!("ontouchstart" in document.documentElement)) {
    document.documentElement.className += " no-touch";
}

$('ul.navbar-nav li.dropdown').hover(function () {
    $(this).find(".dropdown-menu").stop(true, true).delay(200).fadeIn(500);
}, function () {
    $(this).find(".dropdown-menu").stop(true, true).delay(200).fadeOut(500);
});

$('ul.navbar-nav').find('.dropdown').click(function (e) {
    e.stopPropagation();
});

$(document).ready(function () {

    document.querySelectorAll('.dropdown').forEach(function (dropdown) {
        const currentLang = dropdown.querySelector('.dropdown-icon');
        const content = dropdown.querySelector('.dropdown-content');
        if (currentLang) {
            currentLang.addEventListener('click', function (event) {
                console.log(event);
                dropdown.classList.toggle('expanded-dropdown');
            })
        }
    });


    const selectDropdown = $('#select-categories2');
    fetchCategoriesDropdown(selectDropdown);

});




