package architecture.services;

import architecture.domain.entities.Image;
import architecture.domain.models.serviceModels.ImageServiceModel;
import architecture.error.NotFoundException;
import architecture.repositories.ImageRepository;
import architecture.services.interfaces.ImageService;
import architecture.util.LocaleMessageUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository, ModelMapper modelMapper) {
        this.imageRepository = imageRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void saveImage(ImageServiceModel imageServiceModel) {
        Image image = this.modelMapper.map(imageServiceModel, Image.class);
        this.imageRepository.saveAndFlush(image);
    }

    @Override
    public List<ImageServiceModel> getImagesByArticle(Long articleId) {
        return this.imageRepository.getImagesByArticle(articleId).stream()
                .map(image -> this.modelMapper.map(image, ImageServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public ImageServiceModel getImageById(Long id) {
        return this.modelMapper.map(
                this.imageRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException(LocaleMessageUtil.getLocalizedMessage("archSentence"))), ImageServiceModel.class);
    }
}
