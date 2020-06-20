package architecture.domain.models.bindingModels;

import architecture.annotations.BeginUppercase;
import architecture.annotations.ContainsNotEmpty;
import architecture.annotations.EnumValidator;
import architecture.annotations.LengthOrEmpty;
import architecture.constants.AppConstants;
import architecture.domain.CountryCodes;
import architecture.domain.models.BaseModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedHashMap;

public class CategoryEditBindingModel extends BaseModel {
    @NotNull(message = "{value.null}")
    @Size(min = AppConstants.COUNTRY_SIZE, max = AppConstants.COUNTRY_SIZE)
    @ContainsNotEmpty
    private LinkedHashMap<@EnumValidator(enumClass = CountryCodes.class, message = "{country.nonexistent}") CountryCodes,
            @LengthOrEmpty(min = AppConstants.NAME_MIN_LENGTH, max = AppConstants.CATEGORY_MAX_LENGTH) @BeginUppercase(allowEmpty = true) String> localNames;

    public CategoryEditBindingModel() {
        this.localNames = new LinkedHashMap<>();
        for (CountryCodes countryCode : CountryCodes.values()) {
            this.localNames.put(countryCode, "");
        }
    }

    public LinkedHashMap<CountryCodes, String> getLocalNames() {
        return localNames;
    }

    public void setLocalNames(LinkedHashMap<CountryCodes, String> localNames) {
        this.localNames = localNames;
    }
}
