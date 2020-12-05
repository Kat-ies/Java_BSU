import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class Tank extends Entity {
    private static int speedX = 2;
    private static int speedY = 2;
    private static Random random = new Random();
    private static Image[] images;

    static {
        try {
            images = new Image[]{
                    ImageIO.read(new File("src\\images\\tankD.png")),
                    ImageIO.read(new File("src\\images\\tankU.png")),
                    ImageIO.read(new File("src\\images\\tankL.png")),
                    ImageIO.read(new File("src\\images\\tankR.png")),
                    ImageIO.read(new File("src\\images\\HtankD.png")),
                    ImageIO.read(new File("src\\images\\HtankU.png")),
                    ImageIO.read(new File("src\\images\\HtankL.png")),
                    ImageIO.read(new File("src\\images\\HtankR.png"))};
        } catch (IOException e) {
            System.err.println("Can't load tank images");
        }
    }

    private Direction direction;
    private Direction imDirection = Direction.U;
    private boolean player;
    private int oldX, oldY;
    private int life;
    private boolean live;
    private int rate;
    private int step;
    private boolean bL, bU, bR, bD;

    public Tank(int x, int y, boolean player, Direction direction, TankClient tankClient) {
        super(x, y, 30, 30, tankClient);
        this.oldX = x;
        this.oldY = y;
        this.player = player;
        this.direction = direction;
        life = 2;
        live = true;
        rate = 1;
        step = random.nextInt(10) + 5;
        bL = false;
        bU = false;
        bR = false;
        bD = false;
    }

    public void draw(Graphics g) {
        if (life <= 0)
            return;
        switch (imDirection) {
            case D:
                if (player) {
                    g.drawImage(images[4], x, y, null);
                } else {
                    g.drawImage(images[0], x, y, null);
                }
                break;
            case U:
                if (player) {
                    g.drawImage(images[5], x, y, null);
                } else {
                    g.drawImage(images[1], x, y, null);
                }
                break;
            case L:
                if (player) {
                    g.drawImage(images[6], x, y, null);
                } else {
                    g.drawImage(images[2], x, y, null);
                }
                break;
            case R:
                if (player) {
                    g.drawImage(images[7], x, y, null);
                } else {
                    g.drawImage(images[3], x, y, null);
                }
                break;
        }
    }

    public void move() {
        if (!live)
            return;
        oldX = x;
        oldY = y;
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
            case STOP:
                break;
        }
        if (this.direction != Direction.STOP) {
            this.imDirection = this.direction;
        }
        if (x < 0)
            x = 0;
        if (y < 0)
            y = 0;
        if (x + width > tankClient.getWidth())
            x = tankClient.getWidth() - width;
        if (y + height > tankClient.getHeight())
            y = tankClient.getHeight() - height;
        if (!player) {
            Direction[] directions = Direction.values();
            if (step == 0) {
                step = random.nextInt(12) + 3;
                int mod = random.nextInt(9);
                if (playerTankAround()) {
                    if (Math.abs(x - tankClient.userTank().getRect().x) < 15) {
                        if (y > tankClient.userTank().getRect().y) direction = directions[1];
                        else if (y < tankClient.userTank().getRect().y) direction = directions[3];
                    } else if (Math.abs(y - tankClient.userTank().getRect().y) < 15) {
                        if (x > tankClient.userTank().getRect().x) direction = directions[0];
                        else if (x < tankClient.userTank().getRect().x) direction = directions[2];
                    } else {
                        int rn = random.nextInt(directions.length);
                        direction = directions[rn];
                    }
                    rate = 2;
                } else if (mod == 1) {
                    rate = 1;
                } else if (1 < mod && mod <= 3) {
                    rate = 1;
                } else {
                    int rn = random.nextInt(directions.length);
                    direction = directions[rn];
                    rate = 1;
                }
            }
            step--;
            if (rate == 2) {
                if (random.nextInt(40) > 35)
                    this.fire();
            } else if (random.nextInt(40) > 38)
                this.fire();
        }
    }

    private boolean playerTankAround() {
        int rx = x - 25, ry = y - 25;
        if (rx < 0)
            rx = 0;
        if (ry < 0)
            ry = 0;
        Rectangle a = new Rectangle(rx, ry, 85, 85);
        return a.intersects(tankClient.userTank().getRect()) && tankClient.userTank().live;
    }


    private void changeToOldDir() {
        x = oldX;
        y = oldY;
    }

    public void keyPressed(int e) {
        if (!live || !tankClient.isRun()) {
            System.out.println("blocked");
            return;
        }
        switch (e) {
            case KeyEvent.VK_RIGHT:
                bR = true;
                break;
            case KeyEvent.VK_LEFT:
                bL = true;
                break;
            case KeyEvent.VK_UP:
                bU = true;
                break;
            case KeyEvent.VK_DOWN:
                bD = true;
                break;
        }
        decideDirection();
    }

    private void decideDirection() {
        if (!bL && !bU && bR && !bD)
            direction = Direction.R;
        else if (bL && !bU && !bR && !bD)
            direction = Direction.L;
        else if (!bL && bU && !bR && !bD)
            direction = Direction.U;
        else if (!bL && !bU && !bR && bD)
            direction = Direction.D;
        else
            direction = Direction.STOP;
        if (!live || !tankClient.isRun()) direction = Direction.STOP;
    }

    public void keyReleased(int e) {
        if (!live || !tankClient.isRun())
            return;
        switch (e) {
            case KeyEvent.VK_SPACE:
                fire();
                break;
            case KeyEvent.VK_RIGHT:
                bR = false;
                break;
            case KeyEvent.VK_LEFT:
                bL = false;
                break;
            case KeyEvent.VK_UP:
                bU = false;
                break;
            case KeyEvent.VK_DOWN:
                bD = false;
                break;
        }
        decideDirection();
    }

    private void fire() {
        int x = this.x + width / 2 - 5;
        int y = this.y + height / 2 - 5;
        tankClient.addBullet(new Bullet(x, y, player, imDirection, this.tankClient));
    }


    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isPlayer() {
        return player;
    }

    public boolean collideWithBrickWall(List<BrickWall> list) {
        for (BrickWall w : list) {
            if (this.getRect().intersects(w.getRect())) {
                this.changeToOldDir();
                return true;
            }
        }
        return false;
    }

    public boolean collideWithMetalWall(List<MetalWall> list) {
        for (MetalWall w : list) {
            if (this.getRect().intersects(w.getRect())) {
                this.changeToOldDir();
                return true;
            }
        }
        return false;
    }


    public boolean collideWithTanks(List<Tank> tanks) {
        for (Tank t : tanks) {
            if ((t.life > 0) && (this != t) && this.getRect().intersects(t.getRect())) {
                this.changeToOldDir();
                t.changeToOldDir();
                return true;
            }
        }
        return false;
    }

    public void hit() {
        if (player) {
            life--;
            tankClient.hit();
            if (life == 0) {
                tankClient.addExplosion(new Explosion(x - 15, y - 15, true, tankClient, 2));
                tankClient.lose();
            }
        } else {
            tankClient.removeTank(this);
            live = false;
            life = 0;
            tankClient.addExplosion(new Explosion(x - 15, y - 15, true, tankClient, 2));
            tankClient.win();
        }
    }

    public boolean isLive() {
        return live;
    }

    public void die() {
        live = false;
    }
}