function showFieldErrors(errorMap) {
    for (const [key, value] of Object.entries(errorMap)) {
        const currentFieldDiv = $(`#${key.replace('.', '\\.')}Div`);
        console.log(`#${key}Div`);
        console.log(currentFieldDiv);
        currentFieldDiv.addClass('text-danger');
        const currentSmall = $('<small></small>').addClass('text-danger');

        value.forEach((v) => {
            currentSmall.append(`${v}<br>`)
        });
        currentFieldDiv.append(currentSmall);
    }
}

function removeOldErrors(fieldsNames) {
    fieldsNames.forEach((name) => {
        $(`#${name.replace('.', '\\.')}Div`).removeClass('text-danger');
    });
    $('small').remove('.text-danger');
}

function showAllCategories() {
    const categoriesSelect = $('#categories-dropdown a');
    const dropdownToCopy = $('#cat');

    categoriesSelect.each(function (index) {
        if(index!==0){
            const href = $(this).attr('href');
            const value = getCategoryId(href);
            const innerText = $(this).text();
            dropdownToCopy.append(`<option ${index===1 ? 'selected' : ''} value="${value}">${innerText}</option>`);
        }
    });
    categoriesSelect.change(function () {
        $('#categoryChange').removeAttr('disabled');
    });
}

function getCategoryId(url) {
    const regex = /^.+category\/(.+)$/;
    const found = url.match(regex);
    return found[1];
}

export {showFieldErrors, removeOldErrors, showAllCategories}
