package architecture.services.interfaces;

import architecture.domain.CountryCodes;
import architecture.domain.models.serviceModels.article.ArticleServiceModel;
import architecture.domain.models.viewModels.articles.ArticleLocalViewModel;

import java.util.List;

public interface ArticleService {

    void createArticle(ArticleServiceModel article);

    ArticleServiceModel findById(Long id);

    List<ArticleServiceModel> findAll();

    void updateArticle(ArticleServiceModel article);

    List<ArticleLocalViewModel> findArticlesByCategory(Long id, CountryCodes wantedCode);
}
