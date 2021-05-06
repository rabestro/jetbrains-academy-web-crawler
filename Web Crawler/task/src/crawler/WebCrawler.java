package crawler;

import crawler.component.TablePanel;
import crawler.component.Toolbar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.System.Logger.Level.INFO;

public class WebCrawler extends JFrame implements ActionListener {
    private static final System.Logger LOGGER = System.getLogger("");
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Pattern TITLE = Pattern.compile(
            ".*<title>(.+)</title>.*",
            Pattern.DOTALL + Pattern.CASE_INSENSITIVE
    );
    private static final Pattern LINK = Pattern.compile(
            "<a [^>]*href\\s*=\\s*(['\"])(?<link>(?!javascript)[^\"'>]+)\\1"
            , Pattern.DOTALL + Pattern.CASE_INSENSITIVE
    );
    private final Toolbar toolbar = new Toolbar(this);
    private final TablePanel tablePanel = new TablePanel();

    {
        setTitle("Web Crawler");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setVisible(true);
        add(toolbar, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
    }

    public WebCrawler() {
        LOGGER.log(INFO, "Web Crawler started!");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LOGGER.log(INFO, "actionPerformed: {0}", e);
        final var url = toolbar.getURL();
        final var response = getResponse(url);

        LOGGER.log(INFO, response.map(HttpResponse::headers).map(HttpHeaders::toString).orElse("none"));

        final var title = response.map(this::getTitle).orElse("--- no page ---");
        toolbar.setTitle(title);

        UnaryOperator<String> absoluteLink = link -> link.startsWith("http") ? link :
                url.endsWith("/") || link.startsWith("/") ? url + link : url + "/" + link;

        final var links = response
                .map(HttpResponse::body)
                .map(LINK::matcher)
                .map(Matcher::results)
                .orElseGet(Stream::empty)
                .map(m -> m.group(2))
                .distinct()
                .map(absoluteLink)
                .map(s -> new String[]{s, getResponse(s).map(this::getTitle).orElse("--- none ---")})
                .filter(row -> row.length == 2)
                .toArray(String[][]::new);

        LOGGER.log(INFO, "Links collected: {0}.", links.length);
        tablePanel.setData(links);
        tablePanel.refresh();
    }

    //    private String getAbsoluteLink(String baseUrl, String link) {
//        return link.startsWith("http") ? link : baseUrl + link;
//    }
    private String[] getRow(String url) {
        return getResponse(url)
                .filter(this::isTextHtml)
                .map(HttpResponse::body)
                .map(TITLE::matcher)
                .map(s -> new String[]{url, s.replaceFirst("$1")})
                .orElse(new String[0]);
    }

    private String getTitle(HttpResponse<String> response) {
        return TITLE.matcher(response.body()).replaceFirst("$1");
    }

    private boolean isTextHtml(HttpResponse<String> response) {
        return response.headers()
                .firstValue("content-type")
                .map(s -> s.contains("text/html"))
                .orElse(false);
    }

    private Optional<HttpResponse<String>> getResponse(String url) {
        final var request = HttpRequest.newBuilder(URI.create(url)).GET().build();
        try {
            return Optional.ofNullable(client.send(request, HttpResponse.BodyHandlers.ofString()));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}