package architecture.web.controllers;

import architecture.constants.ViewNames;
import architecture.domain.CountryCodes;
import architecture.domain.models.bindingModels.images.ImageEditBindingModel;
import architecture.domain.models.serviceModels.ImageServiceModel;
import architecture.services.interfaces.ImageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/admin/images")
@PreAuthorize(value = "hasRole('ADMIN')")
public class ImageController extends BaseController {
    private final ImageService imageService;
    private final ModelMapper modelMapper;

    @Autowired
    public ImageController(ImageService imageService, ModelMapper modelMapper) {
        this.imageService = imageService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/edit/{id}")
    public String editImage(@PathVariable Long id, Model model) {
        ImageServiceModel imageById = this.imageService.getImageById(id);
        ImageEditBindingModel imageToEdit = this.modelMapper.map(imageById, ImageEditBindingModel.class);
        for (CountryCodes value : CountryCodes.values()) {
            imageToEdit.getLocalImageNames().putIfAbsent(value, "");
        }
        model.addAttribute("imageEdit", imageToEdit);
        return ViewNames.IMAGE_EDIT;
    }

    @PutMapping(value = "/edit/{imageId}")
    public String editImagePut(@Valid @ModelAttribute(name = ViewNames.IMAGE_EDIT_BindingModel_Name) ImageEditBindingModel model, BindingResult bindingResult,
                               @PathVariable(name = "imageId") Long imageId) {
        if (bindingResult.hasErrors()) {
            return ViewNames.IMAGE_EDIT;
        }
        model.getLocalImageNames().entrySet().removeIf(kv -> kv.getValue().isEmpty());
        ImageServiceModel imageById = this.imageService.getImageById(imageId);
        imageById.setUrl(model.getUrl());
        imageById.setLocalImageNames(model.getLocalImageNames());
        this.imageService.saveImage(imageById);
        return "redirect:/" + super.getLocale() + "/admin/articles/edit/" + imageById.getArticle().getId();
    }
}
