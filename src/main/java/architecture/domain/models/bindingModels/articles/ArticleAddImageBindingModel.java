package architecture.domain.models.bindingModels.articles;

import architecture.domain.CountryCodes;
import architecture.domain.models.bindingModels.images.ImageBindingModel;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ArticleAddImageBindingModel {
    @NotNull
    private CountryCodes lang;

    @NotNull
    @Valid
    private ImageBindingModel image;

    public ArticleAddImageBindingModel() {
    }

    public CountryCodes getLang() {
        return lang;
    }

    public void setLang(CountryCodes lang) {
        this.lang = lang;
    }

    public ImageBindingModel getImage() {
        return image;
    }

    public void setImage(ImageBindingModel image) {
        this.image = image;
    }
}
