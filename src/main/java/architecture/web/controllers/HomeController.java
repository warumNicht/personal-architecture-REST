package architecture.web.controllers;

import architecture.domain.CountryCodes;
import architecture.domain.entities.LocalisedArticleContent;
import architecture.domain.models.viewModels.LocalisedArticleContentViewModel;
import architecture.domain.models.viewModels.articles.ArticleLocalViewModel;
import architecture.repositories.ArticleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/")
public class HomeController extends BaseController {

    private final ModelMapper modelMapper;
    private ArticleRepository articleRepository;

    @Autowired
    public HomeController(ArticleRepository articleRepository, ModelMapper modelMapper) {
        this.articleRepository = articleRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    public ModelAndView getIndex(ModelAndView modelAndView, HttpServletRequest req) {
        CountryCodes wantedCode = super.getCurrentCookieLocale();
        Object[] all = this.articleRepository.getAllNestedSelect(CountryCodes.BG, wantedCode);

        List<ArticleLocalViewModel> localisedArticles = new ArrayList<>();
        for (Object article : all) {
            Object[] articleObjects = (Object[]) article;
            ArticleLocalViewModel articleLocalViewModel = new ArticleLocalViewModel();
            articleLocalViewModel.setId((Long) articleObjects[0]);
            articleLocalViewModel.setDate((Date) articleObjects[1]);
            LocalisedArticleContent localisedArticleContent = (LocalisedArticleContent) articleObjects[2];
            articleLocalViewModel.setLocalisedContent(this.modelMapper.map(localisedArticleContent, LocalisedArticleContentViewModel.class));
            localisedArticles.add(articleLocalViewModel);
        }
        modelAndView.addObject("localizedArticles", localisedArticles);
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @GetMapping("/home")
    public String index(HttpServletRequest request) {
        return "<h1 style=\"color: red;\">Under construction</h1>";
    }

}
