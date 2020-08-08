package architecture.web.controllers;

import architecture.constants.ViewNames;
import architecture.domain.CountryCodes;
import architecture.domain.models.viewModels.articles.ArticleLocalViewModel;
import architecture.domain.models.viewModels.articles.ArticleSimpleViewModel;
import architecture.services.interfaces.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity projectsByCategory(@PathVariable Long id) {
        CountryCodes wantedCode = super.getCurrentCookieLocale();
        List<ArticleLocalViewModel> localisedArticles = this.articleService.findArticlesByCategory(id, wantedCode);
        return ResponseEntity.status(HttpStatus.OK)
                .body(localisedArticles);
    }

    @GetMapping(value = "/category/all")
    public ResponseEntity allProjects() {
        CountryCodes wantedCode = super.getCurrentCookieLocale();
//        List<ArticleLocalViewModel> localisedArticles = this.articleService.findAllLocalisedArticles(wantedCode);
        List<ArticleSimpleViewModel> articles = this.articleService.findAllLocalisedArticles2(wantedCode);
        return ResponseEntity.status(HttpStatus.OK)
                .body(articles);
    }
}
