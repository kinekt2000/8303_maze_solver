import render.Drawable;

import java.awt.*;

public class TileHighlighter implements Drawable {
    private int x;
    private int y;

    final private int tileSize;
    final private Color color;

    public TileHighlighter(int x, int y, int tileSize, Color color) {
        this.x = x;
        this.y = y;
        this.tileSize = tileSize;
        this.color = color;
    }

    public TileHighlighter(int x, int y, int tileSize) {
        this(x, y, tileSize, Color.BLACK);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Color oldColor = g2d.getColor();
        Stroke oldStroke = g2d.getStroke();

        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(1));

        g2d.drawRect(x * tileSize, y * tileSize, tileSize, tileSize);

        g2d.setStroke(oldStroke);
        g2d.setColor(oldColor);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
