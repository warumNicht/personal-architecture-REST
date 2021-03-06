package architecture.web.controllers;

import architecture.constants.ViewNames;
import architecture.domain.CountryCodes;
import architecture.domain.models.bindingModels.articles.ArticleAddImageBindingModel;
import architecture.domain.models.bindingModels.articles.ArticleCreateModel;
import architecture.domain.models.bindingModels.articles.ArticleAddLangModel;
import architecture.domain.models.bindingModels.articles.ArticleEditLangModel;
import architecture.domain.models.serviceModels.CategoryServiceModel;
import architecture.domain.models.serviceModels.ImageServiceModel;
import architecture.domain.models.serviceModels.article.ArticleServiceModel;
import architecture.domain.models.serviceModels.article.LocalisedArticleContentServiceModel;
import architecture.domain.models.viewModels.articles.ArticleAddImageViewModel;
import architecture.domain.models.viewModels.articles.ArticleEditViewModel;
import architecture.domain.models.viewModels.articles.partial.*;
import architecture.error.NotFoundException;
import architecture.error.RestException;
import architecture.services.interfaces.ArticleService;
import architecture.services.interfaces.CategoryService;
import architecture.services.interfaces.ImageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static architecture.constants.AppConstants.DEFAULT_COUNTRY_CODE;

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

    @PostMapping("/create")
    public ResponseEntity createArticlePost(@Valid @RequestBody ArticleCreateModel bindingModel,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body(super.getBindingErrorsMap(bindingResult.getAllErrors()));
        }
        ArticleServiceModel article = new ArticleServiceModel(new Date());
        CategoryServiceModel category = this.categoryService.findById(bindingModel.getCategoryId());
        article.setCategory(category);
        LocalisedArticleContentServiceModel content = new LocalisedArticleContentServiceModel(bindingModel.getTitle(), bindingModel.getContent());
        article.getLocalContent().put(bindingModel.getCountry(), content);
        if (bindingModel.getMainImage()!=null) {
            ImageServiceModel mainImage = this.modelMapper.map(bindingModel.getMainImage(), ImageServiceModel.class);
            mainImage.getLocalImageNames().put(bindingModel.getCountry(), bindingModel.getMainImage().getName());
            mainImage.setArticle(article);
            article.setMainImage(mainImage);
        }
        this.articleService.createArticle(article);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(String.format("Successfully created article %s!", bindingModel.getTitle()));
    }

    @GetMapping("/addLang/{id}")
    public ResponseEntity addLanguageToArticle( @PathVariable(name = "id") Long articleId) {
        ArticleServiceModel article = this.articleService.findById(articleId);
        boolean hasMainImage = false;
        if (article.getMainImage() != null) {
            hasMainImage = true;
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hasMainImage);
    }

    @PostMapping("/addLang/{id}")
    public ResponseEntity addLanguageToArticlePost(@Valid @RequestBody ArticleAddLangModel model, BindingResult bindingResult,
                                                   @PathVariable(name = "id") Long articleId) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body(super.getBindingErrorsMap(bindingResult.getAllErrors()));
        }
        ArticleServiceModel article = this.articleService.findById(articleId);
        LocalisedArticleContentServiceModel localisedArticleContent = new LocalisedArticleContentServiceModel(model.getTitle(), model.getContent());
        article.getLocalContent().put(model.getCountry(), localisedArticleContent);
        if (article.getMainImage() != null) {
            article.getMainImage().getLocalImageNames().put(model.getCountry(), model.getMainImage());
        }
        this.articleService.updateArticle(article);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Successfully added language!");
    }

