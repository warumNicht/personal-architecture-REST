package architecture.domain.models.viewModels.articles.partial;

import java.util.Map;

public class AdminContent {
    private Map<String, Object> localContent;

    public Map<String, Object> getLocalContent() {
        return localContent;
    }

    public void setLocalContent(Map<String, Object> localContent) {
        this.localContent = localContent;
    }
}
