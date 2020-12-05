package Excel;

import javax.swing.*;
import java.awt.*;

public class ExcelApp extends JFrame {

    private static final int ROWS_NUMBER = 30;
    private static final int COLUMNS_NUMBER = 27;

    private ExcelApp() {

        setFrameParams();

        var table = new ExcelTable(ROWS_NUMBER, COLUMNS_NUMBER);

        var scrollPane = new JScrollPane(
                table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        add(scrollPane, BorderLayout.CENTER);
    }

    private void setFrameParams() {
        setTitle("Excel");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(ExcelApp::new);
    }
}
