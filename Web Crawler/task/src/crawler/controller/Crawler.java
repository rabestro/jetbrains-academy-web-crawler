package crawler.controller;

import crawler.model.PageContent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.System.Logger.Level.INFO;

public class Crawler {
    private static final System.Logger LOGGER = System.getLogger("");

    private PageContent content;

    public PageContent getPageContent(final String url) {
        try {
            final var doc = Jsoup.connect(url).get();
            final var links = doc.select("a[href]");
            final var title = doc.title();
            LOGGER.log(INFO, "Parse: {0}; Title: {1}.", url, title);
            final var data = links.eachAttr("abs:href").stream()
                    .distinct()
                    .map(Crawler::apply)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            data.put(url, title);
            content = new PageContent(url, title, data);
        } catch (IOException e) {
            e.printStackTrace();
            content = new PageContent(url, "", Collections.emptyMap());
        }
        return content;
    }

    public void save(String fileName) {
        LOGGER.log(INFO, "Save links to file {0}", fileName);
        try {
            Files.write(Paths.get(fileName), content.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map.Entry<String, String> apply(String link) {
        final Document doc;
        try {
            doc = Jsoup.connect(link).get();
            LOGGER.log(INFO, "Add: {0}, DocumentType: {1}, Body: {2}", link, doc.documentType(), doc.body());

            return doc.body().toString().contains("Web Page not found") ? null : Map.entry(link, doc.title());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
