package Excel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import java.awt.*;



public class CellRenderer extends JTextField implements TableCellRenderer {

    private static final Color SELECTION_FOREGROUND = UIManager.getColor("Table.selectionForeground");
    private static final Color FOREGROUND = UIManager.getColor("Table.foreground");
    private static final Color SELECTION_BACKGROUND = UIManager.getColor("Table.selectionBackground");
    private static final Color BACKGROUND = UIManager.getColor("Table.background");

    private static final Color INDEX_BACKGROUND = new Color(200,200,200);

    private static final Border LINE_BORDER_SELECTION_FOREGROUND = BorderFactory.createLineBorder(UIManager.getColor("Table.selectionForeground"), 1);
    private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(2,2,2,2);

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {

        if (column == 0){
            setText(value.toString());

        } else {

            if (value instanceof Cell) {
                var cell = (Cell) value;

                if (cell.getFormula() == null) {
                    setText(null);

                } else {
                    if (hasFocus) {
                        setText(cell.getFormula());

                    } else {
                        if (cell.isWrongFormula()) {
                            setText(cell.getError());

                        } else {
                            setText(cell.toString());
                        }
                    }
                }
            }
        }

        setForeground(isSelected ? SELECTION_FOREGROUND : FOREGROUND);
        setBackground(isSelected ? SELECTION_BACKGROUND : BACKGROUND);

        setBorder(hasFocus ? LINE_BORDER_SELECTION_FOREGROUND : EMPTY_BORDER);

        if (column == 0) {
            setBackground(INDEX_BACKGROUND);
        }

        return this;
    }
}
