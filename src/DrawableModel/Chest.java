package DrawableModel;

import logic.Cell;
import render.Drawable;
import resources.ResourceManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Chest extends Cell implements Drawable {

    BufferedImage texture;
    int tileSize;

    protected Chest(int x, int y, int tileSize) {
        super(x, y);
        this.tileSize = tileSize;
        texture = ResourceManager.getTexture("chest");
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(texture, tileSize * getX(), tileSize * getY(), tileSize, tileSize, null);
    }
}
