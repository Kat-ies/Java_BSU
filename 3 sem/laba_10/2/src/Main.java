import javax.swing.*;
import java.awt.event.*;

public class Main extends JDialog {
    private JPanel contentPane;
    private JPanel panel;
    private JButton bsuirButton;
    private JButton famcsButton;

    public Main() {
        setContentPane(contentPane);
        setModal(true);
        panel.setLayout(null);
        famcsButton.setBounds(100, 50, 100, 30);
        bsuirButton.setBounds(450, 50, 100, 30);

        famcsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                infoBox("Даааа!", "Info");
            }
        });

     /*   panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                radix(e.getX(), e.getY());
            }
        });
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                radix(e.getX(), e.getY());
            }
        });*/

        bsuirButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                radix(e.getX()+bsuirButton.getX(), e.getY()+bsuirButton.getY());
            }
        });
    }

    int r = 5;

    private void radix(int x, int y)//функция анализирует близость мышки к нопке
    {
        int x1 = bsuirButton.getX();
        int x2 = x1 + bsuirButton.getWidth();
        int y1 = bsuirButton.getY();
        int y2 = bsuirButton.getHeight() + y1;
        if (x >= x1 - r && x <= x2 + r && y >= y1 - r && y <= y2 + r)
            changePosition(x1, x2, y1, y2, x, y);

    }

    private void changePosition(int x1, int x2, int y1, int y2, int x, int y) {
        if (x <= x1 && y <= y1)
            bsuirButton.setLocation(bsuirButton.getX() + r, bsuirButton.getY() + r);
        if (x > x1 && x < x2 && y <= y1)
            bsuirButton.setLocation(bsuirButton.getX(), bsuirButton.getY() + r);
        if (x >= x2 && y <= y1)
            bsuirButton.setLocation(bsuirButton.getX() - r, bsuirButton.getY() + r);
        if (x <= x1 && y > y1 && y <= y2)
            bsuirButton.setLocation(bsuirButton.getX() + r, bsuirButton.getY());
        if (x >= x2 && y > y1 && y <= y2)
            bsuirButton.setLocation(bsuirButton.getX() - r, bsuirButton.getY());
        if (x <= x1 && y >= y2)
            bsuirButton.setLocation(bsuirButton.getX() + r, bsuirButton.getY() - r);
        if (x > x1 && x <= x2 && y >= y2)
            bsuirButton.setLocation(bsuirButton.getX(), bsuirButton.getY() - r);
        if (x > x2 && y >= y2)
            bsuirButton.setLocation(bsuirButton.getX() - r, bsuirButton.getY() - r);
        if (bsuirButton.getX() < panel.getX() || bsuirButton.getX() + bsuirButton.getWidth() > panel.getX() + panel.getWidth()
                || bsuirButton.getY() < panel.getY() || bsuirButton.getY() + bsuirButton.getHeight() > panel.getY() + panel.getHeight())
            infoBox("Ваша кнопка убежала!", "Info");
    }


    public void infoBox(String infoMessage, String titleBar) {
        JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
    }


    public static void main(String[] args) {
        Main dialog = new Main();
        dialog.pack();
        dialog.setSize(700, 500);
        dialog.setVisible(true);
        System.exit(0);
    }
}
