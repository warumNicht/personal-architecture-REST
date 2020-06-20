package architecture.integration.web;

import architecture.constants.AppConstants;
import architecture.constants.ViewNames;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;
import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AccessControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    public void getAdminPage_withUser_returnsUnauthorized() throws Exception {
        MockHttpSession mockHttpSession = new MockHttpSession();
        this.mockMvc.perform(get("/fr/admin/articles/create")
                .locale(Locale.FRANCE)
                .session(mockHttpSession)
                .contextPath("/fr")
                .cookie(new Cookie(AppConstants.LOCALE_COOKIE_NAME, "fr")))
                .andExpect(status().is(302))
                .andExpect(redirectedUrlPattern("/**/unauthorized"))
                .andDo(print());

        this.mockMvc.perform(get("/fr/unauthorized")
                .locale(Locale.FRANCE)
                .session(mockHttpSession)
                .contextPath("/fr")
                .cookie(new Cookie(AppConstants.LOCALE_COOKIE_NAME, "fr")))
                .andExpect(status().is(200))
                .andExpect(view().name(ViewNames.UNAUTHORIZED))
                .andDo(print());
    }
}
