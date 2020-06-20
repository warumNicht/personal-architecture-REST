package architecture.repositories;

import architecture.domain.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query(value = "SELECT i FROM Image i " +
            "WHERE i.article.id = :articleId")
    List<Image> getImagesByArticle(@Param(value = "articleId") Long articleId);
}
