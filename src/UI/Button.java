package UI;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Button {
    final String id;

    private int x;
    private int y;

    final private double r;
    final private BufferedImage texture;

    private int timer = 0;
    private boolean autoRelease = false;

    boolean pressed;

    public Button(String id, BufferedImage texture, int x, int y, double r) {
        if(id == null) {
            throw new NullPointerException("null pointer button id casted");
        }

        this.id = id;
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.r = r;
    }

    public Button(String id, BufferedImage texture, int x, int y) {
        if(texture == null){
            throw new NullPointerException("null pointer button texture casted");
        }

        if(id == null) {
            throw new NullPointerException("null pointer button id casted");
        }

        this.id = id;
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.r = Math.min(texture.getWidth(), texture.getHeight())/2.;
    }

    public Button(String id, int x, int y, double r) {
        if(id == null) {
            throw new NullPointerException("null pointer button id casted");
        }

        this.id = id;
        this.x = x;
        this.y = y;
        this.r = r;
        this.texture = null;
    }

    public void move(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public void update(int time) {
        timer -= time;
        if(timer < 0) {
            timer = 0;
        }

        if(autoRelease && timer == 0) {
            pressed = false;
        }
    }

    public void press() {
        pressed = true;
        autoRelease = false;
    }

    public void press(int time) {
        pressed = true;
        timer = time;
        autoRelease = true;
    }

    public void release() {
        if(!autoRelease) {
            pressed = false;
        }
    }

    public boolean isPointIn(int x, int y) {
        if((x - this.x)*(x - this.x) + (y - this.y)*(y - this.y) <= r*r) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isPressed() {
        return pressed;
    }

    public String getID() {
        return id;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // save context
        Stroke oldStroke = g2d.getStroke();
        Color oldColor = g2d.getColor();

        // draw texture
        if(texture != null )
            g2d.drawImage(texture, x - texture.getWidth()/2, y - texture.getHeight()/2, null);

        if(pressed) {
            g2d.setColor(new Color(0x3C, 0x3F, 0x41, 0xAF));
            g2d.fillOval(x - (int)r, y - (int)r, (int)r*2, (int)r*2);
        }

        // draw attributes
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.GRAY);
        g2d.drawOval(x - (int)r, y - (int)r, (int)r*2, (int)r*2);


        // restore context
        g2d.setColor(oldColor);
        g2d.setStroke(oldStroke);
    }
}
