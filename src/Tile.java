import java.io.Serializable;

public class Tile implements Serializable {

    private final int x;
    private final int y;
    private  final TileType tileType;
    private int distanceFunction;
    private int distance;
    public boolean isVisited;

    public Tile(int x, int y, TileType tyleType){
        this.x = x;
        this.y = y;
        this.tileType = tyleType;
        this.isVisited = false;
    }


    public void setDistanceFunction(int distanceFunction) {
        this.distanceFunction = distanceFunction;
    }

    public int getDistanceFunction() {
        return distanceFunction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public TileType getTileType() {
        return tileType;
    }
}
