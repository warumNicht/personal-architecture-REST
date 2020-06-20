package architecture.domain.models.bindingModels.images;

import architecture.annotations.BeginUppercase;
import architecture.annotations.ContainsNotEmpty;
import architecture.annotations.EnumValidator;
import architecture.annotations.LengthOrEmpty;
import architecture.constants.AppConstants;
import architecture.domain.CountryCodes;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedHashMap;
import java.util.Map;

public class ImageEditBindingModel extends ImageUrlModel {
    private Long id;

    @NotNull
    @Size(min = AppConstants.COUNTRY_SIZE, max = AppConstants.COUNTRY_SIZE)
    @ContainsNotEmpty
    private LinkedHashMap<@EnumValidator(enumClass = CountryCodes.class, message = "{country.nonexistent}") CountryCodes,
            @LengthOrEmpty(min = AppConstants.NAME_MIN_LENGTH, max = AppConstants.NAME_MAX_LENGTH) @BeginUppercase(allowEmpty = true) String> localImageNames;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LinkedHashMap<CountryCodes, String> getLocalImageNames() {
        return localImageNames;
    }

    public void setLocalImageNames(LinkedHashMap<CountryCodes, String> localImageNames) {
        this.localImageNames = localImageNames.entrySet()
                .stream()
                .collect(LinkedHashMap::new,                                   // Supplier
                        (map, item) -> map.put(item.getKey(), item.getValue().trim()),  // Accumulator
                        Map::putAll);                                                   // Combiner
    }
}
