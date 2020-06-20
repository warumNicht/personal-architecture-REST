package architecture.integration.web;

import architecture.constants.AppConstants;
import architecture.constants.ViewNames;
import architecture.domain.CountryCodes;
import architecture.domain.entities.Article;
import architecture.integration.web.articles.ArticleControllerBaseTests;
import architecture.util.TestConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class ProjectControllerIntegrationTests extends ArticleControllerBaseTests {
    @Before
    public void init() {
        super.seedCategories();
    }

    @Test
    public void getArticlesByCategory_returnsCorrectViewAndArticles() throws Exception {
        super.createArticleWithoutImage();
        Article article = super.articleRepository.findAll().get(0);
        super.mockMvc.perform(get("/fr/projects/category/" + super.categoryRepository.findAll().get(0).getId())
                .locale(Locale.FRANCE)
                .contextPath("/fr")
                .cookie(new Cookie(AppConstants.LOCALE_COOKIE_NAME, "fr")))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ViewNames.PROJECTS_model_attribute_name, hasSize(1)))
                .andExpect(model().attribute(ViewNames.PROJECTS_model_attribute_name, hasItem(
                        allOf(
                                hasProperty("id", is(article.getId())),
                                hasProperty("date", notNullValue()),
                                hasProperty("mainImage", nullValue()),
                                hasProperty("localisedContent", notNullValue()),
                                hasProperty("localisedContent", allOf(
                                        hasProperty("title", is(article.getLocalContent().get(CountryCodes.FR).getTitle())),
                                        hasProperty("content", is(article.getLocalContent().get(CountryCodes.FR).getContent()))
                                ))
                        )
                )))
                .andExpect(view().name(ViewNames.PROJECTS_BY_CATEGORY))
                .andDo(print());
    }

    @Test
    public void getArticlesByCategory_returnsCorrectArticles_whenWithMainImage() throws Exception {
        super.createArticleWithImage();
        Article article = super.articleRepository.findAll().get(0);
        super.mockMvc.perform(get("/fr/projects/category/" + super.categoryRepository.findAll().get(0).getId())
                .locale(Locale.FRANCE)
                .contextPath("/fr")
                .cookie(new Cookie(AppConstants.LOCALE_COOKIE_NAME, "fr")))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ViewNames.PROJECTS_model_attribute_name, hasSize(1)))
                .andExpect(model().attribute(ViewNames.PROJECTS_model_attribute_name, hasItem(
                        allOf(
                                hasProperty("id", is(article.getId())),
                                hasProperty("date", notNullValue()),
                                hasProperty("mainImage", allOf(
                                        hasProperty("name", is(article.getMainImage().getLocalImageNames().get(CountryCodes.FR)))
                                )),
                                hasProperty("localisedContent", notNullValue()),
                                hasProperty("localisedContent", allOf(
                                        hasProperty("title", is(article.getLocalContent().get(CountryCodes.FR).getTitle())),
                                        hasProperty("content", is(article.getLocalContent().get(CountryCodes.FR).getContent()))
                                ))
                        )
                )))
                .andDo(print());
    }

    @Test
    public void getArticlesByNonexistentCategory_returnsZeroArticles() throws Exception {
        super.createArticleWithoutImage();
        super.mockMvc.perform(get("/fr/projects/category/" + TestConstants.INVALID_ID)
                .locale(Locale.FRANCE)
                .contextPath("/fr")
                .cookie(new Cookie(AppConstants.LOCALE_COOKIE_NAME, "fr")))
                .andExpect(model().attribute(ViewNames.PROJECTS_model_attribute_name, hasSize(0)))
                .andDo(print());
    }
}
