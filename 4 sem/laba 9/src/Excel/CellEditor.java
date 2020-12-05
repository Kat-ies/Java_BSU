package Excel;

import javax.swing.*;
import java.awt.*;


public class CellEditor extends DefaultCellEditor {

    CellEditor() {
        super(new JTextField());
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                                                 int row, int column) {

        var textField = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);

        if (value != null) {
            textField.setText(((Cell) value).getFormula());
        }

        return textField;
    }
}
