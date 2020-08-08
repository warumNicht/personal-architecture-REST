package architecture.domain.models.viewModels.articles.partial;

public class TitleViewModel {
    private String title;

    public TitleViewModel(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
