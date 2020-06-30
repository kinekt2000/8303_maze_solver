import resources.TextureGettingException;
import tiles.TileType;

import java.util.Arrays;
import java.util.List;

public class TileMap {
    private DrawableTile[] tiles;
    
    private int width;
    private int height;
    
    public TileMap(int width, int height, TileType initialType) {
        this.width = width;
        this.height = height;

        tiles = new DrawableTile[width * height];
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                try {
                    tiles[x + width * y] = new DrawableTile(initialType, x, y);
                } catch (TextureGettingException e) {
                    System.out.println(e.getMessage());
                    tiles[x + width * y] = null;
                }
            }
        }
    }

    public TileMap(int width, int height) {
        this.width = width;
        this.height = height;

        tiles = new DrawableTile[width * height];
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                try {
                    tiles[x + width * y] = new DrawableTile(TileType.random(), x, y);
                } catch (TextureGettingException e) {
                    System.out.println(e.getMessage());
                    tiles[x + width * y] = null;
                }
            }
        }
    }

    public List<Drawable> getRenderObjects() {
        return Arrays.asList(tiles);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
