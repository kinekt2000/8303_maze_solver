import java.io.Serializable;

public class Tile implements Serializable, Cloneable {    //Класс плитки содержащей ландшафт

    private final int x;
    private final int y;
    private  final TileType tileType;  //Тип ланшафта клетки
    public boolean isVisited;        //Посещена ли клетка
    public boolean isOpen;           //Открыта ли клетка

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

    @Override
    public Tile clone() throws CloneNotSupportedException {
        return (Tile) super.clone();
    }
}
