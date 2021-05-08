package crawler.controller;

import crawler.model.PageContent;
import crawler.view.WebCrawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.System.Logger.Level.INFO;
import static java.util.Objects.isNull;

public class Crawler {
    private static final System.Logger LOGGER = System.getLogger("");

    private PageContent content;

    public PageContent getPageContent(final String url) {
        final Document doc;
        try {
            doc = Jsoup.connect(url).get();
            final var links = doc.select("a[href]");
            final var title = doc.title();
            final var data = links.eachAttr("abs:href").stream()
                    .distinct()
                    .map(Crawler::apply)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
            content = new PageContent(url, title, data);
        } catch (IOException e) {
            e.printStackTrace();
            content = new PageContent(url, "", Collections.emptyMap());
        }
        return content;
    }

    private static Map.Entry<String, String> apply(String link) {
        final Document doc;
        try {
            doc = Jsoup.connect(link).get();
            LOGGER.log(INFO, "Add: {0}, DocumentType: {1}", link, doc.documentType());
            return isNull(doc.documentType()) ? null : Map.entry(link, doc.title());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
