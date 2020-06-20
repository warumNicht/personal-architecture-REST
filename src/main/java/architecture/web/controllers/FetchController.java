package architecture.web.controllers;

import architecture.constants.AppConstants;
import architecture.domain.CountryCodes;
import architecture.domain.models.viewModels.ImageViewModel;
import architecture.services.interfaces.CategoryService;
import architecture.services.interfaces.ImageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/fetch")
public class FetchController extends BaseController {
    private final CategoryService categoryService;
    private final ImageService imageService;
    private final ModelMapper modelMapper;

    @Autowired
    public FetchController(CategoryService categoryService, ImageService imageService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.imageService = imageService;
        this.modelMapper = modelMapper;
    }

//    @ModelAttribute
//    public void setVaryResponseHeader(HttpServletResponse response) {
//        response.setHeader("Access-Control-Allow-Origin", "*");
//    }

    @GetMapping (value = "/session", produces = "application/json")
    public Object getSessionCookie() {
        CountryCodes wanted = super.getCurrentCookieLocale();
        return wanted;
    }

    @GetMapping (value = "/categories/all", produces = "application/json")
    public Object getCategories() {
        CountryCodes wanted = super.getCurrentCookieLocale();
        return this.categoryService.getAllCategoriesByLocale(AppConstants.DEFAULT_COUNTRY_CODE, wanted);
    }

    @PostMapping(value = "/categories/post", produces = "application/json")
    public Object getCategoriesPost() {
        CountryCodes wanted = super.getCurrentCookieLocale();
        return wanted;
    }

    @ResponseBody
    @PostMapping(value = "/categories/example", produces = "application/json")
    public Object getSamplePost(@RequestBody String text) {
        System.out.println(text);
        return text;
    }

    @RequestMapping(value = "/images/{articleId}", produces = "application/json")
    public Object getArticleImages(@PathVariable(name = "articleId") Long articleId) {
        return this.imageService.getImagesByArticle(articleId)
                .stream()
                .map(img -> this.modelMapper.map(img, ImageViewModel.class))
                .collect(Collectors.toList());
    }
}
