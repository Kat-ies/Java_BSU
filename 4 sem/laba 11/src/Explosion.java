import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Explosion extends Entity {
    private static Image[] images;

    static {
        try {
            images = new Image[]{
                    ImageIO.read(new File("src\\images\\1.png")),
                    ImageIO.read(new File("src\\images\\2.png")),
                    ImageIO.read(new File("src\\images\\3.png")),
                    ImageIO.read(new File("src\\images\\4.png")),
                    ImageIO.read(new File("src\\images\\5.png")),
                    ImageIO.read(new File("src\\images\\6.png")),
                    ImageIO.read(new File("src\\images\\7.png")),
                    ImageIO.read(new File("src\\images\\8.png")),
                    ImageIO.read(new File("src\\images\\9.png")),
                    ImageIO.read(new File("src\\images\\10.png"))};
        } catch (IOException e) {
            System.err.println("Can't load explosion images");
        }
    }

    private int step;
    private boolean live;
    private int type;
    boolean player;

    public Explosion(int x, int y, boolean player, TankClient tankClient, int type) {
        super(x, y, 60, 60, tankClient);
        this.player = player;
        step = 0;
        live = true;
        this.type = type;
        if (player)
            tankClient.explose(type);
    }

    @Override
    public void draw(Graphics g) {
        if (!live || step == 5)
            return;
        g.drawImage(images[step * type], x, y, null);
    }

    public void move() {
        if (!live) {
            tankClient.removeExplosion(this);
            return;
        }
        if (step == 5) {
            live = false;
            return;
        }
        step++;
    }
}