//    @GetMapping(value = "/edit/{id}/{lang}")
//    public ResponseEntity editArticleLang(@PathVariable(name = "id") Long id, @PathVariable(name = "lang") CountryCodes lang) {
//        ArticleServiceModel articleServiceModel = this.articleService.findById(id);
//        LocalisedArticleContentServiceModel localisedArticleContentServiceModel = articleServiceModel.getLocalContent().get(lang);
//        if (localisedArticleContentServiceModel == null) {
//            throw new NotFoundException("country.nonexistent");
//        }
//        ArticleEditLangModel bindingModel = this.modelMapper.map(localisedArticleContentServiceModel, ArticleEditLangModel.class);
//        if (articleServiceModel.getMainImage() != null) {
//            String imageName = articleServiceModel.getMainImage().getLocalImageNames().get(lang);
//            bindingModel.setMainImage(imageName == null ? "" : imageName);
//        }
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(bindingModel);
//    }

    @GetMapping(value = "/edit/{id}/{lang}/all")
    public ResponseEntity getWholeArticleByLang(@PathVariable(name = "id") Long id, @PathVariable(name = "lang") CountryCodes lang) {
        ArticleServiceModel articleServiceModel = this.articleService.findById(id);
        LocalisedArticleContentServiceModel localisedArticleContent = articleServiceModel.getLocalContent().get(lang);
        if (localisedArticleContent == null) {
            throw new NotFoundException("country.nonexistent");
        }

        WholeArticle article = this.modelMapper.map(articleServiceModel, WholeArticle.class);
        CountryCodes currentLang = super.getCurrentCookieLocale();
        article.setTitle(localisedArticleContent.getTitle());
        article.setContent(localisedArticleContent.getContent());



        AdminContent adminContent = new AdminContent();
        Map<String, Object> localTitles = articleServiceModel.getLocalContent().entrySet().stream()
                .collect(Collectors.toMap(kv -> kv.getKey().toString(), kv -> new TitleViewModel(kv.getValue().getTitle())));

        LanguageContent languageContent = new LanguageContent(localisedArticleContent.getTitle());
        languageContent.setContent(localisedArticleContent.getContent());

        localTitles.put(lang.toString(), languageContent);

        adminContent.setLocalContent(localTitles);



        if(articleServiceModel.getMainImage() != null){
            String localImageName = articleServiceModel.getMainImage().getLocalImageNames().get(lang);
            if(localImageName==null){
                localImageName = articleServiceModel.getMainImage().getLocalImageNames().get(DEFAULT_COUNTRY_CODE);
            }
            article.getMainImage().setName(localImageName);
            languageContent.setMainImageName(articleServiceModel.getMainImage().getLocalImageNames().get(lang));
        }

        article.setAdmin(adminContent);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(article);
    }

    @GetMapping(value = "/edit/{id}/{lang}")
    public ResponseEntity getArticleByLangAndParam(@PathVariable(name = "id") Long id,
                                                   @PathVariable(name = "lang") CountryCodes lang,
                                                   @RequestParam (name = "filter", required = false) String filter) {
        ArticleServiceModel articleServiceModel = this.articleService.findById(id);
        LocalisedArticleContentServiceModel localisedArticleContent = articleServiceModel.getLocalContent().get(lang);
        if (localisedArticleContent == null) {
            throw new NotFoundException("country.nonexistent");
        }

        if("content".equals(filter)){
            ContentImageNameViewModel contentImageNameViewModel = new ContentImageNameViewModel();
            contentImageNameViewModel.setContent(localisedArticleContent.getContent());
            if(articleServiceModel.getMainImage() != null){
                String localImageName = articleServiceModel.getMainImage().getLocalImageNames().get(lang);
                contentImageNameViewModel.setMainImageName(localImageName);
            }
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(contentImageNameViewModel);
        }

        Map<String, Object> localContent = articleServiceModel.getLocalContent().entrySet().stream()
                .collect(Collectors.toMap(kv -> kv.getKey().toString(), kv -> new TitleViewModel(kv.getValue().getTitle())));
        LanguageContent languageContent = new LanguageContent(localisedArticleContent.getTitle());
        languageContent.setContent(localisedArticleContent.getContent());

        if(articleServiceModel.getMainImage() != null){
            String localImageName = articleServiceModel.getMainImage().getLocalImageNames().get(lang);
            languageContent.setMainImageName(localImageName);
        }
        localContent.put(lang.toString(), languageContent);

        if("map".equals(filter)){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(localContent);

        }

        WholeArticle article = this.modelMapper.map(articleServiceModel, WholeArticle.class);
        article.setTitle(localisedArticleContent.getTitle());
        article.setContent(localisedArticleContent.getContent());

        if(articleServiceModel.getMainImage() != null){
            String localImageName = articleServiceModel.getMainImage().getLocalImageNames().get(lang);
            if(localImageName==null){
                localImageName = articleServiceModel.getMainImage().getLocalImageNames().get(DEFAULT_COUNTRY_CODE);
            }
            article.getMainImage().setName(localImageName);
            languageContent.setMainImageName(articleServiceModel.getMainImage().getLocalImageNames().get(lang));
        }

        AdminContent adminContent = new AdminContent();
        adminContent.setLocalContent(localContent);
        article.setAdmin(adminContent);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(article);
    }

    @GetMapping(value = "/edit/{id}/{lang}/admin")
    public ResponseEntity getAdminArticleByLang(@PathVariable(name = "id") Long id, @PathVariable(name = "lang") CountryCodes lang) {
        ArticleServiceModel articleServiceModel = this.articleService.findById(id);
        LocalisedArticleContentServiceModel localisedArticleContent = articleServiceModel.getLocalContent().get(lang);
        if (localisedArticleContent == null) {
            throw new NotFoundException("country.nonexistent");
        }

        Map<String, Object> localContent = articleServiceModel.getLocalContent().entrySet().stream()
                .collect(Collectors.toMap(kv -> kv.getKey().toString(), kv -> new TitleViewModel(kv.getValue().getTitle())));

        LanguageContent languageContent = new LanguageContent(localisedArticleContent.getTitle());
        languageContent.setContent(localisedArticleContent.getContent());

        if(articleServiceModel.getMainImage() != null){
            String localImageName = articleServiceModel.getMainImage().getLocalImageNames().get(lang);
            languageContent.setMainImageName(localImageName);
        }
        localContent.put(lang.toString(), languageContent);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(localContent);
    }

    @GetMapping(value = "/edit/{id}/{lang}/content")
    public ResponseEntity getArticleContentByLang(@PathVariable(name = "id") Long id, @PathVariable(name = "lang") CountryCodes lang) {
        ArticleServiceModel articleServiceModel = this.articleService.findById(id);
        LocalisedArticleContentServiceModel localisedArticleContent = articleServiceModel.getLocalContent().get(lang);
        if (localisedArticleContent == null) {
            throw new NotFoundException("country.nonexistent");
        }

        ContentImageNameViewModel contentImageNameViewModel = new ContentImageNameViewModel();
        contentImageNameViewModel.setContent(localisedArticleContent.getContent());
        if(articleServiceModel.getMainImage() != null){
            String localImageName = articleServiceModel.getMainImage().getLocalImageNames().get(lang);
            contentImageNameViewModel.setMainImageName(localImageName);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(contentImageNameViewModel);
    }

    @PatchMapping(value = "/edit/{id}/{lang}")
    public ResponseEntity editArticleLangPatch(@PathVariable(name = "id") Long id, @PathVariable(name = "lang") CountryCodes lang,
                                             @Valid @RequestBody ArticleEditLangModel model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body(super.getBindingErrorsMap(bindingResult.getAllErrors()));
        }
        ArticleServiceModel articleServiceModel = this.articleService.findById(id);
        LocalisedArticleContentServiceModel content = this.modelMapper.map(model, LocalisedArticleContentServiceModel.class);
        articleServiceModel.getLocalContent().put(lang, content);
        if (articleServiceModel.getMainImage() != null) {
            articleServiceModel.getMainImage().getLocalImageNames().put(lang, model.getMainImage());
        }
        this.articleService.updateArticle(articleServiceModel);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Successfully edited language!");
    }

    @GetMapping(value = "/edit/{id}")
    public ResponseEntity editArticle(@PathVariable(name = "id") Long id) {
        ArticleServiceModel article = this.articleService.findById(id);
        ArticleEditViewModel viewModel = this.modelMapper.map(article, ArticleEditViewModel.class);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(viewModel);
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
            LocalisedArticleContentServiceModel localContent = article.getLocalContent().get(DEFAULT_COUNTRY_CODE);
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
            return ResponseEntity.status(405).body(super.getBindingErrorsMap(bindingResult.getAllErrors()));
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

}
