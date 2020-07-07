package DrawableModel;

import logic.Cell;
import render.Drawable;
import resources.ResourceManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Scout extends Cell implements Drawable{

    BufferedImage texture;
    int tileSize;

    protected Scout(int x, int y, int tileSize) {
        super(x, y);
        this.tileSize = tileSize;
        texture = ResourceManager.getTexture("scout");
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(texture, tileSize * getX(), tileSize * getY(), tileSize, tileSize, null);
    }
}
