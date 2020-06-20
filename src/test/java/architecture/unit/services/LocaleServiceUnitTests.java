package architecture.unit.services;

import architecture.constants.AppConstants;
import architecture.domain.CountryCodes;
import architecture.services.LocaleServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LocaleServiceUnitTests {
    private LocaleServiceImpl localeService;

    @Mock
    private HttpServletRequest request;

    @Before
    public void init() {
        this.localeService = new LocaleServiceImpl(request);
    }

    @Test
    public void givenCookie_FR_returnsCorrect() {
        Mockito.when(request.getCookies()).thenReturn(new Cookie[]{new Cookie(AppConstants.LOCALE_COOKIE_NAME, "fr")});
        Assert.assertEquals(this.localeService.getCurrentCookieLocale(), CountryCodes.FR);
        Assert.assertEquals(this.localeService.getLocale(), "fr");
    }

    @Test
    public void when_missingCookie_returnsDefaultLocale() {
        Assert.assertEquals(this.localeService.getCurrentCookieLocale(), AppConstants.DEFAULT_COUNTRY_CODE);
        Assert.assertEquals(this.localeService.getLocale(), "bg");
    }
}
