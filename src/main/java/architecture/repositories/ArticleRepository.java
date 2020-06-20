package architecture.repositories;

import architecture.domain.CountryCodes;
import architecture.domain.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query(value = "SELECT value(m) " +
            "FROM Article a " +
            "JOIN a.localContent m " +
            "WHERE  a.id=:id and KEY(m) = :countryCode or  KEY(m) = 'BG' ")
    Object[] getValue(@Param(value = "countryCode") CountryCodes countryCode, @Param(value = "id") Long id);

    @Query(value = "SELECT a.id, a.date, value(m) " +
            "FROM Article a " +
            "JOIN a.localContent m " +
            "WHERE  KEY(m) = :countryCode OR KEY(m) = :defaultCode " +
            "GROUP BY a.id " +
            "ORDER BY KEY(m) DESC")
    Object[] getAll(@Param(value = "countryCode") CountryCodes countryCode, @Param(value = "defaultCode") CountryCodes defaultCode);

    @Query(value = "SELECT a.id, a.date, value(m)" +
            "FROM Article a " +
            "JOIN a.localContent m " +
            "ON key(m) = ( SELECT max(key(n)) FROM Article  b " +
            "JOIN b.localContent n " +
            "WHERE a.id=b.id AND (KEY(n) = :countryCode OR KEY(n) = :defaultCode )) ")
    Object[] getAllNestedSelect(@Param(value = "countryCode") CountryCodes countryCode, @Param(value = "defaultCode") CountryCodes defaultCode);

    @Query(value = "SELECT a.id,i.url,value(imNam) , a.date, value(m)" +
            "FROM Article a " +
            "LEFT JOIN Image  as i ON i.id=a.mainImage.id " +
            "LEFT JOIN i.localImageNames imNam ON key(imNam) = " +
            "( SELECT max(key(imLoc)) FROM Image i2 " +
            "JOIN i2.localImageNames imLoc WHERE i2.id=i.id AND (KEY(imLoc) = :countryCode OR KEY(imLoc) = :defaultCode )) " +
            "JOIN a.localContent m " +
            "ON key(m) = ( SELECT max(key(n)) FROM Article  b " +
            "JOIN b.localContent n " +
            "WHERE a.id=b.id AND (KEY(n) = :countryCode OR KEY(n) = :defaultCode )) ")
    Object[] findAllArticles(@Param(value = "countryCode") CountryCodes countryCode, @Param(value = "defaultCode") CountryCodes defaultCode);

    @Query(value = "SELECT a.id,i.url,value(imNam) , a.date, value(m)" +
            "FROM Article a " +
            "LEFT JOIN Image  as i ON i.id=a.mainImage.id " +
            "LEFT JOIN i.localImageNames imNam ON key(imNam) = " +
            "( SELECT max(key(imLoc)) FROM Image i2 " +
            "JOIN i2.localImageNames imLoc WHERE i2.id=i.id AND (KEY(imLoc) = :countryCode OR KEY(imLoc) = :defaultCode )) " +
            "JOIN a.localContent m " +
            "ON key(m) = ( SELECT max(key(n)) FROM Article  b " +
            "JOIN b.localContent n " +
            "WHERE a.category.id=:categoryId AND a.id=b.id AND (KEY(n) = :countryCode OR KEY(n) = :defaultCode )) ")
    Object[] getAllByCategory(@Param(value = "countryCode") CountryCodes countryCode, @Param(value = "defaultCode") CountryCodes defaultCode,
                              @Param(value = "categoryId") Long categoryId);

    @Query(value = "SELECT a.id, value(m) " +
            "FROM Article a, Article b " +
            "JOIN a.localContent m " +
            "ON key(m) = (SELECT max(key(n)) from Article b " +
            "Join b.localContent n " +
            "WHERE  (KEY(n) = :countryCode OR KEY(n) = :defaultCode) and b.id=a.id ) " +
            "GROUP BY a.id")
    Object[] getAllMax(@Param(value = "countryCode") CountryCodes countryCode, @Param(value = "defaultCode") CountryCodes defaultCode);

    @Query(value = "SELECT a.id,a.date, con.article_title, con.article_content FROM  articles AS a " +
            "JOIN (SELECT s1.article_id,s1.article_title, s1.article_content, s1.country_code " +
            "FROM localised_content s1 " +
            "JOIN " +
            "(SELECT article_id, article_title, MAX(country_code) AS country " +
            "FROM localised_content\n" +
            "WHERE country_code= :countryCode OR country_code= :defaultCode " +
            "GROUP BY article_id) AS s2 " +
            "ON s1.article_id = s2.article_id AND s1.country_code = s2.country " +
            "ORDER BY article_id) as con " +
            "on a.id=con.article_id;", nativeQuery = true)
    Object[] getAllNativeQuery(@Param(value = "countryCode") String countryCode, @Param(value = "defaultCode") String defaultCode);
}
