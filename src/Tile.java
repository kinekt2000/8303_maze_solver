import java.io.Serializable;

public class Tile implements Serializable {

    private final int x;
    private final int y;
    private  final TileType tileType;
    public boolean isVisited;
    public boolean isOpen;

    public Tile(int x, int y, TileType tyleType){
        this.x = x;
        this.y = y;
        this.tileType = tyleType;
        this.isVisited = false;
        this.isOpen = false;
    }

    public int getX() {

        return x;
    }

    public int getY() {

        return y;
    }

    public TileType getTileType() {
        return tileType;
    }
}
