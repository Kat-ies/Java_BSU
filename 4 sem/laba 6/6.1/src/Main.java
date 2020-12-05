import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main extends JFrame {
    int numberOfTiles = 4;
    private File file;
    String imagePath = "example.png";


    private JPanel tilePanel;
    private JPanel controlPanel;

    private JButton[][] tileMatrix;

    private int swapTileX = -1;
    private int swapTileY = -1;

    private JFileChooser fileChooser;

    public Main() {
        super("Задание 6.1");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setCurrentDirectory(new File ("    D:\\2 курс\\4 семестр\\УП\\laba 6\\6.1\\"));




        /*__________menu________*/
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        final JMenu[] menu = {new JMenu("Файл")};
        JMenu game = new JMenu("Игра");
        menuBar.add(menu[0]);
        menuBar.add(game);

        JMenuItem open = new JMenuItem("Открыть..");
        JMenuItem newGame = new JMenuItem("Новая игра");
        menu[0].add(open);
        game.add(newGame);

        /*_______panel_________*/
        controlPanel = new JPanel();
        JLabel exampleImage = new JLabel();
        exampleImage.setIcon(new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT)));
        controlPanel.add(exampleImage);

        GridLayout gridLayout = new GridLayout(numberOfTiles, numberOfTiles);
        gridLayout.setHgap(5);
        gridLayout.setVgap(5);
        tilePanel = new JPanel(gridLayout);
        makeTiles();

        add(controlPanel, BorderLayout.WEST);
        add(tilePanel, BorderLayout.CENTER);

        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.setDialogTitle("Choose image: ");
                int returnValue = fileChooser.showOpenDialog(Main.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    file = fileChooser.getSelectedFile();
                    imagePath=file.getPath();
                    exampleImage.setIcon(new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT)));
                    makeTiles();
                    updateTiles();
                }
            }
        });
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                    String input = JOptionPane.showInputDialog("Введите размер поля: ");
                    if (input == null) return;
                    else {
                        if (input.equals("")) {
                            JOptionPane.showMessageDialog(null, "Введите  данные!");
                        } else
                        if (input.equals("1")) {
                            JOptionPane.showMessageDialog(null, "Размер поля должен быть >1!");
                        } else{
                            numberOfTiles= Integer.parseInt(input);

                            gridLayout.setColumns(numberOfTiles);
                            gridLayout.setRows(numberOfTiles);
                            tilePanel.setLayout(gridLayout);
                            makeTiles();
                            updateTiles();
                        }

                }
            }
        });

    }

    private void updateTiles() {
        tilePanel.removeAll();
        for (int i = 0; i < numberOfTiles; i++)
            for (int j = 0; j < numberOfTiles; j++)
                tilePanel.add(tileMatrix[i][j]);
        tilePanel.revalidate();
        tilePanel.repaint();
    }

    private void makeTiles() {
        tileMatrix = generateTiles(imagePath);

        for (int i = 0; i < numberOfTiles*numberOfTiles; i++)
            swap((int)(Math.random() * numberOfTiles),
                    (int)(Math.random() * numberOfTiles),
                    (int)(Math.random() * numberOfTiles),
                    (int)(Math.random() * numberOfTiles),
                    false);

        for (int i = 0; i < numberOfTiles; i++)
            for (int j = 0; j < numberOfTiles; j++)
                tilePanel.add(tileMatrix[i][j]);

    }

    private  JButton[][] generateTiles(String imagePath) {
        try {
            BufferedImage wholeImage = ImageIO.read(new File(imagePath));
            int tileWidth = wholeImage.getWidth()/numberOfTiles, tileHeight = wholeImage.getHeight()/numberOfTiles;
            JButton[][] tiles = new JButton[numberOfTiles][numberOfTiles];
            for (int i = 0; i < numberOfTiles; i++)
                for (int j = 0; j < numberOfTiles; j++) {
                    tiles[i][j] = new JButton();
                    tiles[i][j].setIcon(new ImageIcon(wholeImage.getSubimage(j*tileWidth, i*tileHeight, tileWidth, tileHeight)));
                    tiles[i][j].setName(""+(i*numberOfTiles+j));
                    tiles[i][j].putClientProperty(0, i);
                    tiles[i][j].putClientProperty(1, j);
                    tiles[i][j].addActionListener(e -> {
                        JButton b = ((JButton) e.getSource());
                        if (swapTileX == -1) {
                            b.setEnabled(false);
                            swapTileX = (int)b.getClientProperty(0);
                            swapTileY = (int)b.getClientProperty(1);
                        }
                        else {
                            swap((int)b.getClientProperty(0), (int)b.getClientProperty(1), swapTileX, swapTileY, true);
                            checker();
                            swapTileX = -1;
                        }
                    });
                }
            return tiles;

        } catch (IOException e) {
            return null;
        }
    }

    private void swap(int tile1X, int tile1Y, int tile2X, int tile2Y, boolean repaint) {

        JButton tile1 =  tileMatrix[tile1X][tile1Y];
        JButton tile2 =  tileMatrix[tile2X][tile2Y];
        tile1.setEnabled(true);
        tile1.putClientProperty(0, tile2X);
        tile1.putClientProperty(1, tile2Y);
        tile2.setEnabled(true);
        tile2.putClientProperty(0, tile1X);
        tile2.putClientProperty(1, tile1Y);

        JButton temp = tileMatrix[tile1X][tile1Y];
        tileMatrix[tile1X][tile1Y] = tileMatrix[tile2X][tile2Y];
        tileMatrix[tile2X][tile2Y] = temp;

        if (repaint) {
            updateTiles();
        }
    }

    void checker() {
        for (int i = 0; i < numberOfTiles; i++)
            for (int j = 0; j < numberOfTiles; j++)
                if(Integer.parseInt(tileMatrix[i][j].getName()) != i*numberOfTiles+j)
                    return;
        JOptionPane.showMessageDialog(null, "Вы собрали пазл", "УРА!", JOptionPane.INFORMATION_MESSAGE );
    }

    public static void main(String[] args) {
        Main app = new Main();
        app.setVisible(true);
    }
}