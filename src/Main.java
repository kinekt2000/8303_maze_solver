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

        while(field.nextStepFindPath(new Cell(0,0), new Cell(4,4))){

        }

        ArrayList<Cell> path = field.getPath();
        for (Cell el: path)
            System.out.print(el.getX() + " " + el.getY() + " --> ");

        System.out.println();

        ArrayList<Cell> cells = new ArrayList<>();
        cells.add(new Cell(4, 0));
        cells.add(new Cell(0, 4));
        cells.add(new Cell(4, 4));

        while (field.nextStepFindPath(new Cell(0, 0), cells)){

        }

        path = field.getFullPath();
        for (Cell el: path)
            System.out.print(el.getX() + " " + el.getY() + " --> ");

        System.out.println();

        ArrayList<ArrayList<Cell>> allPaths = field.findAllPath(new Cell(0,0), new Cell(4, 4));

        for (ArrayList<Cell> mas: allPaths) {
            for (Cell el : mas) {
                System.out.print(el.getX() + " " + el.getY() + " --> ");
            }
            System.out.println("");
        }

        //field.save();
        //field = field.load();
        //field.print();


    }
}
