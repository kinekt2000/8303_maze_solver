import java.io.*;
import java.util.*;

public class Field implements Serializable{

    transient Tile[][] fieldTiles;
    private final int width;
    private final int height;


    public Field(int width, int height){
        this.width = width;
        this.height = height;
        fieldTiles = new Tile[height][width];
    }

    private Cell isContained(ArrayList<Cell> list, int x, int y){
        for (Cell el: list){
            if (el.getX() == x && el.getY() == y) {
                return el;
            }
        }

        return null;
    }

    public void setRandomLandscape(){
        for (int j = 0; j<height; j++){
            for (int i = 0; i<width; i++)
                fieldTiles[j][i] = new Tile(i, j, TileType.random());
        }
    }

    public void print(){
        for (int j = 0; j<height; j++){
            for (int i = 0; i<width; i++){
                System.out.print(fieldTiles[j][i].getTileType().getTime() + " ");
            }
            System.out.println("\n");
        }
    }

    public ArrayList<Cell> findPath(Cell startCell, Cell finishCell){
        for (int j=0; j<height; j++)
            for (int i=0; i<width; i++)
                fieldTiles[j][i].isVisited = false;

        ArrayList<Cell> notVisitedCells = new ArrayList<>();
        HashMap<Cell, Cell> pathMap = new HashMap<>();
        startCell.setDistance(0);
        startCell.setDistanceFunction(Math.abs(startCell.getX() - finishCell.getX())+ Math.abs(startCell.getY()-finishCell.getY()));
        notVisitedCells.add(startCell);
        Cell currentCell = startCell;
        Cell neighborCell;

        while (currentCell.getX() != finishCell.getX() || currentCell.getY() != finishCell.getY()){
            currentCell = Collections.min(notVisitedCells, (c1, c2) -> (int) (c1.getDistanceFunction() - c2.getDistanceFunction()));
            fieldTiles[currentCell.getY()][currentCell.getX()].isVisited = true;
            notVisitedCells.remove(currentCell);

            if (currentCell.getX()-1 >= 0 && !fieldTiles[currentCell.getY()][currentCell.getX()-1].isVisited){
                neighborCell = isContained(notVisitedCells, currentCell.getX()-1, currentCell.getY());
                if (neighborCell != null){
                    if (neighborCell.getDistance() > currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime()){
                        neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                        neighborCell.setDistanceFunction(neighborCell.getDistance() + Math.abs(neighborCell.getX() - finishCell.getX())+ Math.abs(neighborCell.getY()-finishCell.getY()));
                        pathMap.put(neighborCell, currentCell);
                    }
                }
                else {
                    neighborCell = new Cell(currentCell.getX()-1, currentCell.getY());
                    neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                    neighborCell.setDistanceFunction(neighborCell.getDistance() + Math.abs(neighborCell.getX() - finishCell.getX())+ Math.abs(neighborCell.getY()-finishCell.getY()));
                    notVisitedCells.add(neighborCell);
                    pathMap.put(neighborCell, currentCell);
                }
            }

            if (currentCell.getX()+1 < width && !fieldTiles[currentCell.getY()][currentCell.getX()+1].isVisited){
                neighborCell = isContained(notVisitedCells, currentCell.getX()+1, currentCell.getY());

                if (neighborCell != null){
                    if (neighborCell.getDistance() > currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime()){
                        neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                        neighborCell.setDistanceFunction(neighborCell.getDistance() + Math.abs(neighborCell.getX() - finishCell.getX())+ Math.abs(neighborCell.getY()-finishCell.getY()));
                        pathMap.put(neighborCell, currentCell);
                    }
                }
                else {
                    neighborCell = new Cell(currentCell.getX()+1, currentCell.getY());
                    neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                    neighborCell.setDistanceFunction(neighborCell.getDistance() + Math.abs(neighborCell.getX() - finishCell.getX())+ Math.abs(neighborCell.getY()-finishCell.getY()));
                    notVisitedCells.add(neighborCell);
                    pathMap.put(neighborCell, currentCell);
                }
            }

            if (currentCell.getY()-1 >= 0 && !fieldTiles[currentCell.getY()-1][currentCell.getX()].isVisited){
                neighborCell = isContained(notVisitedCells, currentCell.getX(), currentCell.getY()-1);
                if (neighborCell != null){
                    if (neighborCell.getDistance() > currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime()){
                        neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                        neighborCell.setDistanceFunction(neighborCell.getDistance() + Math.abs(neighborCell.getX() - finishCell.getX())+ Math.abs(neighborCell.getY()-finishCell.getY()));
                        pathMap.put(neighborCell, currentCell);
                    }
                }
                else {
                    neighborCell = new Cell(currentCell.getX(), currentCell.getY()-1);
                    neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                    neighborCell.setDistanceFunction(neighborCell.getDistance() + Math.abs(neighborCell.getX() - finishCell.getX())+ Math.abs(neighborCell.getY()-finishCell.getY()));
                    notVisitedCells.add(neighborCell);
                    pathMap.put(neighborCell, currentCell);
                }
            }

            if (currentCell.getY()+1 < height && !fieldTiles[currentCell.getY()+1][currentCell.getX()].isVisited){
                neighborCell = isContained(notVisitedCells, currentCell.getX(), currentCell.getY()+1);
                if (neighborCell != null){
                    if (neighborCell.getDistance() > currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime()){
                        neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                        neighborCell.setDistanceFunction(neighborCell.getDistance() + Math.abs(neighborCell.getX() - finishCell.getX())+ Math.abs(neighborCell.getY()-finishCell.getY()));
                        pathMap.put(neighborCell, currentCell);
                    }
                }
                else {
                    neighborCell = new Cell(currentCell.getX(), currentCell.getY()+1);
                    neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                    neighborCell.setDistanceFunction(neighborCell.getDistance() + Math.abs(neighborCell.getX() - finishCell.getX())+ Math.abs(neighborCell.getY()-finishCell.getY()));
                    notVisitedCells.add(neighborCell);
                    pathMap.put(neighborCell, currentCell);
                }
            }
        }

        ArrayList<Cell> answerPath = new ArrayList<>();
        answerPath.add(currentCell);
        while (currentCell != startCell){
            currentCell = pathMap.get(currentCell);
            answerPath.add(currentCell);
        }

        Collections.reverse(answerPath);
        return answerPath;

    }

