import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import javax.swing.border.Border;

public class CountryJLabel extends JLabel implements ListCellRenderer<Country> {
    private Border border;

    CountryJLabel() {
        setIconTextGap(10);
        border = BorderFactory.createLineBorder(Color.DARK_GRAY, 1);
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Country> list, Country value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        setIcon(value.getFlag());
        setText(value.getName());

        if (isSelected) {
            setBackground(Color.LIGHT_GRAY);
            setForeground(Color.BLACK);
        } else {
            setBackground(Color.WHITE);
            setForeground(Color.BLUE);
        }

        setFont(list.getFont());
        setEnabled(list.isEnabled());

        if (isSelected && cellHasFocus) {
            setBorder(border);
        } else {
            setBorder(null);
        }

        return this;
    }
}