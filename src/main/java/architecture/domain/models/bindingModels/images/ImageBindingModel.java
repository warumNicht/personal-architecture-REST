package architecture.domain.models.bindingModels.images;

import architecture.annotations.BeginUppercase;
import architecture.annotations.LengthOrEmpty;
import architecture.constants.AppConstants;

import javax.validation.constraints.NotEmpty;

public class ImageBindingModel extends ImageUrlModel {
    @NotEmpty
    @LengthOrEmpty(min = AppConstants.NAME_MIN_LENGTH, max = AppConstants.NAME_MAX_LENGTH)
    @BeginUppercase(allowEmpty = true)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
