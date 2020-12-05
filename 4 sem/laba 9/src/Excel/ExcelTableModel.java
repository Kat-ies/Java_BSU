package Excel;

import javax.swing.table.AbstractTableModel;



public class ExcelTableModel extends AbstractTableModel {

    private Controller controller;

    private Cell[][] data;

    Cell[][] getData() {
        return data;
    }

    void setController(Controller controller) {
        this.controller = controller;
    }

    ExcelTableModel(int rowsNumber, int columnsNumber) {

        data = new Cell[rowsNumber][columnsNumber];

        for (int i = 0; i < rowsNumber; ++i){
            for (int j = 0; j < columnsNumber; ++j){
                data[i][j] = new Cell();
            }
        }
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return data[0].length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        if (columnIndex != 0) {
            return data[rowIndex][columnIndex];
        } else {
            return rowIndex + 1;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 0;
    }

    @Override
    public String getColumnName(int column) {

        if (column != 0) {
            return String.valueOf((char) ('A' + column - 1));
        } else {
            return "";
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        super.setValueAt(aValue, rowIndex, columnIndex);

        controller.setValue(this, (String) aValue, rowIndex, columnIndex);
    }
}
