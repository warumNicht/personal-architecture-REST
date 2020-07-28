package architecture.domain.models.bindingModels.articles;

import architecture.annotations.BeginUppercase;
import architecture.constants.AppConstants;
import org.hibernate.validator.constraints.Length;

public abstract class ArticleImageModel extends ArticleTitleContentModel{
    @Length(min = AppConstants.NAME_MIN_LENGTH, max = AppConstants.NAME_MAX_LENGTH, message = "{text.length.between}")
    @BeginUppercase(allowNull = true)
    private String mainImage;

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }
}
