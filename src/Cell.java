public class Cell {     //Класс клетки, необходимой для работы алгоритма
    private final int x;
    private final int y;
    private int distanceFunction;   //Значение эвристической функции для клетки
    private int distance;           //Расстояние до текущей клетки

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setDistanceFunction(int distanceFunction) {

        this.distanceFunction = distanceFunction;
    }

    public int getDistanceFunction() {

        return distanceFunction;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
