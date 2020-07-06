package DrawableModel;

import render.Drawable;
import resources.ResourceManager;

import logic.Cell;
import logic.Field;
import logic.Tile;
import logic.TileType;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Field class wrap, which converts data to drawable objects
 */
public class TileMap extends Field implements Drawable{
    transient private int tileSize = 16; // tile size in pixels

    transient private Scout scout;
    transient private ArrayList<Chest> chests;
    transient DrawablePath drawablePath;

    transient boolean initialized = false; //equals true, when there is a scout and at least one chest

    /**
     * Creates new Map with width and height and fills it with initial type.
     * Size of tile should be defined as textures sizes
     * @param width
     * @param height
     * @param tileSize
     * @param initialType
     */
    public TileMap(int width, int height, int tileSize, TileType initialType) {
        super(width, height, initialType);
        chests = new ArrayList<>();
        this.tileSize =  tileSize;
        drawablePath = new DrawablePath(width, height, tileSize);
    }

    /**
     * Creates new Map with width and height and fills it with random tiles.
     * Size of tile should be defined as textures sizes
     * @param width
     * @param height
     * @param tileSize
     */
    public TileMap(int width, int height, int tileSize) {
        super(width, height);
        chests = new ArrayList<>();
        this.tileSize = tileSize;
        drawablePath = new DrawablePath(width, height, tileSize);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * clears Map, and sets size to width and height with random tiles
     * @param width
     * @param height
     */
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;

        chests.clear();
        scout = null;

        drawablePath = new DrawablePath(width, height, tileSize);
        clear();
        init();

        fieldTiles = new Tile[height][width];
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                fieldTiles[y][x] = new Tile(x, y, TileType.random());
            }
        }
    }

    /**
     * turns TileMap into initial state, but saves scout and chests
     */
    public void reset() {
        drawablePath.clear();
        init();
    }

    /**
     * Change tile on (x, y) to specified type
     * @param x
     * @param y
     * @param type
     */
    public void setCell(int x, int y, TileType type) {
        try{
            fieldTiles[y][x] = new Tile(x, y, type);
            clear();
            init();
        } catch (ArrayIndexOutOfBoundsException dummy) {

        }
    }

    /**
     * Add scout on position (x, y)
     * If it's already here, do nothing
     * @param x
     * @param y
     */
    public void addScout(int x, int y) {
        if(x >= 0 && x < getWidth()
        && y >= 0 && y < getHeight()) {

            if (scout == null) {

                for(Chest chest: chests) {
                    if(chest.getX() == x && chest.getY() == y) {
                        System.out.println("There is a chest");
                        return;
                    }
                }

                scout = new Scout(x, y, tileSize);
                init();

            } else {
                System.out.println("There can be only ONE!");
            }
        }
    }

    /**
     * removes scout from position (x, y)
     * if there is no scout on this position, do nothing
     * @param x
     * @param y
     */
    public void removeScout(int x, int y) {
        if(scout == null) return;
        if(scout.getX() == x && scout.getY() == y) {
            init();
            scout = null;
        }
    }

    /**
     * adds chest on position (x, y)
     * if there is a chest on this position, do nothing
     * @param x
     * @param y
     */
    public void addChest(int x, int y) {
        if(x >= 0 && x < getWidth()
        && y >= 0 && y < getHeight()) {
            for (Chest chest : chests) {
                if (chest.getX() == x && chest.getY() == y) {
                    System.out.println("There already stays chest");
                    return;
                }
            }

            if(scout != null) {
                if(scout.getX() == x && scout.getY() == y) {
                    System.out.println("There is a scout");
                    return;
                }
            }

            chests.add(new Chest(x, y, tileSize));
            init();
        }
    }

    /**
     * removes chest from position (x, y)
     * if there is no chest, do nothing
     * @param x
     * @param y
     */
    public void removeChest(int x, int y) {
        for(Chest chest: chests) {
            if(chest.getX() == x && chest.getY() == y) {
                chests.remove(chest);
                init();
                break;
            }
        }
    }

    /**
     * initializate field, and algorithm
     */
    private void init() {
        clear();
        drawablePath.clear();
        if(scout != null && chests.size() > 0) {
            setStartCell(scout);
            if(chests.size() == 1) {
                setFinishCell(chests.get(0));
            } else {
                setFinishCells(new ArrayList<>(chests));
            }
            initialized = true;
        } else {
            initialized = false;
        }
    }

    /**
     * finds path through chests if there is a scout
     * and at least one chest
     */
    @Override
    public void run() {
        if(initialized) {
            try {
                super.run();
                if(chests.size() == 1) {
                    drawablePath.setPath(getPath());
                } else {
                    drawablePath.setPath(getFullPath());
                }
                clear();
            } catch (CloneNotSupportedException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * does step of algorithm if there is a scout
     * and at least one chest
     */
    public void stepForward() {
        if(initialized) {
            try {
                nextStep();

                if(chests.size() == 1) {
                    drawablePath.setPath(getPath());
                } else {
                    drawablePath.setPath(getFullPath());
                }
            } catch (CloneNotSupportedException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * rollback on one step
     */
    public void stepBack() {
        super.previousStep();
    }

    public void findAll() {
        if(scout != null && chests.size() == 1) {
            try {
                ArrayList<ArrayList<Cell>> paths = findAllPath();
                for(ArrayList<Cell> path: paths) {
                    drawablePath.addPath(path);
                }
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        Color oldColor = g.getColor();

        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                BufferedImage texture = ResourceManager.getTexture(fieldTiles[y][x].getTileType().toString());
                g.drawImage(texture, x * tileSize, y * tileSize, tileSize, tileSize, null);

                if(fieldTiles[y][x].isVisited) {
                    g.setColor(new Color(0, 0, 0, 151));
                    g.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
                } else if(fieldTiles[y][x].isOpen) {
                    g.setColor(new Color(231, 197, 28, 151));
                    g.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
                }
            }
        }

        drawablePath.draw(g);

        if(scout != null) scout.draw(g);
        for(Chest chest: chests) {
            chest.draw(g);
        }

        g.setColor(oldColor);
    }


    /**
     * load map from file, than initiate all other fields
     * @param filename path to save file
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public void load(String filename) throws IOException, ClassNotFoundException {
        super.load(filename);

        clear();
        scout = null;
        chests.clear();
        drawablePath.clear();
        init();
    }
}
