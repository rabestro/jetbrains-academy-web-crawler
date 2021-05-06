package crawler.component;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import static java.lang.System.Logger.Level.INFO;

public class TablePanel extends JPanel {
    private static final System.Logger LOGGER = System.getLogger("");

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
        LOGGER.log(INFO, "Table set to: {0}", Objects.isNull(db));
        tableModel.setData(db);
    }

    public void refresh() {
        tableModel.fireTableDataChanged();
    }
}
