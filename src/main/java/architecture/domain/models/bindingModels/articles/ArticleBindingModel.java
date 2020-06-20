package architecture.domain.models.bindingModels.articles;

import architecture.annotations.BeginUppercase;
import architecture.annotations.EnumValidator;
import architecture.constants.AppConstants;
import architecture.domain.CountryCodes;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public abstract class ArticleBindingModel {
    @NotNull
    @EnumValidator(enumClass = CountryCodes.class, message = "{country.nonexistent}")
    private CountryCodes country;

    @NotNull
    @NotEmpty(message = "{text.empty}")
    @Size(min = AppConstants.NAME_MIN_LENGTH, max = AppConstants.NAME_MAX_LENGTH, message = "{text.length.between}")
    @BeginUppercase(allowNull = true)
    private String title;

    @NotNull
    @NotEmpty(message = "{text.empty}")
    @Size(min = AppConstants.DESCRIPTION_MIN_LENGTH, message = "{text.length.min}")
    @BeginUppercase(allowEmpty = true)
    private String content;

    public CountryCodes getCountry() {
        return country;
    }

    public void setCountry(CountryCodes country) {
        this.country = country;
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
