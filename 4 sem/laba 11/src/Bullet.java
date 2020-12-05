import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Bullet extends Entity {
    private static int speedX = 5;
    private static int speedY = 5;
    private static Image[] images;

    static {
        try {
            images = new Image[]{
                    ImageIO.read(new File("src\\images\\bulletL.png")),
                    ImageIO.read(new File("src\\images\\bulletU.png")),
                    ImageIO.read(new File("src\\images\\bulletR.png")),
                    ImageIO.read(new File("src\\images\\bulletD.png"))};
        } catch (IOException e) {
            System.err.println("Can't load bullet images");
        }
    }

    private Direction direction;
    private boolean player;

    public Bullet(int x, int y, boolean player, Direction direction, TankClient tankClient) {
        super(x, y, 10, 10, tankClient);
        this.direction = direction;
        this.player = player;
        this.tankClient = tankClient;
        if (player)
            tankClient.shot();
    }

    public void move() {
        switch (direction) {
            case L:
                x -= speedX;
                break;
            case U:
                y -= speedY;
                break;
            case R:
                x += speedX;
                break;
            case D:
                y += speedY;
                break;
            default:
                break;
        }
        if (x < 0 || y < 0 || x > tankClient.getWidth() || y > tankClient.getHeight()) {
            tankClient.removeBullet(this);
        }
    }

    @Override
    public void draw(Graphics g) {
        switch (direction) {
            case L:
                g.drawImage(images[0], x, y, null);
                break;
            case U:
                g.drawImage(images[1], x, y, null);
                break;
            case R:
                g.drawImage(images[2], x, y, null);
                break;
            case D:
                g.drawImage(images[3], x, y, null);
                break;
        }
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }

    public boolean hitTanks(List<Tank> tanks) {
        for (Tank tank : tanks) {
            if (hitTank(tank)) {
                return true;
            }
        }
        return false;
    }

    public boolean hitTank(Tank t) {
        if (t.isLive() && this.getRect().intersects(t.getRect()) && this.player != t.isPlayer()) {
            t.hit();
            tankClient.addExplosion(new Explosion(x - 25, y - 25, player, tankClient, 1));
            tankClient.removeBullet(this);
            return true;
        }
        return false;
    }

    public boolean hitBrickWall(List<BrickWall> list) {
        for (BrickWall w : list) {
            if (this.getRect().intersects(w.getRect())) {
                tankClient.removeBullet(this);
                tankClient.addExplosion(new Explosion(x - 25, y - 25, player, tankClient, 1));
                tankClient.removeBrick(w);
                return true;
            }
        }
        return false;
    }

    public boolean hitBullet(List<Bullet> list) {
        for (Bullet b : list) {
            if ((this.player != b.player) && this.getRect().intersects(b.getRect())) {
                tankClient.removeBullet(this);
                tankClient.removeBullet(b);
                return true;
            }
        }
        return false;
    }

    public boolean hitMetalWall(List<MetalWall> list) {
        for (MetalWall w : list) {
            if (this.getRect().intersects(w.getRect())) {
                tankClient.removeBullet(this);
                return true;
            }
        }
        return false;
    }
}
