package architecture.constants;

import architecture.domain.CountryCodes;
import org.junit.Assert;
import org.junit.Test;

public class ConstantsTests {

    @Test
    public void countryMapSizeEquals_const_COUNTRY_SIZE() {
        int expected = AppConstants.COUNTRY_SIZE;
        int actual = CountryCodes.values().length;

        Assert.assertEquals(expected, actual);
    }
}
