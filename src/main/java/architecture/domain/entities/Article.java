package architecture.domain.entities;

import architecture.domain.CountryCodes;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "articles")
public class Article extends BaseEntity {
    @Column(name = "date", nullable = false)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category;

    @OneToOne(cascade = {CascadeType.ALL})
    private Image mainImage;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "localised_content", joinColumns = @JoinColumn(name = "article_id", nullable = false))
    @MapKeyColumn(name = "country_code", length = 2)
    @MapKeyEnumerated(EnumType.STRING)
    private Map<CountryCodes, LocalisedArticleContent> localContent = new HashMap<>();

    public Article() {
    }

    public Article(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Image getMainImage() {
        return mainImage;
    }

    public void setMainImage(Image mainImage) {
        this.mainImage = mainImage;
    }

    public Map<CountryCodes, LocalisedArticleContent> getLocalContent() {
        return localContent;
    }

    public void setLocalContent(Map<CountryCodes, LocalisedArticleContent> localContent) {
        this.localContent = localContent;
    }
}
