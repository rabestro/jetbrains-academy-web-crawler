package crawler.model;

import java.util.Map;

public class PageContent {
    private final String url;
    private final String title;
    private final Map<String, String> links;

    public PageContent(String url, String title, Map<String, String> links) {
        this.url = url;
        this.title = title;
        this.links = links;
    }

    public String getTitle() {
        return title;
    }

    public String[][] getLinks() {
        return links.entrySet().stream()
                .map(e -> new String[]{e.getKey(), e.getValue()})
                .toArray(String[][]::new);
    }
}
