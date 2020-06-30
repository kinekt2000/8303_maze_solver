import java.util.Random;

public enum TileType {
    Grass("grass", 1),
    Sand("sand", 2),
    Gravel("gravel", 3),
    Snow("snow", 4),
    Cobblestone("cobblestone", 5);

    final static private TileType [] VALUES = TileType.values();
    final static private Random randomizer =  new Random();

    private final String name;
    private int time;

    TileType(String name, int time) {
        this.name = name;
        this.time = time;
    }

    @Override
    public String toString() {
        return name;
    }

    int getTime() {
        return time;
    }

    void setTime(int time) {
        this.time = time;
    }

    static public TileType random() {
        return VALUES[randomizer.nextInt(VALUES.length)];
    }

    static public TileType ID(String name) {
        for(TileType type: VALUES) {
            if(type.name.equals(name)) {
                return type;
            }
        }
        return null;
    }

}
