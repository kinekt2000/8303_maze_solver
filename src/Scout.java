import java.awt.*;
import java.awt.image.BufferedImage;

public class Scout implements Drawable{

    private int x;
    private int y;

    BufferedImage texture;
    private int size;

    public Scout(BufferedImage texture, int x, int y, int size) {
        this.texture = texture;
        this.size = size;
        this.x = x;
        this.y = y;
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
