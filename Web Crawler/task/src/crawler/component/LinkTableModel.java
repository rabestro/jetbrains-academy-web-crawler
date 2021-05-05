package crawler.component;

import javax.swing.table.AbstractTableModel;

public class LinkTableModel extends AbstractTableModel {
    private static final String[] columns = {"URL", "Title"};
    private String[][] data = {{"Bob" , "Programmer"} , {"Alice" , "Programmer"}};

    public void setData(String[][] data) {
        this.data = data;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

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
