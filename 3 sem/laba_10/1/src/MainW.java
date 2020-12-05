import javax.swing.*;
import java.awt.event.*;


public class MainW extends JDialog {
    private JPanel contentPane;
    private JButton button1;
    private JLabel label;
    private JPanel panel;


    public MainW() {

        setContentPane(contentPane);
        panel.setLayout(null);
        button1.setBounds(0, 0, 100, 30);
        setModal(true);
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                StringBuffer s = new StringBuffer();
                int x = e.getX();
                int y = e.getY();
                s.append("Координаты: X=" + x + " Y=" + y);
                label.setText(s.toString());
            }
        });


        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                button1.setLocation(panel.getMousePosition());
            }
        });
        button1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                    StringBuffer buf = new StringBuffer();
                    buf.append(button1.getText());
                    if (buf.length() != 0)
                        buf.deleteCharAt(buf.length() - 1);
                    button1.setText(buf.toString());
                } else {
                    StringBuffer buf = new StringBuffer();
                    buf.append(button1.getText());
                    buf.append(e.getKeyChar());
                    button1.setText(buf.toString());
                }
            }
        });
        button1.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (e.isControlDown()) {
                    button1.setLocation(e.getXOnScreen() - panel.getLocationOnScreen().x, e.getYOnScreen() - panel.getLocationOnScreen().y);

                }
                printKoor(e);
            }
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                printKoor(e);
            }
        });

        panel.addMouseListener(new MouseAdapter() {
        });
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseMoved(e);
                printKoor(e);
            }
        });
    }

    private void printKoor(MouseEvent e) {
        StringBuffer s = new StringBuffer();
        int x = e.getXOnScreen() - panel.getLocationOnScreen().x;
        int y = e.getYOnScreen() - panel.getLocationOnScreen().y;
        s.append("Координаты: X=" + x + " Y=" + y);
        label.setText(s.toString());
    }

    public static void main(String[] args) {
        MainW dialog = new MainW();
        dialog.pack();
        dialog.setSize(700, 500);
        dialog.setVisible(true);
        System.exit(0);
    }
}
