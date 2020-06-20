package architecture.domain.entities;

import architecture.constants.AppConstants;
import architecture.domain.CountryCodes;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "images")
public class Image extends BaseEntity {
    @Column(name = "image_url", columnDefinition = "TEXT", nullable = false)
    private String url;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "image_names", joinColumns = @JoinColumn(name = "image_id", nullable = false))
    @MapKeyColumn(name = "country_code", length = 2)
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "image_names", nullable = false, length = AppConstants.NAME_MAX_LENGTH)
    private Map<CountryCodes, String> localImageNames = new HashMap<>();

    @OneToOne
    @JoinColumn(name = "article_id", referencedColumnName = "id", nullable = false)
    private Article article;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<CountryCodes, String> getLocalImageNames() {
        return localImageNames;
    }

    public void setLocalImageNames(Map<CountryCodes, String> localImageNames) {
        this.localImageNames = localImageNames;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
