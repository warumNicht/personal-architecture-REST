package architecture.domain.models.viewModels.articles;

import architecture.domain.models.viewModels.CategoryViewModel;

public class ArticleEditViewModel extends ArticleViewModel {
    private CategoryViewModel category;

    public CategoryViewModel getCategory() {
        return category;
    }

    public void setCategory(CategoryViewModel category) {
        this.category = category;
    }
}
