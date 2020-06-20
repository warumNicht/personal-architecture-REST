package architecture.domain.models.bindingModels;

import architecture.annotations.BeginUppercase;
import architecture.annotations.EnumValidator;
import architecture.constants.AppConstants;
import architecture.domain.CountryCodes;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CategoryCreateBindingModel {
    @NotNull
    @EnumValidator(enumClass = CountryCodes.class, message = "{country.nonexistent}")
    private CountryCodes country;

    @NotNull
    @NotEmpty(message = "{text.empty}")
    @Size(min = AppConstants.NAME_MIN_LENGTH, max = AppConstants.CATEGORY_MAX_LENGTH, message = "{text.length.between}")
    @BeginUppercase
    @Pattern(regexp = "^(?=.*\\S).+$|^$", flags = Pattern.Flag.DOTALL, message = "{text.blank}")
    private String name;

    public CountryCodes getCountry() {
        return country;
    }

    public void setCountry(CountryCodes country) {
        this.country = country;
    }

    public void setCountry(String country) {
        try {
            this.country = CountryCodes.valueOf(country);
        } catch (Exception e) {
            this.country = null;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
