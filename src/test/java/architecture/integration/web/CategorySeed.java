package architecture.integration.web;

import architecture.domain.CountryCodes;
import architecture.domain.entities.Category;
import architecture.repositories.CategoryRepository;
import architecture.util.TestConstants;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public abstract class CategorySeed {
    @Autowired
    protected CategoryRepository categoryRepository;

    protected void seedCategories() {
        Category category = new Category();
        category.setLocalCategoryNames(new HashMap<CountryCodes, String>() {{
            put(CountryCodes.BG, TestConstants.CATEGORY_1_BG_NAME);
            put(CountryCodes.FR, TestConstants.CATEGORY_1_FR_NAME);
        }});
        this.categoryRepository.save(category);

        Category category2 = new Category();
        category2.setLocalCategoryNames(new HashMap<CountryCodes, String>() {{
            put(CountryCodes.BG, TestConstants.CATEGORY_2_BG_NAME);
            put(CountryCodes.FR, TestConstants.CATEGORY_2_FR_NAME);
            put(CountryCodes.ES, TestConstants.CATEGORY_1_ES_NAME);
        }});
        this.categoryRepository.save(category2);

        Category categoryThree = new Category();
        categoryThree.setLocalCategoryNames(new HashMap<>() {{
            put(CountryCodes.FR, TestConstants.CATEGORY_3_FR_NAME);
            put(CountryCodes.ES, TestConstants.CATEGORY_2_ES_NAME);
        }});
        this.categoryRepository.save(categoryThree);
    }
}