    private ArrayList<Cell> findDijkstraPath(Cell startCell, ArrayList<Cell> finishCells){
        for (int j=0; j<height; j++)
            for (int i=0; i<width; i++)
                fieldTiles[j][i].isVisited = false;

        ArrayList<Cell> notVisitedCells = new ArrayList<>();
        HashMap<Cell, Cell> pathMap = new HashMap<>();
        int[][] minimalPathMap = new int[height][width];


        startCell.setDistance(0);
        notVisitedCells.add(startCell);
        Cell currentCell = startCell;
        Cell neighborCell;

        while (!notVisitedCells.isEmpty()){
            currentCell = Collections.min(notVisitedCells, (c1, c2) -> (int) (c1.getDistance() - c2.getDistance()));
            fieldTiles[currentCell.getY()][currentCell.getX()].isVisited = true;
            notVisitedCells.remove(currentCell);
            minimalPathMap[currentCell.getY()][currentCell.getX()] = currentCell.getDistance();

            if (currentCell.getX()-1 >= 0 && !fieldTiles[currentCell.getY()][currentCell.getX()-1].isVisited){
                neighborCell = isContained(notVisitedCells, currentCell.getX()-1, currentCell.getY());
                if (neighborCell != null){
                    if (neighborCell.getDistance() > currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime()){
                        neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                        pathMap.put(neighborCell, currentCell);
                    }
                }
                else {
                    neighborCell = new Cell(currentCell.getX()-1, currentCell.getY());
                    neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                    notVisitedCells.add(neighborCell);
                    pathMap.put(neighborCell, currentCell);
                }
            }

            if (currentCell.getX()+1 < width && !fieldTiles[currentCell.getY()][currentCell.getX()+1].isVisited){
                neighborCell = isContained(notVisitedCells, currentCell.getX()+1, currentCell.getY());

                if (neighborCell != null){
                    if (neighborCell.getDistance() > currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime()){
                        neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                        pathMap.put(neighborCell, currentCell);
                    }
                }
                else {
                    neighborCell = new Cell(currentCell.getX()+1, currentCell.getY());
                    neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                    notVisitedCells.add(neighborCell);
                    pathMap.put(neighborCell, currentCell);
                }
            }

            if (currentCell.getY()-1 >= 0 && !fieldTiles[currentCell.getY()-1][currentCell.getX()].isVisited){
                neighborCell = isContained(notVisitedCells, currentCell.getX(), currentCell.getY()-1);
                if (neighborCell != null){
                    if (neighborCell.getDistance() > currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime()){
                        neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                        pathMap.put(neighborCell, currentCell);
                    }
                }
                else {
                    neighborCell = new Cell(currentCell.getX(), currentCell.getY()-1);
                    neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                    notVisitedCells.add(neighborCell);
                    pathMap.put(neighborCell, currentCell);
                }
            }

            if (currentCell.getY()+1 < height && !fieldTiles[currentCell.getY()+1][currentCell.getX()].isVisited){
                neighborCell = isContained(notVisitedCells, currentCell.getX(), currentCell.getY()+1);
                if (neighborCell != null){
                    if (neighborCell.getDistance() > currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime()){
                        neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                        pathMap.put(neighborCell, currentCell);
                    }
                }
                else {
                    neighborCell = new Cell(currentCell.getX(), currentCell.getY()+1);
                    neighborCell.setDistance(currentCell.getDistance() + fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                    notVisitedCells.add(neighborCell);
                    pathMap.put(neighborCell, currentCell);
                }
            }
        }

        Cell nearCell = Collections.min(finishCells, (c1, c2)->(int)(minimalPathMap[c1.getY()][c1.getX()]-minimalPathMap[c2.getY()][c2.getX()]));


        for (Map.Entry<Cell, Cell> pair : pathMap.entrySet()){
            if (pair.getKey().getX() == nearCell.getX() && pair.getKey().getY() == nearCell.getY()){
                currentCell = pair.getKey();
                break;
            }

        }

        ArrayList<Cell> answerPath = new ArrayList<>();
        answerPath.add(currentCell);
        while (currentCell != startCell){
            currentCell = pathMap.get(currentCell);
            answerPath.add(currentCell);
        }

        Collections.reverse(answerPath);
        return answerPath;

    }

    public ArrayList<Cell> findPath(Cell startCell, ArrayList<Cell> finishCells){
        ArrayList<Cell> fullPath = new ArrayList<>();
        ArrayList<Cell> complementaryPath;
        fullPath.add(startCell);
        while (!finishCells.isEmpty()) {
            complementaryPath = findDijkstraPath(fullPath.get(fullPath.size() - 1), finishCells);
            complementaryPath.remove(0);
            fullPath.addAll(complementaryPath);
            for (Cell el: finishCells){
                if (el.getY() == fullPath.get(fullPath.size() - 1).getY() && el.getX() == fullPath.get(fullPath.size() - 1).getX()) {
                    finishCells.remove(el);
                    break;
                }

            }
        }
        return fullPath;
    }

    public void save() throws IOException {
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("save.dat"));
        outputStream.writeObject(this);

        for (int j=0; j<width; j++)
            for (int i=0; i<height; i++)
                outputStream.writeObject(fieldTiles[j][i]);

        outputStream.close();
    }

