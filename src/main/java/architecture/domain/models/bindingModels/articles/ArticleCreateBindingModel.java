package architecture.domain.models.bindingModels.articles;

import architecture.annotations.ImageBindingValidationEmpty;
import architecture.domain.models.bindingModels.images.ImageBindingModel;

import javax.validation.constraints.NotNull;

public class ArticleCreateBindingModel extends ArticleBindingModel {
    @NotNull
    @ImageBindingValidationEmpty
    private ImageBindingModel mainImage;

    public ArticleCreateBindingModel() {
        this.mainImage = new ImageBindingModel();
    }

    public ImageBindingModel getMainImage() {
        return mainImage;
    }

    public void setMainImage(ImageBindingModel mainImage) {
        this.mainImage = mainImage;
    }
}
