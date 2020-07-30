package architecture.domain.models.viewModels.articles;

import architecture.domain.CountryCodes;
import architecture.domain.models.BaseModel;
import architecture.domain.models.viewModels.ImageUrlViewModel;
import architecture.domain.models.viewModels.LocalisedArticleContentViewModel;

import java.util.HashMap;
import java.util.Map;

public class ArticleViewModel extends BaseModel {
    private ImageUrlViewModel mainImage;
    private Map<CountryCodes, LocalisedArticleContentViewModel> localContent = new HashMap<>();

    public ImageUrlViewModel getMainImage() {
        return mainImage;
    }

    public void setMainImage(ImageUrlViewModel mainImage) {
        this.mainImage = mainImage;
    }

    public Map<CountryCodes, LocalisedArticleContentViewModel> getLocalContent() {
        return localContent;
    }

    public void setLocalContent(Map<CountryCodes, LocalisedArticleContentViewModel> localContent) {
        this.localContent = localContent;
    }
}
