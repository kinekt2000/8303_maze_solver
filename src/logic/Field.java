package logic;

import java.io.*;
import java.util.*;

public class Field implements Serializable {    //Класс поля, содержащий алгоритмы для поиска кратчайших путей

    protected transient Tile[][] fieldTiles;  //Карта клеток
    protected int width;    //Ширина
    protected int height;   //Высота

    private transient boolean algIsWork;  //Флаг работы алгоритма поиска пути
    private transient boolean isAlgManyTargetIsWork;  //Флаг работы алгоритма поиска пути до нескольких сундуков
    private transient boolean isAStar;   //Флаг выполнения алгоритма АСтар
    private transient ArrayList<Cell> notVisitedCells;    //Список непосещенных клеток
    private transient ArrayList<Cell> path;   //Путь (кратчайший) до одного сундука
    private transient ArrayList<Cell> fullPath;   // Путь (кратчайший) для обходов всех сундуков
    private transient Cell startCell; // Начальная координата
    private transient Cell finishCell;   //Конечная вершина
    private transient ArrayList<Cell> finishCells;                //Координаты сундуков
    private transient HashMap<Cell, Cell> pathMap;    //Словарь пар, входящих в путь
    private transient Cell currentCell;       //Текущая клетка
    private transient int[][] minimalPathMap;     //Карта минимальных расстояний до всех клеток
    private transient SavesStep savesStep;          //Для сохранения предыдущих шагов


