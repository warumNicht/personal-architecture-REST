package architecture.domain.models.viewModels.articles;

import architecture.domain.models.BaseModel;

public class ArticleSimpleViewModel extends BaseModel {
    private String title;
    private Long categoryId;
    private String mainImage;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }
}
