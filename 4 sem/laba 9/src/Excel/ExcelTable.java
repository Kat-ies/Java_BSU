package Excel;

import javax.swing.*;



class ExcelTable extends JTable {

    private static final int INDEX_CELL_WIDTH = 30;
    private static final int CELL_WIDTH = 150;
    private static final int CELL_HEIGHT = 30;

    ExcelTable(int rowsNumber, int columnsNumber) {

        var model = new ExcelTableModel(rowsNumber, columnsNumber);
        var controller = new Controller();
        model.setController(controller);
        setModel(model);

        setDefaultRenderer(Object.class, new CellRenderer());
        setDefaultEditor(Object.class, new CellEditor());

        setRowSelectionAllowed(false);

        setCellSizes();
    }

    private void setCellSizes() {

        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        setRowHeight(CELL_HEIGHT);

        getColumnModel().getColumn(0).setMaxWidth(INDEX_CELL_WIDTH);

        for (int i = 1; i < getColumnCount(); ++i) {
            getColumnModel().getColumn(i).setMinWidth(CELL_WIDTH);
        }
    }
}
