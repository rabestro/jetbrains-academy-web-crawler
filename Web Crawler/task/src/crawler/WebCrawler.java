package crawler;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static java.lang.System.Logger.Level.INFO;

public class WebCrawler extends JFrame implements ActionListener {
    private static final System.Logger LOGGER = System.getLogger("");

    private final JButton runButton = new JButton("Get text!");
    private final JTextField urlField = new JTextField();
    private final JTextArea textArea = new JTextArea("HTML code?");
    {
        setTitle("Web Crawler");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setVisible(true);

        urlField.setName("UrlTextField");
        add(urlField, BorderLayout.NORTH);
        runButton.setName("RunButton");
        runButton.addActionListener(this);
        add(runButton, BorderLayout.SOUTH);
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
        try (InputStream inputStream = new BufferedInputStream(new URL(urlField.getText()).openStream())) {
            String siteText = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            textArea.setText(siteText);
        } catch (MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}