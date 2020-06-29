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

        /*x = Integer.parseInt(reader.readLine());
        y = Integer.parseInt(reader.readLine());
        Cell startCell = new Cell(x, y);

        x = Integer.parseInt(reader.readLine());
        y = Integer.parseInt(reader.readLine());
        Cell finishCell = new Cell(x, y);*/

        /*Cell cell1 = new Cell(0, 4);
        Cell cell2 = new Cell(4, 0);
        Cell cell3 = new Cell(4, 4);
        ArrayList<Cell> cells = new ArrayList<>();
        cells.add(cell1);
        cells.add(cell2);
        cells.add(cell3);*/

        ArrayList<ArrayList<Cell>> allPaths = new ArrayList<>();
        ArrayList<Cell> currentPath = new ArrayList<>();
        currentPath.add(new Cell(0, 0));

        ArrayList<Cell> aStarPath = field.findPath(new Cell(0, 0), new Cell(3, 3));
        for (Cell el: aStarPath)
            System.out.print(el.getX() + " " + el.getY() + " --> ");
        System.out.println("");

        int minimalPath = 0;
        for (int i=0; i<aStarPath.size()-1; i++)
            minimalPath += field.fieldCell[aStarPath.get(i).getY()][aStarPath.get(i).getX()].time;
        field.findAllPath(new Cell(3, 3), currentPath, allPaths, minimalPath, 0);


        for (ArrayList<Cell> mas: allPaths) {
            for (Cell el : mas) {
                System.out.print(el.getX() + "  " + el.getY() + " --> ");
            }
            System.out.println("");
        }


        //field.save();
        //field = field.load();
        //field.print();


    }
}
