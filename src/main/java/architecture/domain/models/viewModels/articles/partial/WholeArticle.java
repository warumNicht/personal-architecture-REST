package architecture.domain.models.viewModels.articles.partial;

import architecture.domain.models.viewModels.ImageLocaleViewModel;

public class WholeArticle {
    private Long id;
    private Long categoryId;
    private ImageNameUrl mainImage;
    private String title;
    private String content;
    private AdminContent adminContent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public ImageNameUrl getMainImage() {
        return mainImage;
    }

    public void setMainImage(ImageNameUrl mainImage) {
        this.mainImage = mainImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AdminContent getAdminContent() {
        return adminContent;
    }

    public void setAdminContent(AdminContent adminContent) {
        this.adminContent = adminContent;
    }
}
