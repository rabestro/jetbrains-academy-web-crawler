package crawler.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import static java.lang.System.Logger.Level.INFO;

public class TablePanel extends JPanel {
    private static final System.Logger LOGGER = System.getLogger("");
    private static final String[] COLUMNS = {"URL", "Title"};

    private final DefaultTableModel tableModel = new DefaultTableModel(COLUMNS, 0);

    public TablePanel() {
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        setLayout(new BorderLayout());

        final var table = new JTable(tableModel);
        table.setName("TitlesTable");
        table.setEnabled(false);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void setData(String[][] db) {
        LOGGER.log(INFO, "Table set rows: {0}", db.length);

        tableModel.setDataVector(db, COLUMNS);
        tableModel.fireTableDataChanged();
        refresh();
    }

    public void refresh() {
        tableModel.fireTableDataChanged();
    }
}
