package architecture.web.controllers;

import architecture.constants.AppConstants;
import architecture.constants.ViewNames;
import architecture.domain.CountryCodes;
import architecture.domain.models.bindingModels.articles.ArticleAddImageBindingModel;
import architecture.domain.models.bindingModels.articles.ArticleCreateBindingModel;
import architecture.domain.models.bindingModels.articles.ArticleLangBindingModel;
import architecture.domain.models.serviceModels.CategoryServiceModel;
import architecture.domain.models.serviceModels.ImageServiceModel;
import architecture.domain.models.serviceModels.article.ArticleServiceModel;
import architecture.domain.models.serviceModels.article.LocalisedArticleContentServiceModel;
import architecture.domain.models.viewModels.articles.ArticleAddImageViewModel;
import architecture.domain.models.viewModels.articles.ArticleEditViewModel;
import architecture.error.NotFoundException;
import architecture.error.RestException;
import architecture.services.interfaces.ArticleService;
import architecture.services.interfaces.CategoryService;
import architecture.services.interfaces.ImageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "/admin/articles")
@PreAuthorize(value = "hasRole('ADMIN')")
public class ArticleController extends BaseController {
    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final ImageService imageService;
    private final ModelMapper modelMapper;

    @Autowired
    public ArticleController(ArticleService articleService, CategoryService categoryService, ImageService imageService, ModelMapper modelMapper) {
        this.articleService = articleService;
        this.categoryService = categoryService;
        this.imageService = imageService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/create")
    public String createArticle(@ModelAttribute(name = "articleBinding") ArticleCreateBindingModel articleCreateBindingModel, Model model) {
        model.addAttribute("articleBinding", articleCreateBindingModel);
        model.addAttribute("categoryId", "");
        return ViewNames.ARTICLE_CREATE;
    }

    @PostMapping("/create")
    public String createArticlePost(@Valid @ModelAttribute(name = "articleBinding") ArticleCreateBindingModel bindingModel,
                                    BindingResult bindingResult, @RequestParam(name = "categoryId") Long categoryId) {
        if (bindingResult.hasErrors()) {
            return ViewNames.ARTICLE_CREATE;
        }
        ArticleServiceModel article = new ArticleServiceModel(new Date());
        CategoryServiceModel category = this.categoryService.findById(categoryId);
        article.setCategory(category);
        LocalisedArticleContentServiceModel content = new LocalisedArticleContentServiceModel(bindingModel.getTitle(), bindingModel.getContent());
        article.getLocalContent().put(bindingModel.getCountry(), content);
        if (!"".equals(bindingModel.getMainImage().getUrl())) {
            ImageServiceModel mainImage = this.modelMapper.map(bindingModel.getMainImage(), ImageServiceModel.class);
            mainImage.getLocalImageNames().put(bindingModel.getCountry(), bindingModel.getMainImage().getName());
            mainImage.setArticle(article);
            article.setMainImage(mainImage);
        }
        this.articleService.createArticle(article);
        return "redirect:/" + super.getLocale() + "/admin/listAll";
    }

    @GetMapping("/addLang/{id}")
    public String addLanguageToArticle(Model modelView, @PathVariable(name = "id") Long articleId,
                                       @ModelAttribute(name = "articleBinding") ArticleLangBindingModel model) {
        ArticleServiceModel article = this.articleService.findById(articleId);
        if (article.getMainImage() != null) {
            model.setMainImage("");
        }
        modelView.addAttribute("articleBinding", model);
        return ViewNames.ARTICLE_ADD_LANG;
    }

    @PostMapping("/addLang/{id}")
    public String addLanguageToArticlePost(@Valid @ModelAttribute(name = "articleBinding") ArticleLangBindingModel model, BindingResult bindingResult,
                                           @PathVariable(name = "id") Long articleId) {
        if (bindingResult.hasErrors()) {
            return ViewNames.ARTICLE_ADD_LANG;
        }
        ArticleServiceModel article = this.articleService.findById(articleId);
        LocalisedArticleContentServiceModel localisedArticleContent = new LocalisedArticleContentServiceModel(model.getTitle(), model.getContent());
        article.getLocalContent().put(model.getCountry(), localisedArticleContent);
        if (article.getMainImage() != null) {
            article.getMainImage().getLocalImageNames().put(model.getCountry(), model.getMainImage());
        }
        this.articleService.updateArticle(article);
        return "redirect:/" + super.getLocale() + "/admin/articles/edit/" + articleId;
    }

    @GetMapping(value = "/edit/{id}/{lang}")
    public String editArticleLang(Model modelView, @PathVariable(name = "id") Long id, @PathVariable(name = "lang") CountryCodes lang) {
        ArticleServiceModel articleServiceModel = this.articleService.findById(id);
        LocalisedArticleContentServiceModel localisedArticleContentServiceModel = articleServiceModel.getLocalContent().get(lang);
        if (localisedArticleContentServiceModel == null) {
            throw new NotFoundException("country.nonexistent");
        }
        ArticleLangBindingModel bindingModel = this.modelMapper.map(localisedArticleContentServiceModel, ArticleLangBindingModel.class);
        bindingModel.setId(id);
        if (articleServiceModel.getMainImage() != null) {
            String imageName = articleServiceModel.getMainImage().getLocalImageNames().get(lang);
            bindingModel.setMainImage(imageName == null ? "" : imageName);
        }
        modelView.addAttribute("articleEditLang", bindingModel);
        return ViewNames.ARTICLE_EDIT_LANG;
    }

    @ResponseBody
    @RequestMapping(method = {RequestMethod.PATCH}, value = "/edit", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public Object editArticleLangPut(@Valid @RequestBody ArticleLangBindingModel model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return this.getBindingErrorsMap(bindingResult.getAllErrors());
        }
        ArticleServiceModel articleServiceModel = this.articleService.findById(model.getId());
        LocalisedArticleContentServiceModel content = this.modelMapper.map(model, LocalisedArticleContentServiceModel.class);
        articleServiceModel.getLocalContent().put(model.getCountry(), content);
        if (articleServiceModel.getMainImage() != null) {
            articleServiceModel.getMainImage().getLocalImageNames().put(model.getCountry(), model.getMainImage());
        }
        this.articleService.updateArticle(articleServiceModel);
        return "\"/" + super.getLocale() + "/admin/articles/edit/" + model.getId() + "\"";
    }

    @GetMapping(value = "/edit/{id}")
    public String editArticle(Model model, @PathVariable(name = "id") Long id) {
        ArticleServiceModel article = this.articleService.findById(id);
        ArticleEditViewModel viewModel = this.modelMapper.map(article, ArticleEditViewModel.class);
        model.addAttribute("articleEdit", viewModel);
        return ViewNames.ARTICLE_EDIT;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PATCH, value = "/change-category/{articleId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public Object changeCategory(@RequestBody Long categoryId, @PathVariable(name = "articleId") Long articleId) throws RestException {
        try {
            CountryCodes cookieLocale = super.getCurrentCookieLocale();
            ArticleServiceModel article = this.articleService.findById(articleId);
            String oldCategoryName = article.getCategory().getLocalCategoryNames().get(cookieLocale);
            CategoryServiceModel newCategory = this.categoryService.findById(categoryId);
            String newCategoryName = newCategory.getLocalCategoryNames().get(cookieLocale);
            article.setCategory(newCategory);
            this.articleService.updateArticle(article);
            HashMap<String, Object> response = new HashMap<>();
            response.put("oldCategoryName", oldCategoryName);
            response.put("newCategoryName", newCategoryName);
            response.put("title",
                    article.getLocalContent().get(cookieLocale) != null ? article.getLocalContent().get(cookieLocale).getTitle() : null);
            return response;
        } catch (NotFoundException e) {
            throw new RestException(e.getMessage());
        }
    }

    @GetMapping(value = "/add-image/{id}")
    public String articleAddImage(@PathVariable(name = "id") Long id, Model model) {
        ArticleServiceModel article = this.articleService.findById(id);
        LocalisedArticleContentServiceModel content = article.getLocalContent().get(super.getCurrentCookieLocale());
        if (content == null) {
            LocalisedArticleContentServiceModel localContent = article.getLocalContent().get(AppConstants.DEFAULT_COUNTRY_CODE);
            content = localContent != null ? localContent : article.getLocalContent().entrySet().iterator().next().getValue();
        }
        ArticleAddImageViewModel addImageViewModel;
        if (article.getMainImage() != null) {
            addImageViewModel = new ArticleAddImageViewModel(id, content.getTitle(), article.getMainImage().getUrl());
        } else {
            addImageViewModel = new ArticleAddImageViewModel(id, content.getTitle());
        }
        model.addAttribute("article", addImageViewModel);
        return ViewNames.ARTICLE_ADD_IMAGE;
    }

    @ResponseBody
    @RequestMapping(method = {RequestMethod.PUT}, value = "/add-image/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity articleAddImagePut(@Valid @RequestBody ArticleAddImageBindingModel image, BindingResult bindingResult,
                                             @PathVariable(name = "id") Long id,
                                             @RequestParam(value = "main", required = false) boolean isMain) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(405).body(this.getBindingErrorsMap(bindingResult.getAllErrors()));
        }
        ArticleServiceModel article = this.articleService.findById(id);
        ImageServiceModel imageServiceModel = new ImageServiceModel(image.getImage().getUrl());
        imageServiceModel.getLocalImageNames().put(image.getLang(), image.getImage().getName());
        imageServiceModel.setArticle(article);
        if (isMain) {
            article.setMainImage(imageServiceModel);
            this.articleService.updateArticle(article);
        } else {
            this.imageService.saveImage(imageServiceModel);
        }
        return ResponseEntity.status(202).body("\"/" + super.getLocale() + "/admin/articles/edit/" + id + "\"");
    }

    private HashMap<String, List<String>> getBindingErrorsMap(List<ObjectError> allErrors) {
        HashMap<String, List<String>> errors = new HashMap<>();
        for (ObjectError currentError : allErrors) {
            FieldError fieldError = (FieldError) currentError;
            errors.putIfAbsent(fieldError.getField(), new ArrayList<>());
            errors.get(fieldError.getField()).add(fieldError.getDefaultMessage());
        }
        return errors;
    }

}
