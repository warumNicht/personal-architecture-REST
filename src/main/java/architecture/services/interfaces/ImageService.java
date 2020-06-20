package architecture.services.interfaces;

import architecture.domain.models.serviceModels.ImageServiceModel;

import java.util.List;

public interface ImageService {
    void saveImage(ImageServiceModel imageServiceModel);


    List<ImageServiceModel> getImagesByArticle(Long articleId);

    ImageServiceModel getImageById(Long id);
}
