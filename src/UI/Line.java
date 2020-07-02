package UI;

import java.awt.*;
import java.awt.font.FontRenderContext;

public class Line {
    private int x;
    private int y;
    private int width;
    private int height;

    private Color backColor;
    private Color frontColor;

    String line;

    Line(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        backColor = Color.WHITE;
        frontColor = Color.BLACK;
    }

    public void move(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public void setFrontColor(Color frontColor) {
        this.frontColor = frontColor;
    }

    public void setBackColor(Color backColor) {
        this.backColor = backColor;
    }

    public boolean isPointIn(int x, int y) {
        return (x >= this.x) && (x < this.x + width)
                && (y >= this.y) && (y < this.y + height);
    }

    public void setLine(String line) {
        this.line = line;
    }

    public void draw(Graphics g) {
        Color oldColor = g.getColor();
        Font oldFont = g.getFont();

        g.setColor(backColor);
        g.fillRect(x, y, width, height);

        g.setColor(frontColor);

        Font newFont = oldFont.deriveFont(height * 0.75f);
        g.setFont(newFont);

        int yOff = width/2 - newFont.getSize()/2;

        g.drawString(line, x+3, y + height - (int)(height * 0.125f));

        g.setColor(oldColor);
        g.setFont(oldFont);
    }

}
