package architecture.constants;

public final class ViewNames {
    public static final String DEFAULT_ERROR = "error/error";
    public static final String CONTROLLER_ERROR = "error/custom-error";

    //Image controller
    public static final String IMAGE_EDIT = "edit-image";
    public static final String IMAGE_EDIT_BindingModel_Name = "imageEdit";

    //Admin controller
    public static final String CATEGORY_CREATE = "categories/category-create";
    public static final String CATEGORY_EDIT = "categories/category-edit";
    public static final String CATEGORY_CREATE_binding_model_name = "categoryCreateModel";
    public static final String CATEGORY_EDIT_binding_model_name = "categoryEditModel";
    public static final String CATEGORIES_LIST = "categories/categories-list";
    public static final String ARTICLES_LIST_ALL = "listAll";

    //Article controller
    public static final String ARTICLE_CREATE = "articles/article-create";
    public static final String ARTICLE_CREATE_BindingModel_Name = "articleBinding";
    public static final String ARTICLE_FIELD_COUNTRY = "country";
    public static final String ARTICLE_CREATE_Category_Id = "categoryId";
    public static final String ARTICLE_EDIT = "articles/article-edit";
    public static final String ARTICLE_EDIT_LANG = "articles/article-edit-lang";
    public static final String ARTICLE_ADD_LANG = "articles/article-add-lang";
    public static final String ARTICLE_ADD_IMAGE = "articles/article-add-image";

    //User controller
    public static final String USER_REGISTER = "authentication/user-register";
    public static final String USER_REGISTER_binding_model = "userRegister";
    public static final String USER_LOGIN = "authentication/user-login";

    //Access controller
    public static final String UNAUTHORIZED = "error/unauthorized";

    //Project controller
    public static final String PROJECTS_BY_CATEGORY = "projects-category";
    public static final String PROJECTS_model_attribute_name = "articles";

}
