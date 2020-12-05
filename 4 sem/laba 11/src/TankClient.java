import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TankClient extends Panel implements ActionListener {
    static String[] sounds;

    static {
        sounds = new String[5];
        for (int i = 0; i < 5; i++) {
            sounds[i] = "src\\sounds\\" + i + ".wav";
        }
    }

    private int height;
    private int width;
    private int colOfTanks;
    private boolean run;

    private Image screenImage;
    private Tank userTank;
    private List<Tank> enemy;
    private List<Explosion> explosions;
    private List<Bullet> bullets;
    private List<BrickWall> homeWall;
    private List<BrickWall> otherWall;
    private List<MetalWall> metalWall;
    private List<Tank> tanksRemove;
    private List<Explosion> explosionsRemove;
    private List<Bullet> bulletsRemove;
    private List<BrickWall> brickWallRemove;
    private Timer fps;
    private ProgressLine observer;

    public TankClient(File file) throws IOException {
        try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
            width = scanner.nextInt();
            height = scanner.nextInt();
            colOfTanks = scanner.nextInt();
            run = true;
            tanksRemove = new ArrayList<>();
            explosionsRemove = new ArrayList<>();
            bulletsRemove = new ArrayList<>();
            brickWallRemove = new ArrayList<>();
            userTank = new Tank(35 * scanner.nextInt() + 2, 35 * scanner.nextInt() + 2, true, Direction.STOP, this);
            enemy = new ArrayList<>();
            enemy.add(new Tank(35 * scanner.nextInt() + 2, 35 * scanner.nextInt() + 2, false, Direction.valueOf(scanner.next()), this));

            String line;
            homeWall = new ArrayList<>();
            otherWall = new ArrayList<>();
            metalWall = new ArrayList<>();
            bullets = new ArrayList<>();
            explosions = new ArrayList<>();
            for (int i = 0; i < height; i++) {
                line = scanner.next();
                for (int j = 0; j < width; j++) {
                    switch (line.charAt(j)) {
                        case 'H': {
                            homeWall.add(new BrickWall(35 * j, 35 * i, this));
                            break;
                        }
                        case 'B': {
                            otherWall.add(new BrickWall(35 * j, 35 * i, this));
                            break;
                        }
                        case 'M': {
                            metalWall.add(new MetalWall(35 * j, 35 * i, this));
                            break;
                        }
                    }
                }
            }
        }
        setBackground(Color.BLACK);
        setSize(35 * width, 35 * height);
        setPreferredSize(new Dimension(35 * width, 35 * height));
        fps = new Timer(25, this);
        fps.start();
    }

    public void setObserver(ProgressLine observer) {
        this.observer = observer;
    }

    @Override
    public void update(Graphics g) {
        screenImage = this.createImage(35 * width, 35 * height);
        Graphics graphics = screenImage.getGraphics();
        Color c = graphics.getColor();
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, 35 * width, 35 * height);
        graphics.setColor(c);
        draw(graphics);
        g.drawImage(screenImage, 0, 0, null);
    }

    private void draw(Graphics g) {
        for (Bullet b : bullets) {
            b.draw(g);
        }
        for (Tank t : enemy) {
            t.draw(g);
        }
        userTank.draw(g);

        for (MetalWall w : metalWall) {
            w.draw(g);
        }
        for (BrickWall w : otherWall) {
            w.draw(g);
        }
        for (BrickWall w : homeWall) {
            w.draw(g);
        }
        for (Explosion e : explosions) {
            e.draw(g);
        }
    }

    private void move() {
        bullets.removeAll(bulletsRemove);
        bulletsRemove.clear();
        enemy.removeAll(tanksRemove);
        tanksRemove.clear();
        explosions.removeAll(explosionsRemove);
        explosionsRemove.clear();
        homeWall.removeAll(brickWallRemove);
        otherWall.removeAll(brickWallRemove);
        brickWallRemove.clear();
        for (Bullet b : bullets) {
            b.hitTanks(enemy);
            b.hitTank(userTank);
            b.hitBullet(bullets);
            b.hitMetalWall(metalWall);
            b.hitBrickWall(homeWall);
            b.hitBrickWall(otherWall);
            b.move();
        }
        for (Tank t : enemy) {
            moveTank(t);
        }
        moveTank(userTank);
        for (Explosion e : explosions) {
            e.move();
        }
    }

    private void moveTank(Tank t) {
        t.collideWithBrickWall(homeWall);
        t.collideWithBrickWall(otherWall);
        t.collideWithMetalWall(metalWall);
        t.collideWithTanks(enemy);
        t.move();
    }

    public void addExplosion(Explosion e) {
        explosions.add(e);
    }

    public void addBullet(Bullet e) {
        bullets.add(e);
    }

    public void removeExplosion(Explosion e) {
        explosionsRemove.add(e);
    }

    public void removeBrick(BrickWall w) {
        brickWallRemove.add(w);
    }

    public void removeBullet(Bullet b) {
        bulletsRemove.add(b);
    }

    public void removeTank(Tank t) {
        tanksRemove.add(t);
    }

    public void keyPressed(KeyEvent e) {
        userTank.keyPressed(e.getKeyCode());
    }

    public void keyReleased(KeyEvent e) {
        userTank.keyReleased(e.getKeyCode());
    }

    public Tank userTank() {
        return userTank;
    }
    public int getColOfTanks() {
        return colOfTanks;
    }
    public void hit() {
        observer.hit();
    }
    public void lose() {
        userTank.die();
        play(0);
        observer.setLose();
    }

    public void win() {
        colOfTanks--;
        observer.kill();
        if (colOfTanks == 0) {
            userTank.die();
            play(1);
            observer.setWin();
        }
    }

    public void shot() {
        play(4);
    }

    public void explose(int type) {
        if (type == 1) {
            play(3);
        } else {
            play(2);
        }
    }

    public void pause() {
        run = !run;
        if (run)
            fps.start();
        else fps.stop();
    }

    public void stop() {
        run = false;
        fps.stop();
    }

    private void play(int num) {
        SwingUtilities.invokeLater(() -> {
            try {
                AudioInputStream stream = AudioSystem.getAudioInputStream(new File(sounds[num]));
                Clip clip = AudioSystem.getClip();
                clip.open(stream);
                clip.start();
            } catch (LineUnavailableException | IOException | UnsupportedAudioFileException ignored) {
            }
        });
    }

    public boolean isRun() {
        return run;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }
}
