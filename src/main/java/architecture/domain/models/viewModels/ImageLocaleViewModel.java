package architecture.domain.models.viewModels;

public class ImageLocaleViewModel extends ImageSimpleViewModel {
    private String name;

    public ImageLocaleViewModel() {
    }

    public ImageLocaleViewModel(String url, String name) {
        super(url);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
