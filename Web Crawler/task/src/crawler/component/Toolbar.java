package crawler.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Toolbar extends JPanel {
    private final JTextField urlField = new JTextField();
    private final JButton runButton = new JButton("Parse");
    private final JLabel titleLabel = new JLabel("Example domain");

    {
        setLayout(new GridBagLayout());
        var c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        add(new JLabel("URL:"), c);
        c.gridy = 1;
        add(new JLabel("Title:"), c);

        titleLabel.setName("TitleLabel");
        c.gridx = 1;
        c.gridwidth = 2;
        add(titleLabel, c);

        urlField.setName("UrlTextField");
        c.gridwidth = 1;
        c.weightx = 0.8;
        c.gridx = 1;
        c.gridy = 0;
        add(urlField, c);

        runButton.setName("RunButton");
        c.gridx = 2;
        c.weightx = 0;
        add(runButton, c);
    }

    public Toolbar(ActionListener listener) {
        runButton.addActionListener(listener);
    }

    public String getURL() {
        return urlField.getText();
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }
}
