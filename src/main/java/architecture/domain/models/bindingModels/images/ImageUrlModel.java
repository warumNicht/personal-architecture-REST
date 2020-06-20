package architecture.domain.models.bindingModels.images;

import architecture.constants.AppConstants;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public abstract class ImageUrlModel {
    @NotNull
    @NotEmpty(message = "{text.empty}")
    @Pattern(regexp = AppConstants.URL_REGEX_PATTERN, message = "{url}")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
