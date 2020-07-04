import render.Drawable;
import tiles.Tile;
import tiles.TileType;
import resources.ResourceManager;
import resources.TextureGettingException;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DrawableTile extends Tile implements Drawable {

    private BufferedImage texture;
    private int tileSize = 0;

    DrawableTile(TileType type, int x, int y, int tileSize) throws TextureGettingException {
        super(type, x, y);

        texture = ResourceManager.getTexture(type.toString());
        if(texture == null) throw new TextureGettingException("Error while creating DrawableTile: " + type.toString());

        this.tileSize = tileSize;
    }

    DrawableTile(TileType type, int x, int y) throws TextureGettingException {
        super(type, x, y);

        texture = ResourceManager.getTexture(type.toString());
        if(texture == null) throw new TextureGettingException("Error while creating DrawableTile: " + type.toString());

        this.tileSize = Math.max(texture.getHeight(), texture.getWidth());
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(texture, tileSize * getX(), tileSize * getY(), tileSize, tileSize, null);
    }
}
