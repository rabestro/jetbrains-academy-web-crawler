package crawler.component;

import javax.swing.*;
import java.awt.*;

public class TablePanel extends JPanel {
    private JTable table;
    private LinkTableModel tableModel;

    public TablePanel() {
        tableModel = new LinkTableModel();
        table = new JTable(tableModel);
        table.setName("TitlesTable");
        table.setEnabled(false);
        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void setData(String[][] db) {
        tableModel.setData(db);
    }

    public void refresh() {
        tableModel.fireTableDataChanged();
    }
}