    public Field load() throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("save.dat"));
        Field field = (Field) inputStream.readObject();

        Tile[][] fieldCell = new Tile[field.height][field.width];
        for (int j=0; j<field.width; j++)
            for (int i=0; i<field.height; i++)
                fieldCell[j][i] = (Tile) inputStream.readObject();
        field.fieldTiles = fieldCell;
        inputStream.close();
        return field;
    }

    public ArrayList<ArrayList<Cell>> findAllPath(Cell startCell, Cell finishCell){
        ArrayList<ArrayList<Cell>> allPaths = new ArrayList<>();
        ArrayList<Cell> currentPath = new ArrayList<>();
        currentPath.add(startCell);
        ArrayList<Cell> aStarPath = findPath(startCell, finishCell);
        int minimalPath = 0;
        for (int i=0; i<aStarPath.size()-1; i++)
            minimalPath += fieldTiles[aStarPath.get(i).getY()][aStarPath.get(i).getX()].getTileType().getTime();
        findAllPathRecursion(finishCell, currentPath, allPaths, minimalPath, 0);
        return allPaths;
    }

    public void  findAllPathRecursion(Cell finishCell, ArrayList<Cell> currentPath, ArrayList<ArrayList<Cell>> allPaths, int minimalPathLength, int currentPathLength){
        Cell currentCell = currentPath.get(currentPath.size()-1);
        if (currentPathLength == minimalPathLength && currentCell.getX() == finishCell.getX() && currentCell.getY() == finishCell.getY()){
            allPaths.add(new ArrayList<>(currentPath));
        }
        else if (currentPathLength < minimalPathLength){
            if (currentCell.getX()-1 >= 0 && (currentPath.size() == 1 || currentPath.get(currentPath.size()-2).getX() != currentCell.getX()-1)){
                currentPath.add(new Cell(currentCell.getX()-1, currentCell.getY()));
                currentPathLength += fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime();
                findAllPathRecursion(finishCell, currentPath, allPaths, minimalPathLength, currentPathLength);
                currentPath.remove(currentPath.size()-1);
                currentPathLength -= fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime();
            }

            if (currentCell.getX()+1 < width && (currentPath.size() == 1 || currentPath.get(currentPath.size()-2).getX() != currentCell.getX()+1)){
                currentPath.add(new Cell(currentCell.getX()+1, currentCell.getY()));
                currentPathLength += fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime();
                findAllPathRecursion(finishCell, currentPath, allPaths, minimalPathLength, currentPathLength);
                currentPath.remove(currentPath.size()-1);
                currentPathLength -= fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime();
            }

            if (currentCell.getY()-1 >= 0 && (currentPath.size() == 1 || currentPath.get(currentPath.size()-2).getY() != currentCell.getY()-1)){
                currentPath.add(new Cell(currentCell.getX(), currentCell.getY()-1));
                currentPathLength += fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime();
                findAllPathRecursion(finishCell, currentPath, allPaths, minimalPathLength, currentPathLength);
                currentPath.remove(currentPath.size()-1);
                currentPathLength -= fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime();
            }

            if (currentCell.getY()+1 < height && (currentPath.size() == 1 || currentPath.get(currentPath.size()-2).getY() != currentCell.getY()+1)){
                currentPath.add(new Cell(currentCell.getX(), currentCell.getY()+1));
                currentPathLength += fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime();
                findAllPathRecursion(finishCell, currentPath, allPaths, minimalPathLength, currentPathLength);
                currentPath.remove(currentPath.size()-1);
                currentPathLength -= fieldTiles[currentCell.getY()][currentCell.getX()].getTileType().getTime();
            }

        }
    }
    
}
