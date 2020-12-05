import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.filechooser.FileSystemView;

class MainWindow extends JFrame {
    private JButton colour;
    private JButton open;
    private JButton save;
    public JScrollPane scrollPane;
    private MyPanel panel;
    private JPanel buttonPanel;
    private Color currentColor;
    private JFileChooser fileChooser;
    private File file;

    private int xBegin;
    private int yBegin;
    private int xEnd;
    private int yEnd;

    MainWindow() {
        super("Paint");
        colour = new JButton("Color");
        open = new JButton("Open...");
        save = new JButton("Save");

        panel = new MyPanel();
        scrollPane = new JScrollPane(panel);
        int width = 700;
        int height = 500;
        setPreferredSize(new Dimension(width, height));


        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3));
        buttonPanel.add(colour);
        buttonPanel.add(save);
        buttonPanel.add(open);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        currentColor = Color.RED;

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                xBegin = e.getX();
                yBegin = e.getY();
            }
        });
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                xEnd = e.getX();
                yEnd = e.getY();

                Graphics g = panel.getGraphics();
                Graphics buf = panel.getBuffer().getGraphics();
                ((Graphics2D) g).setStroke(new BasicStroke(3));
                ((Graphics2D) buf).setStroke(new BasicStroke(3));
                buf.setColor(currentColor);
                g.setColor(currentColor);

                buf.drawLine(xBegin, yBegin, xEnd, yEnd);
                g.drawLine(xBegin, yBegin, xEnd, yEnd);

                xBegin = xEnd;
                yBegin = yEnd;
            }
        });
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Graphics g = panel.getGraphics();
                Graphics buf = panel.getBuffer().getGraphics();
                buf.setColor(currentColor);
                g.setColor(currentColor);

                g.fillRect(e.getX(), e.getY(), 3, 3);
                buf.fillRect(e.getX(), e.getY(), 3, 3);
            }
        });

        colour.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Color chosenColor = JColorChooser.showDialog(panel, "Choose color", currentColor);
                if (chosenColor != null)
                    currentColor = chosenColor;
            }
        });
        open.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fileChooser.setDialogTitle("Choose image: ");
                int returnValue = fileChooser.showOpenDialog(MainWindow.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    file = fileChooser.getSelectedFile();
                    try {
                        BufferedImage buf = ImageIO.read(file);
                        panel.loadImage(buf);
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        });

        save.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fileChooser.setDialogTitle("Save image: ");
                fileChooser.setSelectedFile(new File("image.png"));
                int ret = fileChooser.showSaveDialog(null);
                file = fileChooser.getSelectedFile();
                if (ret == fileChooser.APPROVE_OPTION) {
                    try {
                        ImageIO.write(panel.getBuffer(), "png", file);

                    } catch (IOException ex) {
                        ex.getMessage();
                    }
                }
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onClose();
            }
        });

    }

    private void onClose() {
        dispose();
    }
}


public class Main {

    public static void main(String[] args) {
        MainWindow paint = new MainWindow();
        paint.setVisible(true);
        paint.pack();
    }
}
