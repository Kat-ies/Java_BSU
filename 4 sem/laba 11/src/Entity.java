import java.awt.*;

public abstract class Entity {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected TankClient tankClient;

    public Entity(int x, int y, int width, int height, TankClient tankClient) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.tankClient = tankClient;
    }

    public Entity(int x, int y, TankClient tankClient) {
        this(x, y, 35, 35, tankClient);
    }

    public abstract void draw(Graphics g);
}
