package architecture.integration.web.articles;

import architecture.constants.AppConstants;
import architecture.constants.ViewNames;
import architecture.domain.CountryCodes;
import architecture.domain.entities.Article;
import architecture.domain.models.bindingModels.articles.ArticleAddImageBindingModel;
import architecture.domain.models.bindingModels.images.ImageBindingModel;
import architecture.services.interfaces.ImageService;
import architecture.util.TestConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@WithMockUser(roles = {"ADMIN"})
public class ArticleControllerAddImageIntegrationTests extends ArticleControllerBaseTests {
    @Autowired
    private ImageService imageService;
    private Article seededArticle;

    @Before
    public void init() {
        super.seedCategories();
        this.seededArticle = super.createArticleWithoutImage();
    }

    @Test
    public void getArticleAddImage_withImage_Admin_returnsCorrectView() throws Exception {
        super.mockMvc.perform(get("/fr/admin/articles/add-image/" + this.seededArticle.getId())
                .locale(Locale.FRANCE)
                .contextPath("/fr")
                .cookie(new Cookie(AppConstants.LOCALE_COOKIE_NAME, "fr")))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.ARTICLE_ADD_IMAGE))
                .andDo(print());
    }

    @Test
    @WithAnonymousUser
    public void getArticleAddImage_withImage_Anonymous_redirectsLogin() throws Exception {
        super.mockMvc.perform(get("/fr/admin/articles/add-image/" + this.seededArticle.getId())
                .locale(Locale.FRANCE)
                .contextPath("/fr")
                .cookie(new Cookie(AppConstants.LOCALE_COOKIE_NAME, "fr")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/fr/users/login"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    public void getArticleAddImage_withImage_User_redirectsUnauthorized() throws Exception {
        super.mockMvc.perform(get("/fr/admin/articles/add-image/" + this.seededArticle.getId())
                .locale(Locale.FRANCE)
                .contextPath("/fr")
                .cookie(new Cookie(AppConstants.LOCALE_COOKIE_NAME, "fr")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/**/fr/unauthorized"))
                .andDo(print());
    }

    @Test
    public void putArticleAddImage_withImage_Admin_validData_returnsCorrectUrl() throws Exception {
        this.seededArticle = super.createArticleWithImage();
        String requestBody = super.getJsonFromObject(this.createValidArticleAddImageBindingModel());

        MockHttpServletResponse res = super.mockMvc.perform(put("/fr/admin/articles/add-image/" + this.seededArticle.getId())
                .locale(Locale.FRANCE)
                .contextPath("/fr")
                .cookie(new Cookie(AppConstants.LOCALE_COOKIE_NAME, "fr"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf()))
                .andExpect(status().is(202))
                .andDo(print()).andReturn().getResponse();

        int articleImages = this.imageService.getImagesByArticle(this.seededArticle.getId()).size();
        Assert.assertEquals(2, articleImages);

        String contentAsString = res.getContentAsString();
        Assert.assertEquals(contentAsString, "\"/fr/admin/articles/edit/" + this.seededArticle.getId() + "\"");
    }

    @Test
    public void putArticleAddMainImage_withImage_Admin_validData_returnsCorrectUrlAndModifiesData() throws Exception {
        this.seededArticle.setMainImage(null);
        String requestBody = super.getJsonFromObject(this.createValidArticleAddImageBindingModel());

        Assert.assertNull(this.seededArticle.getMainImage());

        MockHttpServletResponse res = super.mockMvc.perform(put("/fr/admin/articles/add-image/" + this.seededArticle.getId())
                .locale(Locale.FRANCE)
                .contextPath("/fr")
                .cookie(new Cookie(AppConstants.LOCALE_COOKIE_NAME, "fr"))
                .param("main", "true")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf()))
                .andExpect(status().is(202))
                .andDo(print()).andReturn().getResponse();

        Assert.assertNotNull(this.seededArticle.getMainImage());
        Assert.assertEquals(this.seededArticle.getMainImage().getUrl(), TestConstants.IMAGE_URL);
        Assert.assertEquals(this.seededArticle.getMainImage().getLocalImageNames().size(), 1);
        Assert.assertEquals(this.seededArticle.getMainImage().getLocalImageNames().get(CountryCodes.ES), TestConstants.IMAGE_ES_NAME);

        String contentAsString = res.getContentAsString();
        Assert.assertEquals(contentAsString, "\"/fr/admin/articles/edit/" + this.seededArticle.getId() + "\"");
    }

    @Test
    public void putArticleAddImage_withImage_Admin_nullData_returnsErrorMap() throws Exception {
        MockHttpServletResponse res = super.mockMvc.perform(put("/fr/admin/articles/add-image/" + this.seededArticle.getId())
                .locale(Locale.FRANCE)
                .contextPath("/fr")
                .cookie(new Cookie(AppConstants.LOCALE_COOKIE_NAME, "fr"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(super.getJsonFromObject(new ArticleAddImageBindingModel()))
                .with(csrf()))
                .andExpect(status().is(405))
                .andDo(print()).andReturn().getResponse();

        HashMap<String, List<String>> errorMap = (HashMap<String, List<String>>) super.getObjectFromJsonString(res.getContentAsString());
        Assert.assertEquals(2, errorMap.size());
    }

    @Test
    @WithAnonymousUser
    public void putArticleAddImage_withImage_Anonymous_validData_redirectsLogin() throws Exception {
        String requestBody = super.getJsonFromObject(this.createValidArticleAddImageBindingModel());

        super.mockMvc.perform(put("/fr/admin/articles/add-image/" + this.seededArticle.getId())
                .locale(Locale.FRANCE)
                .contextPath("/fr")
                .cookie(new Cookie(AppConstants.LOCALE_COOKIE_NAME, "fr"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/fr/users/login"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    public void putArticleAddImage_withImage_User_validData_redirectsUnauthorized() throws Exception {
        String requestBody = super.getJsonFromObject(this.createValidArticleAddImageBindingModel());

        super.mockMvc.perform(put("/fr/admin/articles/add-image/" + this.seededArticle.getId())
                .locale(Locale.FRANCE)
                .contextPath("/fr")
                .cookie(new Cookie(AppConstants.LOCALE_COOKIE_NAME, "fr"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/**/fr/unauthorized"))
                .andDo(print());
    }


    private ArticleAddImageBindingModel createValidArticleAddImageBindingModel() {
        ArticleAddImageBindingModel model = new ArticleAddImageBindingModel();
        model.setLang(CountryCodes.ES);

        ImageBindingModel image = new ImageBindingModel();
        image.setName(TestConstants.IMAGE_ES_NAME);
        image.setUrl(TestConstants.IMAGE_URL);

        model.setImage(image);
        return model;
    }


}
