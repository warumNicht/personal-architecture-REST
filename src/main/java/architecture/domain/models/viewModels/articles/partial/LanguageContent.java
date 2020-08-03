package architecture.domain.models.viewModels.articles.partial;

public class LanguageContent {
    private String title;
    private String content;
    private String mainImageName;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMainImageName() {
        return mainImageName;
    }

    public void setMainImageName(String mainImageName) {
        this.mainImageName = mainImageName;
    }
}
