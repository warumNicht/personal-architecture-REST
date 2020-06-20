package architecture.domain.models;

public abstract class BaseModel {
    private Long id;

    protected BaseModel() {
    }

    protected BaseModel(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
