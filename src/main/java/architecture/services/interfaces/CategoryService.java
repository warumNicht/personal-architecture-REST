package architecture.services.interfaces;

import architecture.domain.CountryCodes;
import architecture.domain.models.serviceModels.CategoryServiceModel;
import architecture.domain.models.viewModels.LocalisedCategoryViewModel;

import java.util.List;

public interface CategoryService {
    List<LocalisedCategoryViewModel> getAllCategoriesByLocale(CountryCodes defaultCode, CountryCodes currentCode);

    void addCategory(CategoryServiceModel categoryServiceModel);

    CategoryServiceModel findById(Long id);

    void updateCategory(CategoryServiceModel categoryServiceModel);
}
