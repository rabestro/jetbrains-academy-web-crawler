package crawler.component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import static java.lang.System.Logger.Level.INFO;

public class TablePanel extends JPanel {
    private static final System.Logger LOGGER = System.getLogger("");
    private static final String[] COLUMNS = {"URL", "Title"};

    private JTable table;
    private final DefaultTableModel tableModel = new DefaultTableModel(COLUMNS, 0);

    public TablePanel() {
        table = new JTable(tableModel);
        table.setName("TitlesTable");
        table.setEnabled(false);
        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void setData(String[][] db) {
        LOGGER.log(INFO, "Table setDataVector, rows: {0}", db.length);
        tableModel.setDataVector(db, COLUMNS);
        tableModel.fireTableDataChanged();
    }

    public void refresh() {
        tableModel.fireTableDataChanged();
    }
}
