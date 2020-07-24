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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
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

    @PostMapping("/category/create")
    public ResponseEntity createCategoryPost(@Valid @RequestBody CategoryCreateBindingModel bindingModel,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body(super.getBindingErrorsMap(bindingResult.getAllErrors()));
        }
        CategoryServiceModel category = new CategoryServiceModel();
        category.getLocalCategoryNames().put(bindingModel.getCountry(), bindingModel.getName());
        this.categoryService.addCategory(category);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(String.format("Successfully created category %s !", bindingModel.getName()));
    }

    @GetMapping("/category/edit/{categoryId}")
    public ResponseEntity editCategory( @PathVariable(name = "categoryId") Long categoryId) {
        CategoryServiceModel category = this.categoryService.findById(categoryId);
        CategoryEditBindingModel bindingModel = new CategoryEditBindingModel();
        bindingModel.setId(categoryId);

        for (Map.Entry<CountryCodes, String> local : category.getLocalCategoryNames().entrySet()) {
            bindingModel.getLocalNames().put(local.getKey(), local.getValue());
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bindingModel);
    }

    @PutMapping(value = "/category/edit/{categoryId}")
    public ResponseEntity editCategoryPut(@Valid @RequestBody CategoryEditBindingModel model, BindingResult bindingResult,
                                  @PathVariable(name = "categoryId") Long categoryId) {
        this.categoryService.findById(categoryId);
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body(super.getBindingErrorsMap(bindingResult.getAllErrors()));
        }
        CategoryServiceModel categoryToUpdate = this.modelMapper.map(model, CategoryServiceModel.class);
        Map<CountryCodes, String> filteredValues = categoryToUpdate.getLocalCategoryNames().entrySet()
                .stream()
                .filter(entry -> !"".equals(entry.getValue()))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
        categoryToUpdate.setLocalCategoryNames(filteredValues);
        categoryToUpdate.setId(categoryId);

        this.categoryService.updateCategory(categoryToUpdate);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Successfully edited!");
    }

}
