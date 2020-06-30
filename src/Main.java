import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.BufferPoolMXBean;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        int x, y;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        x = Integer.parseInt(reader.readLine());
        y = Integer.parseInt(reader.readLine());

        Field field = new Field(x, y);
        field.setRandomLandscape();
        field.print();

        ArrayList<Cell> path = field.findPath(new Cell(0,0), new Cell(1,1));
        for (Cell el: path)
            System.out.print(el.getX() + " " + el.getY() + " --> ");
        System.out.println("\n------------------------------------------");

        ArrayList<ArrayList<Cell>> allPaths = field.findAllPath(new Cell(0,0), new Cell(1, 1));

        for (ArrayList<Cell> mas: allPaths) {
            for (Cell el : mas) {
                System.out.print(el.getX() + " " + el.getY() + " --> ");
            }
            System.out.println("");
        }

        //field.save();
        field = field.load();
        field.print();


    }
}
