package architecture.domain.models.viewModels.articles;

import architecture.domain.models.BaseModel;
import architecture.domain.models.viewModels.ImageLocaleViewModel;

public class ArticleSimpleViewModel extends BaseModel {
    private String title;
    private Long categoryId;
    private ImageLocaleViewModel mainImage;

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

    public ImageLocaleViewModel getMainImage() {
        return mainImage;
    }

    public void setMainImage(ImageLocaleViewModel mainImage) {
        this.mainImage = mainImage;
    }
}
