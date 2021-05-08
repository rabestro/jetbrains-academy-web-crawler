package crawler.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ExportFile extends JPanel {
    private final JTextField fileName = new JTextField();
    private final JButton saveButton = new JButton("Save");

    {
        saveButton.setName("ExportButton");
        fileName.setName("ExportUrlTextField");

        add(new JLabel("Export:"), BorderLayout.WEST);
        add(fileName, BorderLayout.CENTER);
        add(saveButton, BorderLayout.EAST);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public ExportFile(ActionListener listener) {
        super(new BorderLayout());
        saveButton.addActionListener(listener);
    }

    public String getFileName() {
        return fileName.getText();
    }
}
