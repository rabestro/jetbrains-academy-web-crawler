package crawler;

import javax.swing.*;

import java.awt.*;

import static java.lang.System.Logger.Level.INFO;

public class WebCrawler extends JFrame {
    private static final System.Logger LOGGER = System.getLogger("");

    private final JButton runButton = new JButton("Get text!");
    private final JTextField url = new JTextField();
    private final JTextArea textArea = new JTextArea("HTML code?");
    {
        setTitle("Web Crawler");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setVisible(true);

        url.setName("UrlTextField");
        add(url, BorderLayout.NORTH);
        runButton.setName("RunButton");
        add(runButton, BorderLayout.SOUTH);
        textArea.setName("HtmlTextArea");
        textArea.setEnabled(false);
        add(textArea, BorderLayout.CENTER);
    }

    public WebCrawler() {
        LOGGER.log(INFO, "Web Crawler started!");
    }
}