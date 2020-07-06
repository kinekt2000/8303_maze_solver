package DrawableModel;

import render.Drawable;
import resources.ResourceManager;

import logic.Field;
import logic.Tile;
import logic.TileType;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class TileMap extends Field implements Drawable{
    transient int tileSize = 16; // tile size in pixels

    transient Scout scout;
    transient ArrayList<Chest> chests;
    transient DrawablePath drawablePath;

    transient boolean initialized = false; //equals true, when there is a scout and at least one chest

    public TileMap(int width, int height, int tileSize, TileType initialType) {
        super(width, height, initialType);
        chests = new ArrayList<>();
        this.tileSize =  tileSize;
        drawablePath = new DrawablePath(width, height, tileSize);
    }

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

    public void setCell(int x, int y, TileType type) {
        try{
            fieldTiles[y][x] = new Tile(x, y, type);
            clear();
            init();
        } catch (ArrayIndexOutOfBoundsException dummy) {

        }
    }

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

    public void removeScout(int x, int y) {
        if(scout == null) return;
        if(scout.getX() == x && scout.getY() == y) {
            init();
            scout = null;
        }
    }

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

    public void removeChest(int x, int y) {
        for(Chest chest: chests) {
            if(chest.getX() == x && chest.getY() == y) {
                chests.remove(chest);
                init();
                break;
            }
        }
    }

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


    public void stepForward() {
        if(initialized) {
            try {
                if(!nextStep()) {
                    clear();
                }

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

    public void stepBack() {
        super.previousStep();
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

    @Override
    public void save(String filename) throws IOException {
        ((Field) this).save(filename);
    }

//    @Override
//    public void load(String filename) throws IOException, ClassNotFoundException {
//
//        return super.load(filename);
//    }
}