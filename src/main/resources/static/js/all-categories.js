function editCategory(categoryId) {
    const url = `/${locale}/admin/category/edit/${categoryId}`;
    window.location.href = url;
}

function showAllCategories() {
    const categoriesContainer = $('div.categories-container');
    const categoriesSelect = $('#select-categories option');
    const editButtonName = $('#edit-button-name').val();

    categoriesSelect.each(function (index) {
        if (index !== 0) {
            const value = $(this).val();
            const innerText = $(this).text();
            const currentCategoryDiv = $('<div class="category-wrapper"></div>');
            currentCategoryDiv.append(`<div class="category-name">${innerText}</div>`);
            currentCategoryDiv.append(`<button class="btn btn-info" onclick="editCategory(${value})">${editButtonName}</button>`);
            categoriesContainer.append(currentCategoryDiv);
        }
    });
    $('div.spinner-border').hide();
}