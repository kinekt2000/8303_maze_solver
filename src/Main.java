import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException, CloneNotSupportedException {

        int x, y;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        x = Integer.parseInt(reader.readLine());
        y = Integer.parseInt(reader.readLine());

        Field field = new Field(x, y);
        field.print();

        Cell startCell = new Cell(0,0);
        /*//Cell finishCell = new Cell(3,3);
        ArrayList<Cell> finishCells = new ArrayList<>();
        finishCells.add(new Cell(4, 0));
        finishCells.add(new Cell(4, 4));
        finishCells.add(new Cell(0, 4));

        field.setStartCell(startCell);
        field.setFinishCells(finishCells);

        for (int i=0; i<35; i++){
            field.nextStep();
            field.printStatusCell();
        }

        field.clear();
        field.printStatusCell();

        field.setStartCell(startCell);
        field.setFinishCells(finishCells);
        while (field.nextStep()){
            field.printStatusCell();
        }

        ArrayList<Cell> path = field.getFullPath();
        for (Cell el: path){
            System.out.print(el.getX() + " " + el.getY() + " --> ");
        }
        System.out.println();*/

        field.setStartCell(startCell);
        field.setFinishCell(new Cell(4,4));
        while (field.nextStep())
            field.printStatusCell();
        ArrayList<Cell> path = field.getPath();

        for (int i=0; i<5; i++){
            field.nextStep();
            field.printStatusCell();
        }

        for (Cell el: path){
            System.out.print(el.getX() + " " + el.getY() + " --> ");
        }
        System.out.println();

        /*ArrayList<ArrayList<Cell>> allPath = field.findAllPath();
        for (ArrayList<Cell> mas: allPath){
            for (Cell el: mas){
                System.out.print(el.getX() + " " + el.getY() + " --> ");
            }
            System.out.println();
        }*/

        //field.save("file.dat");
        field.load("file.dat");
        field.print();



    }
}
