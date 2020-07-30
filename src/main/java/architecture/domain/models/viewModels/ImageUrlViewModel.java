package architecture.domain.models.viewModels;

import architecture.domain.models.BaseModel;

public class ImageUrlViewModel extends BaseModel {
    private String url;

    public ImageUrlViewModel() {
    }

    public ImageUrlViewModel(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
