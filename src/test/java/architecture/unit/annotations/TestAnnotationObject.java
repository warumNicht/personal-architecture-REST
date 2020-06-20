package architecture.unit.annotations;

import architecture.annotations.BeginUppercase;

public class TestAnnotationObject {
    @BeginUppercase
    private String value;

    public TestAnnotationObject(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
