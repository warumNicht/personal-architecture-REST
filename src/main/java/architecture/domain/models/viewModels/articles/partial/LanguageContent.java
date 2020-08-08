package architecture.domain.models.viewModels.articles.partial;

public class LanguageContent extends ContentImageNameViewModel{
    private String title;

    public LanguageContent(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
