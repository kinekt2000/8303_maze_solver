package logic;


import java.util.ArrayList;
import java.util.HashMap;

public class SavesStep { //Класс для сохранения предыдущих шагов
    private final ArrayList<Tile[][]> savesFieldTiles;   //Сохраненные поля предыдущих шагов
    private final ArrayList<Cell> currentCell;       //Текущая клетка
    private final ArrayList<ArrayList<Cell>> notVisitedCells;    //Список непосещенных клеток
    private final ArrayList<int[][]> minimalPathMap;     //Карта минимальных расстояний до всех клеток
    private final ArrayList<ArrayList<Cell>> finishCells;                //Координаты сундуков
    private final ArrayList<ArrayList<Cell>> fullPath;   // Путь (кратчайший) для обходов всех сундуков
    private final ArrayList<HashMap<Cell, Cell>> pathMap;


    public SavesStep(){    //Конструктор
        this.savesFieldTiles = new ArrayList<>();
        this.currentCell = new ArrayList<>();
        this.notVisitedCells = new ArrayList<>();
        this.minimalPathMap = new ArrayList<>();
        this.finishCells = new ArrayList<>();
        this.fullPath = new ArrayList<>();
        this.pathMap = new ArrayList<>();
    }

    public void set(Tile[][] tiles, Cell currentCell, ArrayList<Cell> notVistedCells) throws CloneNotSupportedException { //Записать шаг
        Tile[][] currentTiles = new Tile[tiles.length][tiles[0].length];
        for (int i=0; i<tiles.length; i++)
            for (int j=0; j<tiles[0].length; j++)
                currentTiles[i][j] = tiles[i][j].clone();

        this.savesFieldTiles.add(currentTiles);
        this.currentCell.add(currentCell);
        this.notVisitedCells.add(new ArrayList<>(notVistedCells));
    }

    public void set(Tile[][] tiles, Cell currentCell, ArrayList<Cell> notVistedCells, int[][] minimalPathMap, ArrayList<Cell> finishCells, ArrayList<Cell> fullPath, HashMap<Cell, Cell> pathMap) throws CloneNotSupportedException { //Записать шаг
        Tile[][] currentTiles = new Tile[tiles.length][tiles[0].length];
        for (int i=0; i<tiles.length; i++)
            for (int j=0; j<tiles[0].length; j++)
                currentTiles[i][j] = tiles[i][j].clone();

        this.savesFieldTiles.add(currentTiles);
        this.currentCell.add(currentCell);
        this.notVisitedCells.add(new ArrayList<>(notVistedCells));

        this.minimalPathMap.add(minimalPathMap.clone());
        this.finishCells.add(new ArrayList<>(finishCells));
        this.fullPath.add(new ArrayList<>(fullPath));
        this.pathMap.add(new HashMap<>(pathMap));

    }

    public Tile[][] getTile(){    //Получить поле предыдущего шага
        Tile[][] field = savesFieldTiles.get(savesFieldTiles.size()-2);
        savesFieldTiles.remove(savesFieldTiles.size()-1);
        return field;
    }

    public Cell getCeLL(){    //Получить текущую клетку предыдущего шага
        Cell cell = currentCell.get(currentCell.size()-2);
        currentCell.remove(currentCell.size()-1);
        return cell;
    }

    public ArrayList<Cell> getNotVisitedCell(){  //Получить список непосещенных клеток предыдущего шага
        ArrayList<Cell> notVisitedCell = notVisitedCells.get(notVisitedCells.size()-2);
        notVisitedCells.remove(notVisitedCells.size()-1);
        return notVisitedCell;
    }

    public int[][] getMinimalPathMap(){  //Получить карту минимальных расстояний предыдущего шага
        int[][] pathMap = minimalPathMap.get(minimalPathMap.size()-2);
        minimalPathMap.remove(minimalPathMap.size()-1);
        return pathMap;
    }

    public ArrayList<Cell> getFinishCells(){  //Получить множество конечных точек предыдущего шага
        ArrayList<Cell> finishCell = finishCells.get(finishCells.size()-2);
        finishCells.remove(finishCells.size()-1);
        return finishCell;
    }

    public ArrayList<Cell> getFullPath(){  //Получить путь обхода всех конечных клеток предыдущего шага
        ArrayList<Cell> path = fullPath.get(fullPath.size()-2);
        fullPath.remove(fullPath.size()-1);
        return path;
    }

    public  HashMap<Cell, Cell> getPathMap(){  //Получить пары путей предыдущего шага
        HashMap<Cell, Cell> path = pathMap.get(pathMap.size()-2);
        pathMap.remove(pathMap.size()-1);
        return path;
    }


}


