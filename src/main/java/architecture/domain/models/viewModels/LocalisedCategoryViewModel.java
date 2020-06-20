package architecture.domain.models.viewModels;

public class LocalisedCategoryViewModel extends CategoryViewModel {
    private String name;

    public LocalisedCategoryViewModel(Long id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
