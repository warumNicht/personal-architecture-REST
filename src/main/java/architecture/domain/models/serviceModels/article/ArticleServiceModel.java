package architecture.domain.models.serviceModels.article;

import architecture.domain.CountryCodes;
import architecture.domain.models.BaseModel;
import architecture.domain.models.serviceModels.CategoryServiceModel;
import architecture.domain.models.serviceModels.ImageServiceModel;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ArticleServiceModel extends BaseModel {
    private Date date;
    private CategoryServiceModel category;
    private ImageServiceModel mainImage;
    private Map<CountryCodes, LocalisedArticleContentServiceModel> localContent = new HashMap<>();

    public ArticleServiceModel() {
    }

    public ArticleServiceModel(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public CategoryServiceModel getCategory() {
        return category;
    }

    public void setCategory(CategoryServiceModel category) {
        this.category = category;
    }

    public ImageServiceModel getMainImage() {
        return mainImage;
    }

    public void setMainImage(ImageServiceModel mainImage) {
        this.mainImage = mainImage;
    }

    public Map<CountryCodes, LocalisedArticleContentServiceModel> getLocalContent() {
        return localContent;
    }

    public void setLocalContent(Map<CountryCodes, LocalisedArticleContentServiceModel> localContent) {
        this.localContent = localContent;
    }
}
