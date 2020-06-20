package architecture.web.controllers;

import architecture.constants.ViewNames;
import architecture.domain.CountryCodes;
import architecture.domain.models.viewModels.articles.ArticleLocalViewModel;
import architecture.services.interfaces.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/projects")
public class ProjectController extends BaseController {
    private final ArticleService articleService;

    @Autowired
    public ProjectController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping(value = "/category/{id}")
    public String projectsByCategory(ModelAndView modelAndView, @PathVariable Long id, Model model) {
        CountryCodes wantedCode = super.getCurrentCookieLocale();
        List<ArticleLocalViewModel> localisedArticles = this.articleService.findArticlesByCategory(id, wantedCode);
        model.addAttribute(ViewNames.PROJECTS_model_attribute_name, localisedArticles);
        return ViewNames.PROJECTS_BY_CATEGORY;
    }
}
