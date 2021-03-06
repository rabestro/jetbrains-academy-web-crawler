package crawler;

import crawler.controller.Crawler;
import crawler.view.ExportFile;
import crawler.view.TablePanel;
import crawler.view.Toolbar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.System.Logger.Level.INFO;

public class WebCrawler extends JFrame implements ActionListener {
    private static final System.Logger LOGGER = System.getLogger("");

    private final Crawler crawler = new Crawler();
    private final Toolbar toolbar = new Toolbar(this);
    private final ExportFile exportFile = new ExportFile(this);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        LOGGER.log(INFO, "actionPerformed: {0}", e.getActionCommand());
        switch (e.getActionCommand()) {
            case "Parse":
                final var data = crawler.getPageContent(toolbar.getURL());
                toolbar.setTitle(data.getTitle());
                tablePanel.setData(data.getLinks());
                break;
            case "Save":
                crawler.save(exportFile.getFileName());
        }

    }

}