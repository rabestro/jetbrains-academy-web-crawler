package crawler;

import crawler.component.LinkTableModel;
import crawler.component.TablePanel;
import crawler.component.Toolbar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.regex.MatchResult;
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
            "<a [^>]*>"
            , Pattern.DOTALL + Pattern.CASE_INSENSITIVE
    );
    private final Toolbar toolbar = new Toolbar(this);
    private final LinkTableModel tableModel = new LinkTableModel();
    private final JTable table = new JTable(tableModel);
    private final JScrollPane sp = new JScrollPane(table);
    private final TablePanel tablePanel = new TablePanel();

    {
        setTitle("Web Crawler");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setVisible(true);
        add(toolbar, BorderLayout.NORTH);
        table.setName("TitlesTable");
        add(tablePanel, BorderLayout.CENTER);
    }

    public WebCrawler() {
        LOGGER.log(INFO, "Web Crawler started!");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LOGGER.log(INFO, "actionPerformed: {0}", e);
        final var response = getResponse(toolbar.getURL());

        final var title = response
                .map(HttpResponse::body)
                .map(TITLE::matcher)
                .map(m -> m.replaceFirst("$1"))
                .orElse("--- no page ---");
        toolbar.setTitle(title);

        final var links = response
                .map(HttpResponse::body)
                .map(LINK::matcher)
                .map(Matcher::results)
                .orElseGet(Stream::empty)
                .map(MatchResult::group)
                .distinct()
                .map(link -> new String[]{link, ""})
                .toArray(String[][]::new);

        LOGGER.log(INFO, "Links collected: {0}.", links.length);
        tablePanel.setData(links);
        tablePanel.refresh();
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