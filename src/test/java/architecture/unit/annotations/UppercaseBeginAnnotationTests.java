package architecture.unit.annotations;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.Set;

public class UppercaseBeginAnnotationTests {
    private static final String[] VALID_VALUES = {"Щ", "Я", "Й", "Ѣ", "Й", "Ь", "Ѫ", "Ю",
            "Ä", "Æ", "Œ", "Ø", "Ç", "Ð", "Ψ", "Σ", "Ω", "Ǣ", "Ϣ", "Â", "Í"};

    private static final String[] INVALID_VALUES = {null, "", "ѣ", "ѫ", "ю", "诶", "吉", "あ", "ㅎ",
            "ä", "æ", "£", "œ", "β", "ç", "φ", "ω", "θ", "ξ", "1", "@", "%", "#"};
    private Validator validator;

    @Before
    public void init() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void validValues_returnCorrect() {
        Arrays.stream(VALID_VALUES).forEach((value) -> {
            System.out.print(value);
            TestAnnotationObject object = new TestAnnotationObject(value);
            Set<ConstraintViolation<TestAnnotationObject>> validate = this.validator.validate(object);
            System.out.println(" -> " + validate.isEmpty());
            Assert.assertTrue(validate.isEmpty());
        });
    }

    @Test
    public void nullValue_returnsError() {
        TestAnnotationObject object = new TestAnnotationObject(null);
        Set<ConstraintViolation<TestAnnotationObject>> validate = this.validator.validate(object);
        Assert.assertFalse(validate.isEmpty());
    }

    @Test
    public void invalidValues_returnError() {
        Arrays.stream(INVALID_VALUES).forEach((value) -> {
            System.out.print(value);
            TestAnnotationObject object = new TestAnnotationObject(value);
            Set<ConstraintViolation<TestAnnotationObject>> validate = this.validator.validate(object);
            System.out.println(" -> " + validate.isEmpty());
            Assert.assertFalse(validate.isEmpty());
        });
    }

}
