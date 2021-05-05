package crawler;

import crawler.component.Toolbar;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Pattern;

import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.INFO;

public class WebCrawler extends JFrame implements ActionListener {
    private static final System.Logger LOGGER = System.getLogger("");
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Pattern TITLE = Pattern.compile(
            ".*<title>(.+)</title>.*",
            Pattern.DOTALL + Pattern.CASE_INSENSITIVE
    );
    private static final Pattern LINK = Pattern.compile(
            "(?<=href=[\"'])(?<link>.+)(?=[\"']>)"
            , Pattern.DOTALL + Pattern.CASE_INSENSITIVE
    );
    private final Toolbar toolbar = new Toolbar(this);
    private final TableModel tableModel = new LinkTableModel();
    private final JTable table = new JTable(tableModel);
    private final JScrollPane sp = new JScrollPane(table);

    {
        setTitle("Web Crawler");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setVisible(true);
        add(toolbar, BorderLayout.NORTH);
        table.setName("TitlesTable");
        add(sp, BorderLayout.CENTER);
    }

    public WebCrawler() {
        LOGGER.log(INFO, "Web Crawler started!");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LOGGER.log(INFO, "actionPerformed: {0}", e);

        final var request = HttpRequest.newBuilder(URI.create(toolbar.getURL())).GET().build();
        try {
            final var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            LOGGER.log(INFO, response.headers());
            LOGGER.log(INFO, response.headers().firstValue("content-type"));
            LOGGER.log(INFO, response.headers().allValues("content-type"));
            final var title = TITLE.matcher(response.body()).replaceFirst("$1");
            toolbar.setTitle(title);
        } catch (IOException | InterruptedException ioException) {
            ioException.printStackTrace();
        }
    }
}