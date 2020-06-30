import java.awt.*;
import java.awt.image.BufferedImage;

public class Chest implements Drawable{

    private int x;
    private int y;

    BufferedImage texture;
    private int size;

    public Chest(BufferedImage texture, int x, int y, int size) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(texture, size * x, size * y, size, size, null);
    }
}
