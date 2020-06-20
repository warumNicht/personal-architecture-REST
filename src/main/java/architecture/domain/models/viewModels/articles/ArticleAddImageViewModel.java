package architecture.domain.models.viewModels.articles;

import architecture.domain.models.BaseModel;

public class ArticleAddImageViewModel extends BaseModel {
    private String title;
    private String mainImageUrl;

    public ArticleAddImageViewModel() {
    }

    public ArticleAddImageViewModel(Long id, String title) {
        super(id);
        this.title = title;
    }

    public ArticleAddImageViewModel(Long id, String title, String mainImageUrl) {
        super(id);
        this.title = title;
        this.mainImageUrl = mainImageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }
}
