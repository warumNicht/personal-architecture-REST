package architecture.domain.models.serviceModels.article;

public class LocalisedArticleContentServiceModel {
    private String title;
    private String content;

    public LocalisedArticleContentServiceModel() {
    }

    public LocalisedArticleContentServiceModel(String title, String content) {
        this.title = title;
        this.content = content;
    }

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
}
