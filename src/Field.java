import java.io.*;
import java.util.*;

public class Field implements Serializable{

    transient Tile[][] fieldCell;
    private final int width;
    private final int height;


    public Field(int width, int height){
        this.width = width;
        this.height = height;
        fieldCell = new Tile[height][width];
    }

    private Tile isContained(ArrayList<Tile> list, int x, int y){
        for (Tile el: list){
            if (el.getX() == x && el.getY() == y) {
                return el;
            }
        }

        return null;
    }

    public void setRandomLandscape(){
        for (int j = 0; j<height; j++){
            for (int i = 0; i<width; i++)
                fieldCell[j][i] = new Tile(i, j, TileType.random());
        }
    }

    public void print(){
        for (int j = 0; j<height; j++){
            for (int i = 0; i<width; i++){
                System.out.print(fieldCell[j][i].getTileType().getTime() + " ");
            }
            System.out.println("\n");
        }
    }

    public ArrayList<Tile> findPath(Tile startCell, Tile finishCell){
        for (int j=0; j<height; j++)
            for (int i=0; i<width; i++)
                fieldCell[j][i].isVisited = false;

        ArrayList<Tile> notVisitedCells = new ArrayList<>();
        HashMap<Tile, Tile> pathMap = new HashMap<>();
        startCell.setDistance(0);
        startCell.setDistanceFunction(Math.abs(startCell.getX() - finishCell.getX())+ Math.abs(startCell.getY()-finishCell.getY()));
        notVisitedCells.add(startCell);
        Tile currentCell = startCell;
        Tile neighborCell;

        while (currentCell.getX() != finishCell.getX() || currentCell.getY() != finishCell.getY()){
            currentCell = Collections.min(notVisitedCells, (c1, c2) -> (int) (c1.getDistanceFunction() - c2.getDistanceFunction()));
            fieldCell[currentCell.getY()][currentCell.getX()].isVisited = true;
            notVisitedCells.remove(currentCell);
            System.out.println(currentCell.getX() + " " + currentCell.getY() + " " + currentCell.getDistance());

            if (currentCell.getX()-1 >= 0 && !fieldCell[currentCell.getY()][currentCell.getX()-1].isVisited){
                neighborCell = isContained(notVisitedCells, currentCell.getX()-1, currentCell.getY());
                if (neighborCell != null){
                    if (neighborCell.getDistance() > currentCell.getDistance() + fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime()){
                        neighborCell.setDistance(currentCell.getDistance() + fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                        neighborCell.setDistanceFunction(neighborCell.getDistance() + Math.abs(neighborCell.getX() - finishCell.getX())+ Math.abs(neighborCell.getY()-finishCell.getY()));
                        pathMap.put(neighborCell, currentCell);
                    }
                }
                else {
                    neighborCell = new Tile(currentCell.getX()-1, currentCell.getY(), fieldCell[currentCell.getY()][currentCell.getX()-1].getTileType());
                    neighborCell.setDistance(currentCell.getDistance() + fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                    neighborCell.setDistanceFunction(neighborCell.getDistance() + Math.abs(neighborCell.getX() - finishCell.getX())+ Math.abs(neighborCell.getY()-finishCell.getY()));
                    notVisitedCells.add(neighborCell);
                    pathMap.put(neighborCell, currentCell);
                }
            }

            if (currentCell.getX()+1 < width && !fieldCell[currentCell.getY()][currentCell.getX()+1].isVisited){
                neighborCell = isContained(notVisitedCells, currentCell.getX()+1, currentCell.getY());

                if (neighborCell != null){
                    if (neighborCell.getDistance() > currentCell.getDistance() + fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime()){
                        neighborCell.setDistance(currentCell.getDistance() + fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                        neighborCell.setDistanceFunction(neighborCell.getDistance() + Math.abs(neighborCell.getX() - finishCell.getX())+ Math.abs(neighborCell.getY()-finishCell.getY()));
                        pathMap.put(neighborCell, currentCell);
                    }
                }
                else {
                    neighborCell = new Tile(currentCell.getX()+1, currentCell.getY(), fieldCell[currentCell.getY()][currentCell.getX()+1].getTileType());
                    neighborCell.setDistance(currentCell.getDistance() + fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                    neighborCell.setDistanceFunction(neighborCell.getDistance() + Math.abs(neighborCell.getX() - finishCell.getX())+ Math.abs(neighborCell.getY()-finishCell.getY()));
                    notVisitedCells.add(neighborCell);
                    pathMap.put(neighborCell, currentCell);
                }
            }

            if (currentCell.getY()-1 >= 0 && !fieldCell[currentCell.getY()-1][currentCell.getX()].isVisited){
                neighborCell = isContained(notVisitedCells, currentCell.getX(), currentCell.getY()-1);
                if (neighborCell != null){
                    if (neighborCell.getDistance() > currentCell.getDistance() + fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime()){
                        neighborCell.setDistance(currentCell.getDistance() + fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                        neighborCell.setDistanceFunction(neighborCell.getDistance() + Math.abs(neighborCell.getX() - finishCell.getX())+ Math.abs(neighborCell.getY()-finishCell.getY()));
                        pathMap.put(neighborCell, currentCell);
                    }
                }
                else {
                    neighborCell = new Tile(currentCell.getX(), currentCell.getY()-1, fieldCell[currentCell.getY()-1][currentCell.getX()].getTileType());
                    neighborCell.setDistance(currentCell.getDistance() + fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                    neighborCell.setDistanceFunction(neighborCell.getDistance() + Math.abs(neighborCell.getX() - finishCell.getX())+ Math.abs(neighborCell.getY()-finishCell.getY()));
                    notVisitedCells.add(neighborCell);
                    pathMap.put(neighborCell, currentCell);
                }
            }

            if (currentCell.getY()+1 < height && !fieldCell[currentCell.getY()+1][currentCell.getX()].isVisited){
                neighborCell = isContained(notVisitedCells, currentCell.getX(), currentCell.getY()+1);
                if (neighborCell != null){
                    if (neighborCell.getDistance() > currentCell.getDistance() + fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime()){
                        neighborCell.setDistance(currentCell.getDistance() + fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                        neighborCell.setDistanceFunction(neighborCell.getDistance() + Math.abs(neighborCell.getX() - finishCell.getX())+ Math.abs(neighborCell.getY()-finishCell.getY()));
                        pathMap.put(neighborCell, currentCell);
                    }
                }
                else {
                    neighborCell = new Tile(currentCell.getX(), currentCell.getY()+1, fieldCell[currentCell.getY()+1][currentCell.getX()].getTileType());
                    neighborCell.setDistance(currentCell.getDistance() + fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                    neighborCell.setDistanceFunction(neighborCell.getDistance() + Math.abs(neighborCell.getX() - finishCell.getX())+ Math.abs(neighborCell.getY()-finishCell.getY()));
                    notVisitedCells.add(neighborCell);
                    pathMap.put(neighborCell, currentCell);
                }
            }
        }

        ArrayList<Tile> answerPath = new ArrayList<>();
        answerPath.add(currentCell);
        while (currentCell != startCell){
            currentCell = pathMap.get(currentCell);
            answerPath.add(currentCell);
        }

        Collections.reverse(answerPath);
        return answerPath;

    }

    private ArrayList<Tile> findDijkstraPath(Tile startCell, ArrayList<Tile> finishCells){
        for (int j=0; j<height; j++)
            for (int i=0; i<width; i++)
                fieldCell[j][i].isVisited = false;

        ArrayList<Tile> notVisitedCells = new ArrayList<>();
        HashMap<Tile, Tile> pathMap = new HashMap<>();
        int[][] minimalPathMap = new int[height][width];


        startCell.setDistance(0);
        notVisitedCells.add(startCell);
        Tile currentCell = startCell;
        Tile neighborCell;

        while (!notVisitedCells.isEmpty()){
            currentCell = Collections.min(notVisitedCells, (c1, c2) -> (int) (c1.getDistance() - c2.getDistance()));
            fieldCell[currentCell.getY()][currentCell.getX()].isVisited = true;
            notVisitedCells.remove(currentCell);
            System.out.println(currentCell.getX() + " " + currentCell.getY() + " " + currentCell.getDistance());
            minimalPathMap[currentCell.getY()][currentCell.getX()] = currentCell.getDistance();

            if (currentCell.getX()-1 >= 0 && !fieldCell[currentCell.getY()][currentCell.getX()-1].isVisited){
                neighborCell = isContained(notVisitedCells, currentCell.getX()-1, currentCell.getY());
                if (neighborCell != null){
                    if (neighborCell.getDistance() > currentCell.getDistance() + fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime()){
                        neighborCell.setDistance(currentCell.getDistance() + fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                        pathMap.put(neighborCell, currentCell);
                    }
                }
                else {
                    neighborCell = new Tile(currentCell.getX()-1, currentCell.getY(), fieldCell[currentCell.getY()][currentCell.getX()-1].getTileType());
                    neighborCell.setDistance(currentCell.getDistance() + fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                    notVisitedCells.add(neighborCell);
                    pathMap.put(neighborCell, currentCell);
                }
            }

            if (currentCell.getX()+1 < width && !fieldCell[currentCell.getY()][currentCell.getX()+1].isVisited){
                neighborCell = isContained(notVisitedCells, currentCell.getX()+1, currentCell.getY());

                if (neighborCell != null){
                    if (neighborCell.getDistance() > currentCell.getDistance() + fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime()){
                        neighborCell.setDistance(currentCell.getDistance() + fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                        pathMap.put(neighborCell, currentCell);
                    }
                }
                else {
                    neighborCell = new Tile(currentCell.getX()+1, currentCell.getY(), fieldCell[currentCell.getY()][currentCell.getX()+1].getTileType());
                    neighborCell.setDistance(currentCell.getDistance() + fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                    notVisitedCells.add(neighborCell);
                    pathMap.put(neighborCell, currentCell);
                }
            }

            if (currentCell.getY()-1 >= 0 && !fieldCell[currentCell.getY()-1][currentCell.getX()].isVisited){
                neighborCell = isContained(notVisitedCells, currentCell.getX(), currentCell.getY()-1);
                if (neighborCell != null){
                    if (neighborCell.getDistance() > currentCell.getDistance() + fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime()){
                        neighborCell.setDistance(currentCell.getDistance() + fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                        pathMap.put(neighborCell, currentCell);
                    }
                }
                else {
                    neighborCell = new Tile(currentCell.getX(), currentCell.getY()-1, fieldCell[currentCell.getY()-1][currentCell.getX()].getTileType());
                    neighborCell.setDistance(currentCell.getDistance() + fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                    notVisitedCells.add(neighborCell);
                    pathMap.put(neighborCell, currentCell);
                }
            }

            if (currentCell.getY()+1 < height && !fieldCell[currentCell.getY()+1][currentCell.getX()].isVisited){
                neighborCell = isContained(notVisitedCells, currentCell.getX(), currentCell.getY()+1);
                if (neighborCell != null){
                    if (neighborCell.getDistance() > currentCell.getDistance() + fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime()){
                        neighborCell.setDistance(currentCell.getDistance() + fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                        pathMap.put(neighborCell, currentCell);
                    }
                }
                else {
                    neighborCell = new Tile(currentCell.getX(), currentCell.getY()+1, fieldCell[currentCell.getY()+1][currentCell.getX()].getTileType());
                    neighborCell.setDistance(currentCell.getDistance() + fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime());
                    notVisitedCells.add(neighborCell);
                    pathMap.put(neighborCell, currentCell);
                }
            }
        }

        for (int j=0; j<height; j++) {
            for (int i = 0; i < width; i++) {
                System.out.print(minimalPathMap[j][i] + " ");
            }
            System.out.println("");
        }

        Tile nearCell = Collections.min(finishCells, (c1, c2)->(int)(minimalPathMap[c1.getY()][c1.getX()]-minimalPathMap[c2.getY()][c2.getX()]));


        for (Map.Entry<Tile, Tile> pair : pathMap.entrySet()){
            if (pair.getKey().getX() == nearCell.getX() && pair.getKey().getY() == nearCell.getY()){
                currentCell = pair.getKey();
                break;
            }

        }

        ArrayList<Tile> answerPath = new ArrayList<>();
        answerPath.add(currentCell);
        while (currentCell != startCell){
            currentCell = pathMap.get(currentCell);
            answerPath.add(currentCell);
        }

        Collections.reverse(answerPath);
        return answerPath;

    }

    public ArrayList<Tile> findPath(Tile startCell, ArrayList<Tile> finishCells){
        ArrayList<Tile> fullPath = new ArrayList<>();
        ArrayList<Tile> complementaryPath;
        fullPath.add(startCell);
        while (!finishCells.isEmpty()) {
            complementaryPath = findDijkstraPath(fullPath.get(fullPath.size() - 1), finishCells);
            complementaryPath.remove(0);
            fullPath.addAll(complementaryPath);
            for (Tile el: finishCells){
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
                outputStream.writeObject(fieldCell[j][i]);

        outputStream.close();
    }

    public Field load() throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("save.dat"));
        Field field = (Field) inputStream.readObject();

        Tile[][] fieldCell = new Tile[height][width];
        for (int j=0; j<width; j++)
            for (int i=0; i<height; i++)
                fieldCell[j][i] = (Tile) inputStream.readObject();
        field.fieldCell = fieldCell;
        inputStream.close();
        return field;
    }

    public void  findAllPath(Tile finishCell, ArrayList<Tile> currentPath, ArrayList<ArrayList<Tile>> allPaths, int minimalPathLength, int currentPathLength){
        Tile currentCell = currentPath.get(currentPath.size()-1);
        if (currentPathLength == minimalPathLength && currentCell.getX() == finishCell.getX() && currentCell.getY() == finishCell.getY()){
            allPaths.add(new ArrayList<>(currentPath));
        }
        else if (currentPathLength < minimalPathLength){
            if (currentCell.getX()-1 >= 0 && (currentPath.size() == 1 || currentPath.get(currentPath.size()-2).getX() != currentCell.getX()-1)){
                currentPath.add(new Tile(currentCell.getX()-1, currentCell.getY(), fieldCell[currentCell.getY()][currentCell.getX()-1].getTileType()));
                currentPathLength += fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime();
                findAllPath(finishCell, currentPath, allPaths, minimalPathLength, currentPathLength);
                currentPath.remove(currentPath.size()-1);
                currentPathLength -= fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime();
            }

            if (currentCell.getX()+1 < width && (currentPath.size() == 1 || currentPath.get(currentPath.size()-2).getX() != currentCell.getX()+1)){
                currentPath.add(new Tile(currentCell.getX()+1, currentCell.getY(), fieldCell[currentCell.getY()][currentCell.getX()+1].getTileType()));
                currentPathLength += fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime();
                findAllPath(finishCell, currentPath, allPaths, minimalPathLength, currentPathLength);
                currentPath.remove(currentPath.size()-1);
                currentPathLength -= fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime();
            }

            if (currentCell.getY()-1 >= 0 && (currentPath.size() == 1 || currentPath.get(currentPath.size()-2).getY() != currentCell.getY()-1)){
                currentPath.add(new Tile(currentCell.getX(), currentCell.getY()-1, fieldCell[currentCell.getY()-1][currentCell.getX()].getTileType()));
                currentPathLength += fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime();
                findAllPath(finishCell, currentPath, allPaths, minimalPathLength, currentPathLength);
                currentPath.remove(currentPath.size()-1);
                currentPathLength -= fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime();
            }

            if (currentCell.getY()+1 < height && (currentPath.size() == 1 || currentPath.get(currentPath.size()-2).getY() != currentCell.getY()+1)){
                currentPath.add(new Tile(currentCell.getX(), currentCell.getY()+1, fieldCell[currentCell.getY()+1][currentCell.getX()].getTileType()));
                currentPathLength += fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime();
                findAllPath(finishCell, currentPath, allPaths, minimalPathLength, currentPathLength);
                currentPath.remove(currentPath.size()-1);
                currentPathLength -= fieldCell[currentCell.getY()][currentCell.getX()].getTileType().getTime();
            }


        }
    }
    
}
