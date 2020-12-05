import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ProgressLine extends JPanel {
    private static Image[] images;

    static {
        try {
            images = new Image[]{
                    ImageIO.read(new File("src\\images\\hp.png"))};
        } catch (IOException e) {
            System.err.println("Can't load progress images");
        }
    }

    private int colOfTanks;
    private boolean win;
    private boolean lose;
    private int hp;
    private String level;
    private Image screenImage;
    private int height;
    private Font font;

    public ProgressLine(int height, int colOfTanks, String level) {
        this.colOfTanks = colOfTanks;
        this.level = level;
        this.height = height;
        hp = 2;
        font = new Font("DS Dots", Font.BOLD, 36);
        setBackground(Color.GRAY);
        setSize(120, height);
        setPreferredSize(new Dimension(120, height));
    }

    @Override
    public void paintComponent(Graphics g) {
        screenImage = this.createImage(120, height);
        Graphics graphics = screenImage.getGraphics();
        graphics.setColor(Color.GRAY);
        graphics.fillRect(0, 0, 120, height);
        graphics.setColor(Color.BLACK);
        graphics.setFont(font);

        for (int i = 0; i < hp; i++) {
            graphics.drawImage(images[0], 37 + 26 * (i % 2), 300 + 20 * (i / 2), null);
        }
        graphics.drawString(level, 50, height - 40);
        if (win) {
            graphics.drawString("WIN!", 15, 270);
        }
        if (lose) {
            graphics.drawString("LOSE", 5, 270);
        }
        g.drawImage(screenImage, 0, 0, null);
    }

    public void kill() {
        colOfTanks--;
        repaint();
    }

    public void hit() {
        hp--;
        repaint();
    }

    public void setWin() {
        win = true;
        repaint();
    }

    public void setLose() {
        lose = true;
        repaint();
    }
}