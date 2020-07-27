package architecture.domain.models.bindingModels.articles;

import architecture.annotations.ImageBindingValidationEmpty;
import architecture.domain.models.bindingModels.images.ImageBindingModel;

import javax.validation.constraints.NotNull;

public class ArticleCreateBindingModel extends ArticleBindingModel {
    @NotNull
    private Long categoryId;

    @ImageBindingValidationEmpty
    private ImageBindingModel mainImage;

    public ImageBindingModel getMainImage() {
        return mainImage;
    }

    public void setMainImage(ImageBindingModel mainImage) {
        this.mainImage = mainImage;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
