package crawler.view;

import javax.swing.*;
import java.awt.*;

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

    ExportFile() {
        super(new BorderLayout());
    }
}
