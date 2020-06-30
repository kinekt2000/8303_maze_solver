package tiles;

public class Tile {

    private final int x;
    private final int y;
    protected TileType type;

    protected Tile(TileType type, int x, int y) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
