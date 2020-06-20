package architecture.unit.web;

import architecture.constants.AppConstants;
import architecture.domain.CountryCodes;
import architecture.domain.models.viewModels.LocalisedCategoryViewModel;
import architecture.services.interfaces.CategoryService;
import architecture.services.interfaces.ImageService;
import architecture.services.interfaces.LocaleService;
import architecture.services.interfaces.UserService;
import architecture.util.TestConstants;
import architecture.web.controllers.FetchController;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(FetchController.class)
public class FetchControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private UserService userService;
    @MockBean
    private CategoryService categoryService;
    @MockBean
    private ImageService imageService;
    @MockBean
    private LocaleService localeService;

    @Before
    public void setUp() {
        Mockito.when(localeService.getCurrentCookieLocale()).thenReturn(CountryCodes.DE);
    }

    @Test
    public void whenNoCategories_returnsEmptyArray() throws Exception {
        Mockito.when(categoryService.getAllCategoriesByLocale(AppConstants.DEFAULT_COUNTRY_CODE, CountryCodes.DE))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/fetch/categories/all").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void givenCategories_returnsCorrect() throws Exception {
        List<LocalisedCategoryViewModel> categories = Arrays.asList(new LocalisedCategoryViewModel(1L, TestConstants.CATEGORY_1_BG_NAME),
                new LocalisedCategoryViewModel(2L, TestConstants.CATEGORY_2_BG_NAME)
        );
        Mockito.when(categoryService.getAllCategoriesByLocale(AppConstants.DEFAULT_COUNTRY_CODE, CountryCodes.DE))
                .thenReturn(categories);

        MvcResult mvcResult = mockMvc.perform(get("/fetch/categories/all").contentType(MediaType.APPLICATION_JSON)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        mockMvc.perform(get("/fetch/categories/all").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].name", Matchers.is(TestConstants.CATEGORY_1_BG_NAME)))
                .andExpect(jsonPath("$[1].name", Matchers.is(TestConstants.CATEGORY_2_BG_NAME)));
    }
}
