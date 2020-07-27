package architecture.domain.models.viewModels.articles;

import architecture.domain.models.viewModels.CategoryViewModel;

public class ArticleEditViewModel extends ArticleViewModel {
    private Long categoryId;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
