package architecture.domain.models.viewModels.articles;

import architecture.domain.models.BaseModel;
import architecture.domain.models.viewModels.ImageLocaleViewModel;
import architecture.domain.models.viewModels.LocalisedArticleContentViewModel;

import java.util.Date;

public class ArticleLocalViewModel extends BaseModel {
    private Date date;
    private ImageLocaleViewModel mainImage;
    private LocalisedArticleContentViewModel localisedContent;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ImageLocaleViewModel getMainImage() {
        return mainImage;
    }

    public void setMainImage(ImageLocaleViewModel mainImage) {
        this.mainImage = mainImage;
    }

    public LocalisedArticleContentViewModel getLocalisedContent() {
        return localisedContent;
    }

    public void setLocalisedContent(LocalisedArticleContentViewModel localisedContent) {
        this.localisedContent = localisedContent;
    }
}
