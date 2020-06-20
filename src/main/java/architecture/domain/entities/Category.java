package architecture.domain.entities;

import architecture.constants.AppConstants;
import architecture.domain.CountryCodes;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "category_names", joinColumns = @JoinColumn(name = "category_id", nullable = false))
    @MapKeyColumn(name = "country_code", length = 2)
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "category_names", nullable = false, length = AppConstants.CATEGORY_MAX_LENGTH)
    private Map<CountryCodes, String> localCategoryNames = new HashMap<>();

    public Map<CountryCodes, String> getLocalCategoryNames() {
        return localCategoryNames;
    }

    public void setLocalCategoryNames(Map<CountryCodes, String> localCategoryNames) {
        this.localCategoryNames = localCategoryNames;
    }
}
