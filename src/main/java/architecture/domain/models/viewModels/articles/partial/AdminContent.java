package architecture.domain.models.viewModels.articles.partial;

import java.util.Map;

public class AdminContent {
    private Map<String, String> localTitles;
    private LanguageContent languageContent;

    public Map<String, String> getLocalTitles() {
        return localTitles;
    }

    public void setLocalTitles(Map<String, String> localTitles) {
        this.localTitles = localTitles;
    }

    public LanguageContent getLanguageContent() {
        return languageContent;
    }

    public void setLanguageContent(LanguageContent languageContent) {
        this.languageContent = languageContent;
    }
}
