package logic;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {

    @Test
    void nextStep() throws CloneNotSupportedException {
        Field field = new Field(5, 5, TileType.Cobblestone);
        assertFalse(field.nextStep());

        field.setFinishCell(new Cell(2,2));
        assertThrows(NullPointerException.class, () -> field.nextStep());

        field.setStartCell(new Cell(2,3));
        assertTrue(field.nextStep());

        field.run();
        assertFalse(field.nextStep());
    }

    @Test
    void getPath() throws CloneNotSupportedException {
        Field field = new Field(5, 5, TileType.Cobblestone);
        field.setFinishCell(new Cell(2,2));
        field.setStartCell(new Cell(2, 4));
        field.run();

        ArrayList<Cell> list = field.getPath();
        assertEquals(list.get(0).getX(), 2);
        assertEquals(list.get(0).getY(), 4);
        assertEquals(list.get(1).getX(), 2);
        assertEquals(list.get(1).getY(), 3);
        assertEquals(list.get(2).getX(), 2);
        assertEquals(list.get(2).getY(), 2);

        field = new Field(7, 7, TileType.Cobblestone);
        field.setFinishCell(new Cell(3,4));
        field.setStartCell(new Cell(5, 4));
        field.run();

        list = field.getPath();
        assertEquals(list.get(0).getX(), 5);
        assertEquals(list.get(0).getY(), 4);
        assertEquals(list.get(1).getX(), 4);
        assertEquals(list.get(1).getY(), 4);
        assertEquals(list.get(2).getX(), 3);
        assertEquals(list.get(2).getY(), 4);
    }

    @Test
    void getFullPath() throws CloneNotSupportedException {
        Field field = new Field(10, 10, TileType.Grass);
        ArrayList<Cell> finishs = new ArrayList<Cell>();
        finishs.add(new Cell(3, 0));
        finishs.add(new Cell(3, 3));
        field.setFinishCells(finishs);
        field.setStartCell(new Cell(0, 0));
        field.run();

        ArrayList<Cell> list = field.getFullPath();
        assertEquals(list.get(0).getX(), 0);
        assertEquals(list.get(0).getY(), 0);
        assertEquals(list.get(1).getX(), 1);
        assertEquals(list.get(1).getY(), 0);
        assertEquals(list.get(2).getX(), 2);
        assertEquals(list.get(2).getY(), 0);
        assertEquals(list.get(3).getX(), 3);
        assertEquals(list.get(3).getY(), 0);
        assertEquals(list.get(4).getX(), 3);
        assertEquals(list.get(4).getY(), 1);
        assertEquals(list.get(5).getX(), 3);
        assertEquals(list.get(5).getY(), 2);
        assertEquals(list.get(6).getX(), 3);
        assertEquals(list.get(6).getY(), 3);

        field = new Field(10, 10, TileType.Grass);
        finishs = new ArrayList<Cell>();
        finishs.add(new Cell(2, 5));
        finishs.add(new Cell(5, 5));
        field.setFinishCells(finishs);
        field.setStartCell(new Cell(2, 2));
        field.run();

        list = field.getFullPath();
        assertEquals(list.get(0).getX(), 2);
        assertEquals(list.get(0).getY(), 2);
        assertEquals(list.get(1).getX(), 2);
        assertEquals(list.get(1).getY(), 3);
        assertEquals(list.get(2).getX(), 2);
        assertEquals(list.get(2).getY(), 4);
        assertEquals(list.get(3).getX(), 2);
        assertEquals(list.get(3).getY(), 5);
        assertEquals(list.get(4).getX(), 3);
        assertEquals(list.get(4).getY(), 5);
        assertEquals(list.get(5).getX(), 4);
        assertEquals(list.get(5).getY(), 5);
        assertEquals(list.get(6).getX(), 5);
        assertEquals(list.get(6).getY(), 5);
    }

    @Test
    void nextStepFindPathManyTarget() throws CloneNotSupportedException {
        Field field = new Field(10, 10, TileType.Grass);
        ArrayList<Cell> finishs = new ArrayList<Cell>();
        finishs.add(new Cell(3, 0));
        finishs.add(new Cell(3, 3));
        field.setFinishCells(finishs);
        field.setStartCell(new Cell(0, 0));
        assertTrue(field.nextStepFindPathManyTarget());

        field.run();
        assertFalse(field.nextStepFindPathManyTarget());
    }


    @Test
    void findAllPath() throws CloneNotSupportedException {
        Field field = new Field(5, 5, TileType.Cobblestone);
        field.setFinishCell(new Cell(1,1));
        field.setStartCell(new Cell(0, 0));
        ArrayList<ArrayList<Cell>> paths = field.findAllPath();
        if (paths.get(0).get(1).getX() == 1){
            ArrayList<Cell> list = paths.get(0);
            assertEquals(list.get(0).getX(), 0);
            assertEquals(list.get(0).getY(), 0);
            assertEquals(list.get(1).getX(), 1);
            assertEquals(list.get(1).getY(), 0);
            assertEquals(list.get(2).getX(), 1);
            assertEquals(list.get(2).getY(), 1);

            list = paths.get(1);
            assertEquals(list.get(0).getX(), 0);
            assertEquals(list.get(0).getY(), 0);
            assertEquals(list.get(1).getX(), 0);
            assertEquals(list.get(1).getY(), 1);
            assertEquals(list.get(2).getX(), 1);
            assertEquals(list.get(2).getY(), 1);
        }
        else{
            ArrayList<Cell> list = paths.get(0);
            assertEquals(list.get(0).getX(), 0);
            assertEquals(list.get(0).getY(), 0);
            assertEquals(list.get(1).getX(), 0);
            assertEquals(list.get(1).getY(), 1);
            assertEquals(list.get(2).getX(), 1);
            assertEquals(list.get(2).getY(), 1);

            list = paths.get(1);
            assertEquals(list.get(0).getX(), 0);
            assertEquals(list.get(0).getY(), 0);
            assertEquals(list.get(1).getX(), 1);
            assertEquals(list.get(1).getY(), 0);
            assertEquals(list.get(2).getX(), 1);
            assertEquals(list.get(2).getY(), 1);
        }
    }

    @Test
    void findAllPathRecursion() {
    }
}