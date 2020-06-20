package architecture.services;

import architecture.domain.CountryCodes;
import architecture.domain.entities.Article;
import architecture.domain.entities.LocalisedArticleContent;
import architecture.domain.models.serviceModels.article.ArticleServiceModel;
import architecture.domain.models.viewModels.ImageLocaleViewModel;
import architecture.domain.models.viewModels.LocalisedArticleContentViewModel;
import architecture.domain.models.viewModels.articles.ArticleLocalViewModel;
import architecture.error.NotFoundException;
import architecture.repositories.ArticleRepository;
import architecture.services.interfaces.ArticleService;
import architecture.util.LocaleMessageUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository, ModelMapper modelMapper) {
        this.articleRepository = articleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void createArticle(ArticleServiceModel article) {
        Article articleToCreate = this.modelMapper.map(article, Article.class);
        this.articleRepository.save(articleToCreate);
    }

    @Override
    public ArticleServiceModel findById(Long id) {
        Article article = this.articleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(LocaleMessageUtil.getLocalizedMessage("notFound.article")));
        ArticleServiceModel articleServiceModel = this.modelMapper.map(article, ArticleServiceModel.class);
        return articleServiceModel;
    }

    @Override
    public List<ArticleServiceModel> findAll() {
        return this.articleRepository.findAll().stream()
                .map(article -> this.modelMapper.map(article, ArticleServiceModel.class)).collect(Collectors.toList());
    }

    @Override
    public void updateArticle(ArticleServiceModel article) {
        Article articleToUpdate = this.modelMapper.map(article, Article.class);
        this.articleRepository.saveAndFlush(articleToUpdate);
    }

    @Override
    public List<ArticleLocalViewModel> findArticlesByCategory(Long id, CountryCodes wantedCode) {
        Object[] all;
        all = id == 555 ? this.articleRepository.findAllArticles(CountryCodes.BG, wantedCode)
                : this.articleRepository.getAllByCategory(CountryCodes.BG, wantedCode, id);

        List<ArticleLocalViewModel> localisedArticles = new ArrayList<>();
        for (Object article : all) {
            Object[] articleObjects = (Object[]) article;
            ArticleLocalViewModel articleLocalViewModel = new ArticleLocalViewModel();
            articleLocalViewModel.setId((Long) articleObjects[0]);
            if (articleObjects[1] != null) {
                articleLocalViewModel
                        .setMainImage(new ImageLocaleViewModel((String) articleObjects[1], (String) articleObjects[2]));
            }
            articleLocalViewModel.setDate((Date) articleObjects[3]);
            LocalisedArticleContent localisedArticleContent = (LocalisedArticleContent) articleObjects[4];
            articleLocalViewModel.setLocalisedContent(
                    this.modelMapper.map(localisedArticleContent, LocalisedArticleContentViewModel.class));
            localisedArticles.add(articleLocalViewModel);
        }
        return localisedArticles;
    }
}
