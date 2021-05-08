package crawler.component;

import org.jsoup.Jsoup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

import static java.lang.System.Logger.Level.INFO;

public class WebCrawler extends JFrame implements ActionListener {
    private static final System.Logger LOGGER = System.getLogger("");

    private final Toolbar toolbar = new Toolbar(this);
    private final ExportFile exportFile = new ExportFile();
    private final TablePanel tablePanel = new TablePanel();

    {
        setTitle("Web Crawler");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setVisible(true);
        add(toolbar, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(exportFile, BorderLayout.SOUTH);
    }

    public WebCrawler() {
        LOGGER.log(INFO, "Web Crawler started!");
    }

    private static String[] apply(String link) {
        try {
            final var doc = Jsoup.connect(link).get();
            LOGGER.log(INFO, "Add: {0}, DocumentType: {1}", link, doc.documentType());
            if (doc.documentType() == null) {
                return null;
            }
            return new String[]{link, Jsoup.connect(link).get().title()};
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LOGGER.log(INFO, "actionPerformed: {0}", e);
        final var url = toolbar.getURL();
        try {
            final var doc = Jsoup.connect(url).get();
            final var links = doc.select("a[href]");
            LOGGER.log(INFO, "Processed: {0}, Title: {1}, Links: {2}", url, doc.title(), links.size());
            toolbar.setTitle(doc.title());
            var s = links.eachAttr("abs:href").stream();

            final var data = s.map(WebCrawler::apply).filter(Objects::nonNull).toArray(String[][]::new);
            LOGGER.log(INFO, "Table set data rows: {0}", data.length);
            tablePanel.setData(data);
            tablePanel.refresh();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

}