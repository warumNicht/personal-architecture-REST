package architecture.integration.web;

import architecture.constants.AppConstants;
import architecture.domain.CountryCodes;
import architecture.domain.entities.Article;
import architecture.domain.entities.Image;
import architecture.integration.web.articles.ArticleControllerBaseTests;
import architecture.repositories.ImageRepository;
import architecture.util.TestConstants;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.HashMap;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class FetchControllerIntegrationTests extends ArticleControllerBaseTests {
    @Autowired
    private ImageRepository imageRepository;

    @Before
    public void init() {
        super.seedCategories();
    }

    @Test
    public void fetchCategories_locale_FR_returnsCorrect_whenAllNamesPresent() throws Exception {
        super.mockMvc.perform(MockMvcRequestBuilders.get("/fetch/categories/all")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie(AppConstants.LOCALE_COOKIE_NAME, "fr")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", Matchers.is(TestConstants.CATEGORY_1_FR_NAME)))
                .andExpect(jsonPath("$[1].name", Matchers.is(TestConstants.CATEGORY_2_FR_NAME)))
                .andExpect(jsonPath("$[2].name", Matchers.is(TestConstants.CATEGORY_3_FR_NAME)));
    }

    @Test
    public void fetchCategories_locale_ES_returnsDefault_whenSomeNamesAbsent() throws Exception {
        super.mockMvc.perform(MockMvcRequestBuilders.get("/fetch/categories/all")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie(AppConstants.LOCALE_COOKIE_NAME, "es")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", Matchers.is(TestConstants.CATEGORY_1_BG_NAME)))
                .andExpect(jsonPath("$[1].name", Matchers.is(TestConstants.CATEGORY_1_ES_NAME)))
                .andExpect(jsonPath("$[2].name", Matchers.is(TestConstants.CATEGORY_2_ES_NAME)));
    }

    @Test
    public void fetchCategories_locale_DE_doesNotReturn_whenNameAndDefaultAbsent() throws Exception {
        super.mockMvc.perform(MockMvcRequestBuilders.get("/fetch/categories/all")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie(AppConstants.LOCALE_COOKIE_NAME, "de")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void fetchArticleImages_locale_FR_returnsCorrect() throws Exception {
        Article article = this.createArticleWithImages();
        super.mockMvc.perform(MockMvcRequestBuilders.get("/fetch/images/" + article.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie(AppConstants.LOCALE_COOKIE_NAME, "es")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].localImageNames.*", hasSize(3)))
                .andExpect(jsonPath("$[1].localImageNames.*", hasSize(2)));
    }

    private Article createArticleWithImages() {
        Article article = super.createArticleWithoutImage();
        Image image1 = new Image();
        image1.setUrl(TestConstants.IMAGE_URL);
        image1.setLocalImageNames(new HashMap<>() {{
            put(CountryCodes.FR, TestConstants.IMAGE_FR_NAME);
            put(CountryCodes.BG, TestConstants.IMAGE_BG_NAME);
            put(CountryCodes.ES, TestConstants.IMAGE_ES_NAME);
        }});
        image1.setArticle(article);
        this.imageRepository.save(image1);

        Image image2 = new Image();
        image2.setUrl(TestConstants.IMAGE_URL_2);
        image2.setLocalImageNames(new HashMap<>() {{
            put(CountryCodes.FR, TestConstants.IMAGE_FR_NAME_2);
            put(CountryCodes.BG, TestConstants.IMAGE_BG_NAME_2);
        }});
        image2.setArticle(article);
        this.imageRepository.save(image2);
        return article;
    }
}
