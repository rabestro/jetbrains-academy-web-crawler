package crawler;

import javax.swing.table.AbstractTableModel;

public class LinkTableModel extends AbstractTableModel {
    final String[] columns = {"URL", "Title"};

    final Object[][] data = {{"Bob" , "Programmer"} , {"Alice" , "Programmer"}};

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }
}
