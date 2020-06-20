package architecture.web.controllers;

import architecture.constants.ViewNames;
import architecture.domain.CountryCodes;
import architecture.domain.models.bindingModels.CategoryCreateBindingModel;
import architecture.domain.models.bindingModels.CategoryEditBindingModel;
import architecture.domain.models.serviceModels.CategoryServiceModel;
import architecture.domain.models.serviceModels.article.ArticleServiceModel;
import architecture.services.interfaces.ArticleService;
import architecture.services.interfaces.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController extends BaseController {
    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @Autowired
    public AdminController(ArticleService articleService, CategoryService categoryService, ModelMapper modelMapper) {
        this.articleService = articleService;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @RequestMapping(method = {RequestMethod.GET}, value = "/listAll")
    public String listAll(Model model) {
        List<ArticleServiceModel> allArticles = this.articleService.findAll();
        model.addAttribute("allArticles", allArticles);
        return ViewNames.ARTICLES_LIST_ALL;
    }

    @GetMapping("/category/create")
    public String createCategory(Model modelView, @ModelAttribute(name = ViewNames.CATEGORY_CREATE_binding_model_name) CategoryCreateBindingModel model) {
        modelView.addAttribute("categoryCreateModel", model);
        return ViewNames.CATEGORY_CREATE;
    }

    @PostMapping("/category/create")
    public String createCategoryPost(@Valid @ModelAttribute(name = ViewNames.CATEGORY_CREATE_binding_model_name) CategoryCreateBindingModel bindingModel,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ViewNames.CATEGORY_CREATE;
        }
        CategoryServiceModel category = new CategoryServiceModel();
        category.getLocalCategoryNames().put(bindingModel.getCountry(), bindingModel.getName());
        this.categoryService.addCategory(category);
        return "redirect:/" + super.getLocale() + "/admin/category/list";
    }

    @ResponseBody
    @PostMapping("/category/create/rest")
    public String createREST(@RequestBody String name) {
        System.out.println(name);
        return name;
    }

    @RequestMapping(value = "/category/create/rest", method = RequestMethod.OPTIONS)
    public String createOptions() {
        System.out.println();
        return "options";
    }

    @GetMapping("/category/edit/{categoryId}")
    public String editCategory(Model model, @PathVariable(name = "categoryId") Long categoryId) {
        CategoryServiceModel category = this.categoryService.findById(categoryId);
        CategoryEditBindingModel bindingModel = new CategoryEditBindingModel();
        bindingModel.setId(categoryId);

        for (Map.Entry<CountryCodes, String> local : category.getLocalCategoryNames().entrySet()) {
            bindingModel.getLocalNames().put(local.getKey(), local.getValue());
        }
        model.addAttribute(ViewNames.CATEGORY_EDIT_binding_model_name, bindingModel);
        return ViewNames.CATEGORY_EDIT;
    }

    @PutMapping("/category/edit/{categoryId}")
    public String editCategoryPut(@Valid @ModelAttribute(name = ViewNames.CATEGORY_EDIT_binding_model_name) CategoryEditBindingModel model, BindingResult bindingResult,
                                  @PathVariable(name = "categoryId") Long categoryId) {
        this.categoryService.findById(categoryId);
        if (bindingResult.hasErrors()) {
            return ViewNames.CATEGORY_EDIT;
        }
        CategoryServiceModel categoryToUpdate = this.modelMapper.map(model, CategoryServiceModel.class);
        Map<CountryCodes, String> filteredValues = categoryToUpdate.getLocalCategoryNames().entrySet()
                .stream()
                .filter(entry -> !"".equals(entry.getValue()))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
        categoryToUpdate.setLocalCategoryNames(filteredValues);
        categoryToUpdate.setId(categoryId);

        this.categoryService.updateCategory(categoryToUpdate);
        return "redirect:/" + super.getLocale() + "/admin/category/list";
    }

    @GetMapping(value = "/category/list")
    public String listCategories() {
        return ViewNames.CATEGORIES_LIST;
    }
}
