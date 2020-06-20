package architecture.domain.models.viewModels;

import architecture.domain.CountryCodes;

import java.util.HashMap;
import java.util.Map;

public class ImageViewModel extends ImageSimpleViewModel {
    private Map<CountryCodes, String> localImageNames = new HashMap<>();

    public Map<CountryCodes, String> getLocalImageNames() {
        return localImageNames;
    }

    public void setLocalImageNames(Map<CountryCodes, String> localImageNames) {
        this.localImageNames = localImageNames;
    }
}
