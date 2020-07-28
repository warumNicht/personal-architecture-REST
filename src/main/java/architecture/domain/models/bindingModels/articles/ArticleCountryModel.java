package architecture.domain.models.bindingModels.articles;

import architecture.annotations.EnumValidator;
import architecture.domain.CountryCodes;

import javax.validation.constraints.NotNull;

public abstract class ArticleCountryModel extends ArticleTitleContentModel{
    @NotNull
    @EnumValidator(enumClass = CountryCodes.class, message = "{country.nonexistent}")
    private CountryCodes country;

    public CountryCodes getCountry() {
        return country;
    }

    public void setCountry(CountryCodes country) {
        this.country = country;
    }
}
