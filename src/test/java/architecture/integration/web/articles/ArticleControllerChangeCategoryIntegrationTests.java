package architecture.integration.web.articles;

import architecture.constants.AppConstants;
import architecture.domain.entities.Article;
import architecture.util.TestConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Locale;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@WithMockUser(roles = {"ADMIN"})
public class ArticleControllerChangeCategoryIntegrationTests extends ArticleControllerBaseTests {
    private Article seededArticle;

    @Before
    public void init() {
        super.seedCategories();
        this.seededArticle = super.createArticleWithImage();
    }

    @Test
    public void patch_changeCategory_withAdmin_works() throws Exception {
        String newCategoryId = super.categoryRepository.findAll().get(1).getId().toString();

        MockHttpServletResponse response = super.mockMvc.perform(patch("/fr/admin/articles/change-category/" + this.seededArticle.getId())
                .locale(Locale.FRANCE)
                .contextPath("/fr")
                .cookie(new Cookie(AppConstants.LOCALE_COOKIE_NAME, "fr"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(newCategoryId)
                .with(csrf()))
                .andExpect(status().is(202))
                .andDo(print())
                .andReturn().getResponse();

        Object responseBody = super.getObjectFromJsonString(response.getContentAsString());
        HashMap<String, Object> responseObject = (HashMap<String, Object>) responseBody;

        Assert.assertEquals(responseObject.get("oldCategoryName"), TestConstants.CATEGORY_1_FR_NAME);
        Assert.assertEquals(responseObject.get("newCategoryName"), TestConstants.CATEGORY_2_FR_NAME);
        Assert.assertEquals(responseObject.get("title"), TestConstants.ARTICLE_VALID_TITLE);
    }

    @Test
    public void patch_changeCategory_withAdmin_nonexistentCategory_returnsError() throws Exception {
        MockHttpServletResponse response = super.mockMvc.perform(patch("/fr/admin/articles/change-category/555")
                .locale(Locale.FRANCE)
                .contextPath("/fr")
                .cookie(new Cookie(AppConstants.LOCALE_COOKIE_NAME, "fr"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("555")
                .with(csrf()))
                .andExpect(status().is(404))
                .andDo(print()).andReturn().getResponse();

        Object res = super.getObjectFromJsonString(response.getContentAsString());
        HashMap<String, Object> hashMap = (HashMap<String, Object>) res;
        Integer status = (Integer) hashMap.get("status");
        Assert.assertEquals(404, (int) status);
    }

    @Test
    public void patch_changeCategory_withAdmin_nonexistentArticle_returnsError() throws Exception {
        MockHttpServletResponse response = super.mockMvc.perform(patch("/fr/admin/articles/change-category/" + this.seededArticle.getId())
                .locale(Locale.FRANCE)
                .contextPath("/fr")
                .cookie(new Cookie(AppConstants.LOCALE_COOKIE_NAME, "fr"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("555")
                .with(csrf()))
                .andExpect(status().is(404))
                .andDo(print()).andReturn().getResponse();

        Object res = super.getObjectFromJsonString(response.getContentAsString());
        HashMap<String, Object> hashMap = (HashMap<String, Object>) res;
        Integer status = (Integer) hashMap.get("status");
        Assert.assertEquals(404, (int) status);
    }

}
