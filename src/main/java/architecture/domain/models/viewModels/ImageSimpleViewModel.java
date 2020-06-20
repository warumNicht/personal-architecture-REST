package architecture.domain.models.viewModels;

import architecture.domain.models.BaseModel;

public class ImageSimpleViewModel extends BaseModel {
    private String url;

    public ImageSimpleViewModel() {
    }

    public ImageSimpleViewModel(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