    public Field(int width, int height, TileType initial) {    //Конструктор поля
        this.width = width;
        this.height = height;
        fieldTiles = new Tile[height][width];

        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                fieldTiles[y][x] = new Tile(x, y, initial);
            }
        }

        algIsWork = false;
        isAlgManyTargetIsWork = false;
    }

    public Field(int width, int height) {
        this.width = width;
        this.height = height;
        fieldTiles = new Tile[height][width];

        setRandomLandscape();

        algIsWork = false;
        isAlgManyTargetIsWork = false;
    }

    void setRandomLandscape() {   //Случайное создание поля
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++)
                fieldTiles[j][i] = new Tile(i, j, TileType.random());
        }
    }

    private Cell isContained(ArrayList<Cell> list, int x, int y) {  //Функция, возвращающая из списка клетку с переданными координатами
        for (Cell el : list) {
            if (el.getX() == x && el.getY() == y) {
                return el;
            }
        }

        return null;
    }

    public void setStartCell(Cell startCell) {     //Установить начальную вершину
        this.startCell = startCell;
    }

    public void setFinishCell(Cell finishCell) {  //Установить конечную вершину
        this.finishCell = finishCell;
        this.isAStar = true;
    }

    public void setFinishCells(ArrayList<Cell> finishCells) {  //Установить множество конечных вершин
        this.finishCells = finishCells;
        this.isAStar = false;
    }


    public boolean nextStep() throws CloneNotSupportedException {   //Следующий шаг алгоритма
        if (isAStar)
            return nextStepFindPath();
        else
            return nextStepFindPathManyTarget();


    }

    public void run() throws CloneNotSupportedException {   //Прогнать алгоритм до конца
        while (nextStep()) {

        }
    }

    public void clear() {                  //Очистить поле, прекрать алгоритм
        this.isAlgManyTargetIsWork = false;
        this.algIsWork = false;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                fieldTiles[i][j].isOpen = false;
                fieldTiles[i][j].isVisited = false;
            }
        }
    }

    public void print() {              //Печать рельефа поля
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                System.out.print(fieldTiles[j][i].getTileType().getTime() + " ");
            }
            System.out.println("\n");
        }
    }

    public void printStatusCell() {      //Печать статуса клетки (посещена/непосещена, просмотрена/не просмотрена)
        System.out.println("-------------------------------------------");
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                if (fieldTiles[j][i].isOpen)
                    System.out.print("1 ");
                else
                    System.out.print("0 ");
            }
            System.out.println("\n");
        }
        System.out.println("");

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                if (fieldTiles[j][i].isVisited)
                    System.out.print("2 ");
                else
                    System.out.print("0 ");
            }
            System.out.println("\n");
        }
        System.out.println("-------------------------------------------");


    }

    public ArrayList<Cell> getPath() {  //Функция, фозвращающая кратчайший путь
        return path;
    }

    public ArrayList<Cell> getFullPath() {    //Функция, возвращающая кратчайший путь обхода всех сундуков
        return fullPath;
    }

    public void previousStep() {     //Предыдущий шаг алгоритма
        try {
            fieldTiles = savesStep.getTile();
            currentCell = savesStep.getCeLL();
            notVisitedCells = savesStep.getNotVisitedCell();
            pathMap = savesStep.getPathMap();
            if (isAlgManyTargetIsWork) {
                minimalPathMap = savesStep.getMinimalPathMap();
                fullPath = savesStep.getFullPath();
                finishCells = savesStep.getFinishCells();
            }
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    public boolean nextStepFindPath() throws CloneNotSupportedException { //Следующий шаг поиска кратчайшего пути
        if (!algIsWork) {           //Если алгоритм только запущен, то очищаются статусы клеток, создаются необходимые списки и словари
            for (int j = 0; j < height; j++)
                for (int i = 0; i < width; i++) {
                    fieldTiles[j][i].isVisited = false;   //Объявляем все клетки непосещенными
                    fieldTiles[j][i].isOpen = false;      //Объявляем все клетки неоткрытыми
                }

            notVisitedCells = new ArrayList<>(); //Список непосещенных клеток
            pathMap = new HashMap<>();      //Словарь обратного пути
            startCell.setDistance(0);
            startCell.setDistanceFunction(Math.abs(startCell.getX() - finishCell.getX()) + Math.abs(startCell.getY() - finishCell.getY()));
            notVisitedCells.add(startCell);  //Добавляем начальную клетку в непосещенные
            currentCell = startCell;
            savesStep = new SavesStep();
        }

        algIsWork = stepFindPath();   //Запускается шаг алгоритма
        savesStep.set(fieldTiles, currentCell, notVisitedCells);

        if (!algIsWork) {                    //Если алгоритм закончил работу, то собирается кратчайший путь
            path = new ArrayList<>();
            path.add(currentCell);
            while (currentCell.getX() != startCell.getX() || currentCell.getY() != startCell.getY()) {
                currentCell = pathMap.get(currentCell);
                path.add(currentCell);
            }
            Collections.reverse(path);
        }
        return algIsWork;           //Возвращается статус работы алгоритма (закончился/ еще работает)
    }

    public boolean stepFindPath() { //Шаг поиска поиска кратчайшего пути от одной клетки к другой
        if (currentCell.getX() == finishCell.getX() && currentCell.getY() == finishCell.getY())   //Если найден путь то финальной клетки, то подача сигнала о завершении
            return false;

        currentCell = Collections.min(notVisitedCells, (c1, c2) -> (int) (c1.getDistanceFunction() - c2.getDistanceFunction()));  //Берется ближайшая клетка
        fieldTiles[currentCell.getY()][currentCell.getX()].isVisited = true;   //Отмечается как посещенная
        notVisitedCells.remove(currentCell);        //Удаляется из списка непосещенных
        Cell neighborCell;
        if (currentCell.getX() - 1 >= 0 && !fieldTiles[currentCell.getY()][currentCell.getX() - 1].isVisited) {  //Если соседняя клетка существует и непосещена
            fieldTiles[currentCell.getY()][currentCell.getX() - 1].isOpen = true;   //Отмечается как просмотренная
            neighborCell = isContained(notVisitedCells, currentCell.getX() - 1, currentCell.getY());  //Ищется в списке непосещенных клеток
            if (neighborCell != null) {        //Если она найдена в этом списке
                if (neighborCell.getDistance() > currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime()) {  //Если в соседнюю вершину есть путь короче через текущую
                    neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());   //Пересчитывается расстояние до нее
                    neighborCell.setDistanceFunction(neighborCell.getDistance() + Math.abs(neighborCell.getX() - finishCell.getX()) + Math.abs(neighborCell.getY() - finishCell.getY()));  //Пересчитывается эвристическая функция
                    pathMap.put(neighborCell, currentCell);   //Добавляется в путь
                }
            } else {
                neighborCell = new Cell(currentCell.getX() - 1, currentCell.getY());   //Если сосед не найден в списке непосещенных вершин, то эта вершина создается
                neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());  //Высчитывается расстояние
                neighborCell.setDistanceFunction(neighborCell.getDistance() + Math.abs(neighborCell.getX() - finishCell.getX()) + Math.abs(neighborCell.getY() - finishCell.getY()));  //Высчитывается эвристическая функция
                notVisitedCells.add(neighborCell);   //Добавляется в список непосещенных
                pathMap.put(neighborCell, currentCell);  //Добавляется в путь
            }
        }

        if (currentCell.getX() + 1 < width && !fieldTiles[currentCell.getY()][currentCell.getX() + 1].isVisited) {
            neighborCell = isContained(notVisitedCells, currentCell.getX() + 1, currentCell.getY());
            fieldTiles[currentCell.getY()][currentCell.getX() + 1].isOpen = true;
            if (neighborCell != null) {
                if (neighborCell.getDistance() > currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime()) {
                    neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                    neighborCell.setDistanceFunction(neighborCell.getDistance() + Math.abs(neighborCell.getX() - finishCell.getX()) + Math.abs(neighborCell.getY() - finishCell.getY()));
                    pathMap.put(neighborCell, currentCell);
                }
            } else {
                neighborCell = new Cell(currentCell.getX() + 1, currentCell.getY());
                neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                neighborCell.setDistanceFunction(neighborCell.getDistance() + Math.abs(neighborCell.getX() - finishCell.getX()) + Math.abs(neighborCell.getY() - finishCell.getY()));
                notVisitedCells.add(neighborCell);
                pathMap.put(neighborCell, currentCell);
            }
        }

        if (currentCell.getY() - 1 >= 0 && !fieldTiles[currentCell.getY() - 1][currentCell.getX()].isVisited) {
            neighborCell = isContained(notVisitedCells, currentCell.getX(), currentCell.getY() - 1);
            fieldTiles[currentCell.getY() - 1][currentCell.getX()].isOpen = true;
            if (neighborCell != null) {
                if (neighborCell.getDistance() > currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime()) {
                    neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                    neighborCell.setDistanceFunction(neighborCell.getDistance() + Math.abs(neighborCell.getX() - finishCell.getX()) + Math.abs(neighborCell.getY() - finishCell.getY()));
                    pathMap.put(neighborCell, currentCell);
                }
            } else {
                neighborCell = new Cell(currentCell.getX(), currentCell.getY() - 1);
                neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                neighborCell.setDistanceFunction(neighborCell.getDistance() + Math.abs(neighborCell.getX() - finishCell.getX()) + Math.abs(neighborCell.getY() - finishCell.getY()));
                notVisitedCells.add(neighborCell);
                pathMap.put(neighborCell, currentCell);
            }
        }

        if (currentCell.getY() + 1 < height && !fieldTiles[currentCell.getY() + 1][currentCell.getX()].isVisited) {
            neighborCell = isContained(notVisitedCells, currentCell.getX(), currentCell.getY() + 1);
            fieldTiles[currentCell.getY() + 1][currentCell.getX()].isOpen = true;
            if (neighborCell != null) {
                if (neighborCell.getDistance() > currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime()) {
                    neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                    neighborCell.setDistanceFunction(neighborCell.getDistance() + Math.abs(neighborCell.getX() - finishCell.getX()) + Math.abs(neighborCell.getY() - finishCell.getY()));
                    pathMap.put(neighborCell, currentCell);
                }
            } else {
                neighborCell = new Cell(currentCell.getX(), currentCell.getY() + 1);
                neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                neighborCell.setDistanceFunction(neighborCell.getDistance() + Math.abs(neighborCell.getX() - finishCell.getX()) + Math.abs(neighborCell.getY() - finishCell.getY()));
                notVisitedCells.add(neighborCell);
                pathMap.put(neighborCell, currentCell);
            }
        }

        return true;

    }

    private boolean nextStepFindDijkstraPath() {  //Следующий шаг алгоритма Дейкстры
        if (!algIsWork) {     //Если алгоритм только запущен, то очищаются статусы клеток, создаются необходимые списки и словари
            for (int j = 0; j < height; j++)
                for (int i = 0; i < width; i++) {
                    fieldTiles[j][i].isVisited = false;   //Объявляем все клетки непосещенными
                    fieldTiles[j][i].isOpen = false;      //Объявляем все клетки неоткрытыми
                }

            notVisitedCells = new ArrayList<>();   //Список непосещенных клеток
            pathMap = new HashMap<>();              //Словарь пути
            minimalPathMap = new int[height][width];  //Карта минмиальных путей до клеток


            startCell.setDistance(0);
            notVisitedCells.add(startCell);
            currentCell = startCell;
        }

        algIsWork = stepFindDijkstraPath();  //Осуществление следующего шага алгоритма Дейкстры
        if (!algIsWork) {   //Если алгоритм закончил работу, находится ближайшая вершина, строится путь до нее
            Cell nearCell = Collections.min(finishCells, (c1, c2) -> (int) (minimalPathMap[c1.getY()][c1.getX()] - minimalPathMap[c2.getY()][c2.getX()]));


            for (Map.Entry<Cell, Cell> pair : pathMap.entrySet()) {
                if (pair.getKey().getX() == nearCell.getX() && pair.getKey().getY() == nearCell.getY()) {
                    currentCell = pair.getKey();
                    break;
                }
            }

            path = new ArrayList<>();
            path.add(currentCell);

            while (currentCell != startCell) {
                currentCell = pathMap.get(currentCell);
                path.add(currentCell);
            }

            Collections.reverse(path);
        }
        return algIsWork;
    }

    private boolean stepFindDijkstraPath() {  //Шаг алгоритма Дейкстры

        if (notVisitedCells.isEmpty())  //Если все клетки посещены, то алгоритм заканчивает работу
            return false;

        currentCell = Collections.min(notVisitedCells, (c1, c2) -> (int) (c1.getDistance() - c2.getDistance()));  //Берется ближайшая клетка из списка непосещенных вершин
        fieldTiles[currentCell.getY()][currentCell.getX()].isVisited = true;   //Отмечается как посещенная
        notVisitedCells.remove(currentCell);   //Удаляется из списка непосещенных
        minimalPathMap[currentCell.getY()][currentCell.getX()] = currentCell.getDistance();  //Изменяется расстояние в карте
        Cell neighborCell;

        if (currentCell.getX() - 1 >= 0 && !fieldTiles[currentCell.getY()][currentCell.getX() - 1].isVisited) {  //Если вершина существует и еще не посещена
            fieldTiles[currentCell.getY()][currentCell.getX() - 1].isOpen = true;   //Отмечается как просмотренная
            neighborCell = isContained(notVisitedCells, currentCell.getX() - 1, currentCell.getY());  //Ищется сосед в списке непосещенных клеток
            if (neighborCell != null) {  //Если сосед найден
                if (neighborCell.getDistance() > currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime()) {   //Если расстояние до соседа через текущую вершину меньшу, то оно пересчитывается
                    neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                    pathMap.put(neighborCell, currentCell);   //Добавляется в карту путей
                }
            } else {
                neighborCell = new Cell(currentCell.getX() - 1, currentCell.getY());   //Если соседа нет, то он создается
                neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());  //Высчитывается расстояние до него
                notVisitedCells.add(neighborCell);  //Добавляется в список непосещенных клеток
                pathMap.put(neighborCell, currentCell);  //Добавляется в карту путей
            }
        }

        if (currentCell.getX() + 1 < width && !fieldTiles[currentCell.getY()][currentCell.getX() + 1].isVisited) {
            neighborCell = isContained(notVisitedCells, currentCell.getX() + 1, currentCell.getY());
            fieldTiles[currentCell.getY()][currentCell.getX() + 1].isOpen = true;
            if (neighborCell != null) {
                if (neighborCell.getDistance() > currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime()) {
                    neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                    pathMap.put(neighborCell, currentCell);
                }
            } else {
                neighborCell = new Cell(currentCell.getX() + 1, currentCell.getY());
                neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                notVisitedCells.add(neighborCell);
                pathMap.put(neighborCell, currentCell);
            }
        }

        if (currentCell.getY() - 1 >= 0 && !fieldTiles[currentCell.getY() - 1][currentCell.getX()].isVisited) {
            neighborCell = isContained(notVisitedCells, currentCell.getX(), currentCell.getY() - 1);
            fieldTiles[currentCell.getY() - 1][currentCell.getX()].isOpen = true;
            if (neighborCell != null) {
                if (neighborCell.getDistance() > currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime()) {
                    neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                    pathMap.put(neighborCell, currentCell);
                }
            } else {
                neighborCell = new Cell(currentCell.getX(), currentCell.getY() - 1);
                neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                notVisitedCells.add(neighborCell);
                pathMap.put(neighborCell, currentCell);
            }
        }

        if (currentCell.getY() + 1 < height && !fieldTiles[currentCell.getY() + 1][currentCell.getX()].isVisited) {
            neighborCell = isContained(notVisitedCells, currentCell.getX(), currentCell.getY() + 1);
            fieldTiles[currentCell.getY() + 1][currentCell.getX()].isOpen = true;
            if (neighborCell != null) {
                if (neighborCell.getDistance() > currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime()) {
                    neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                    pathMap.put(neighborCell, currentCell);
                }
            } else {
                neighborCell = new Cell(currentCell.getX(), currentCell.getY() + 1);
                neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                notVisitedCells.add(neighborCell);
                pathMap.put(neighborCell, currentCell);
            }
        }

        return true;

    }

    public boolean nextStepFindPathManyTarget() throws CloneNotSupportedException {  //Следующий шаг поиска пути обхода всех вершин

        if (!isAlgManyTargetIsWork) {     //Если алгоритм запущен впервые
            fullPath = new ArrayList<>();  //Создается список полного пути
            fullPath.add(startCell);          //Добавляется начальная вершина
            this.finishCells = new ArrayList<>(finishCells);
            savesStep = new SavesStep();
            isAlgManyTargetIsWork = true;
        }

        if (this.finishCells.isEmpty()) {          //Если все сундуки посещены, то алгоритм заканчивается
            isAlgManyTargetIsWork = false;
            return false;
        }

        this.startCell = fullPath.get(fullPath.size() - 1);
        boolean existNextStepDijkstra = nextStepFindDijkstraPath();

        savesStep.set(fieldTiles, currentCell, notVisitedCells, minimalPathMap, this.finishCells, fullPath, pathMap);
        if (!existNextStepDijkstra) { //Запускается следующий шаг алгоритма Дейкстры
            //Если алгоритм Дейкстры закончил работу, в полный путь добавляется путь до ближайшей вершины, вершина удаляется и целевых для посещения
            ArrayList<Cell> complementaryPath = getPath();
            complementaryPath.remove(0);
            fullPath.addAll(complementaryPath);
            for (Cell el : this.finishCells) {
                if (el.getY() == fullPath.get(fullPath.size() - 1).getY() && el.getX() == fullPath.get(fullPath.size() - 1).getX()) {
                    this.finishCells.remove(el);
                    break;
                }

            }
        }

        return true;
    }


    public void save(String filename) throws IOException {    //Функция сохранения поля
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename, false));
        outputStream.writeObject(this);

        for (int j = 0; j < width; j++)
            for (int i = 0; i < height; i++)
                outputStream.writeObject(fieldTiles[j][i]);   //Сохраняется каждая клетка

        outputStream.close();
    }

    public Field load(String filename) throws IOException, ClassNotFoundException {      //Функция загрузки поля
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename));
        Field field = (Field) inputStream.readObject();       //Загрузка поля

        Tile[][] fieldCell = new Tile[field.height][field.width];
        for (int j = 0; j < field.width; j++)
            for (int i = 0; i < field.height; i++)
                fieldCell[j][i] = (Tile) inputStream.readObject();  //Загрузка каждой клетки
        field.fieldTiles = fieldCell;
        inputStream.close();
        return field;
    }

    public ArrayList<ArrayList<Cell>> findAllPath() throws CloneNotSupportedException {  //Нахождение всех кратчайших путей до сундука
        ArrayList<ArrayList<Cell>> allPaths = new ArrayList<>();  //Список всех путей
        ArrayList<Cell> currentPath = new ArrayList<>();   //Текущий путь
        currentPath.add(startCell);
        run();
        ArrayList<Cell> aStarPath = getPath();
        int minimalPath = 0;
        for (int i = 0; i < aStarPath.size() - 1; i++)
            minimalPath += fieldTiles[aStarPath.get(i).getY()][aStarPath.get(i).getX()].getTileType().getTime(); //Ищется длина кратчайшего пути
        findAllPathRecursion(currentPath, allPaths, minimalPath, 0); //Запускается рекурсивный алгоритм
        return allPaths;
    }

    public void findAllPathRecursion(ArrayList<Cell> currentPath, ArrayList<ArrayList<Cell>> allPaths, int minimalPathLength, int currentPathLength) { //Рекурсивная функция поиска альтернативных путей
        Cell currentCell = currentPath.get(currentPath.size() - 1);
        if (currentPathLength == minimalPathLength && currentCell.getX() == finishCell.getX() && currentCell.getY() == finishCell.getY()) {
            allPaths.add(new ArrayList<>(currentPath));  //Если найден еще один путь, то добавляется в список всех путей
        } else if (currentPathLength < minimalPathLength) {  //Если длина текущего пути все еще меньше минимального
            if (currentCell.getX() - 1 >= 0 && (currentPath.size() == 1 || currentPath.get(currentPath.size() - 2).getX() != currentCell.getX() - 1)) { //Если клетка существует и не была посещена на предыдущем этапе
                currentPath.add(new Cell(currentCell.getX() - 1, currentCell.getY()));     //Добавляется в текущий путь
                currentPathLength += fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime();  //Пересчитывается путь, включая добавленную клетку
                findAllPathRecursion(currentPath, allPaths, minimalPathLength, currentPathLength);   //Запускается рекурсивно функция
                currentPath.remove(currentPath.size() - 1);                                                   //На обратном ходу вершина удаляется из текущего пути
                currentPathLength -= fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime();  //Пересчитывается длина, не учитывая текущую вершину
            }

            if (currentCell.getX() + 1 < width && (currentPath.size() == 1 || currentPath.get(currentPath.size() - 2).getX() != currentCell.getX() + 1)) {
                currentPath.add(new Cell(currentCell.getX() + 1, currentCell.getY()));
                currentPathLength += fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime();
                findAllPathRecursion(currentPath, allPaths, minimalPathLength, currentPathLength);
                currentPath.remove(currentPath.size() - 1);
                currentPathLength -= fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime();
            }

            if (currentCell.getY() - 1 >= 0 && (currentPath.size() == 1 || currentPath.get(currentPath.size() - 2).getY() != currentCell.getY() - 1)) {
                currentPath.add(new Cell(currentCell.getX(), currentCell.getY() - 1));
                currentPathLength += fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime();
                findAllPathRecursion(currentPath, allPaths, minimalPathLength, currentPathLength);
                currentPath.remove(currentPath.size() - 1);
                currentPathLength -= fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime();
            }

            if (currentCell.getY() + 1 < height && (currentPath.size() == 1 || currentPath.get(currentPath.size() - 2).getY() != currentCell.getY() + 1)) {
                currentPath.add(new Cell(currentCell.getX(), currentCell.getY() + 1));
                currentPathLength += fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime();
                findAllPathRecursion(currentPath, allPaths, minimalPathLength, currentPathLength);
                currentPath.remove(currentPath.size() - 1);
                currentPathLength -= fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime();
            }

        }
    }


}
