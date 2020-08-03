package architecture.domain.models.viewModels.articles;

import architecture.domain.models.BaseModel;
import architecture.domain.models.viewModels.ImageLocaleViewModel;
import architecture.domain.models.viewModels.LocalisedArticleTitlesViewModel;

import java.util.Date;

public class ArticleLocalViewModel extends BaseModel {
    private Date date;
    private ImageLocaleViewModel mainImage;
    private LocalisedArticleTitlesViewModel localisedContent;

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

    public LocalisedArticleTitlesViewModel getLocalisedContent() {
        return localisedContent;
    }

    public void setLocalisedContent(LocalisedArticleTitlesViewModel localisedContent) {
        this.localisedContent = localisedContent;
    }
}
