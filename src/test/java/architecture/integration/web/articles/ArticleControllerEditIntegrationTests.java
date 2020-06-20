package architecture.integration.web.articles;

import architecture.constants.AppConstants;
import architecture.constants.ViewNames;
import architecture.domain.entities.Article;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class ArticleControllerEditIntegrationTests extends ArticleControllerBaseTests {
    private Article seededArticle;

    @Before
    public void init() {
        super.seedCategories();
        this.seededArticle = super.createArticleWithImage();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void getArticleEdit_withAdmin_returnsCorrectView() throws Exception {
        super.mockMvc.perform(get("/fr/admin/articles/edit/" + this.seededArticle.getId())
                .locale(Locale.FRANCE)
                .contextPath("/fr")
                .cookie(new Cookie(AppConstants.LOCALE_COOKIE_NAME, "fr")))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.ARTICLE_EDIT))
                .andDo(print());
    }

    @Test
    @WithAnonymousUser
    public void getArticleEdit_withAnonymous_redirectsLogin() throws Exception {
        super.mockMvc.perform(get("/fr/admin/articles/edit/" + this.seededArticle.getId())
                .locale(Locale.FRANCE)
                .contextPath("/fr")
                .cookie(new Cookie(AppConstants.LOCALE_COOKIE_NAME, "fr")))
                .andExpect(status().is(302))
                .andExpect(redirectedUrlPattern("**/fr/users/login"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    public void getArticleEdit_withRoleUser_redirectsUnauthorized() throws Exception {
        super.mockMvc.perform(get("/fr/admin/articles/edit/" + this.seededArticle.getId())
                .locale(Locale.FRANCE)
                .contextPath("/fr")
                .cookie(new Cookie(AppConstants.LOCALE_COOKIE_NAME, "fr")))
                .andExpect(status().is(302))
                .andExpect(redirectedUrlPattern("/**/fr/unauthorized"))
                .andDo(print());
    }


}
