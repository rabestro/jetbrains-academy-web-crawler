package crawler;

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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import static java.lang.System.Logger.Level.INFO;

public class WebCrawler extends JFrame implements ActionListener {
    private static final System.Logger LOGGER = System.getLogger("");
    private static final HttpClient client = HttpClient.newHttpClient();

    private final Toolbar toolbar = new Toolbar(this);
    private final JTextArea textArea = new JTextArea("HTML code?");

    {
        setTitle("Web Crawler");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setVisible(true);

        add(toolbar, BorderLayout.NORTH);
        textArea.setName("HtmlTextArea");
        textArea.setEnabled(false);
        add(textArea, BorderLayout.CENTER);
    }

    public WebCrawler() {
        LOGGER.log(INFO, "Web Crawler started!");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LOGGER.log(INFO, "actionPerformed: " + e);
        try {
            final var doc = Jsoup.connect(toolbar.getURL()).get();
            textArea.setText(doc.wholeText());
            toolbar.setTitle(doc.title());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
//        final var request = HttpRequest.newBuilder(URI.create(toolbar.getURL())).GET().build();
//        try {
//            final var response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            textArea.setText(response.body());
//            final var title = response.body().replaceFirst("(?is).*<title>(.+)</title>.*", "$1");
//            toolbar.setTitle(title);
//        } catch (IOException | InterruptedException ioException) {
//            ioException.printStackTrace();
//        }
    }
}