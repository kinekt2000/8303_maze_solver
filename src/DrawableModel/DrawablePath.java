package DrawableModel;

import logic.Cell;
import render.Drawable;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;

import java.util.List;
import java.util.Random;

/**
 * Image with path line, which is superimposed on map
 */
public class DrawablePath implements Drawable {

    int mapWidth;
    int mapHeight;
    int tileSize;

    BufferedImage mappedPath;
    GeneralPath pathLine;
    Random randomizer;

    Color color;
    boolean restrictRecolor = false;

    public DrawablePath(int mapWidth, int mapHeight, int tileSize) {
        randomizer = new Random();

        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.tileSize = tileSize;

        pathLine = new GeneralPath();
    }

    public void addPath(List<Cell> path, Color lineColor) {
        if (path == null) return;
        if (path.size() == 0) return;

        if(mappedPath == null)
            mappedPath = new BufferedImage(mapWidth * tileSize, mapHeight * tileSize, BufferedImage.TYPE_INT_ARGB);

        boolean empty = true;
        pathLine = new GeneralPath();
        for(int i = 0; i < path.size(); i++){
            Cell cell = path.get(i);

            int cell_x = tileSize/2 + cell.getX() * tileSize;
            int cell_y = tileSize/2 + cell.getY() * tileSize;

            if (empty) {
                pathLine.moveTo(cell_x, cell_y);
                empty = false;
            } else {
                if(i > 0 && i < path.size() - 1) {
                    Cell prev = path.get(i-1);
                    Cell next = path.get(i+1);

                    int vpx = cell.getX() - prev.getX(); // Vector Previous X (prev -> cell).x
                    int vpy = cell.getY() - prev.getY(); // Vector Previous Y (prev -> cell).y

                    int vnx = next.getX() - cell.getX(); // Vector Next X (cell -> next).x
                    int vny = next.getY() - cell.getY(); // Vector Next Y (cell -> next).y

                    if(vpx * vnx + vpy * vny == 0) {
                        int border_prev_x = cell_x - vpx * tileSize/2;
                        int border_prev_y = cell_y - vpy * tileSize/2;

                        int border_next_x = cell_x + vnx * tileSize/2;
                        int border_next_y = cell_y + vny * tileSize/2;

                        pathLine.lineTo(border_prev_x, border_prev_y);
                        pathLine.curveTo(border_prev_x, border_prev_y, cell_x, cell_y, border_next_x, border_next_y);
                    } else {
                        pathLine.lineTo(cell_x, cell_y);
                    }
                } else {
                    pathLine.lineTo(cell_x, cell_y);
                }
            }
        }

//        mappedPath = new BufferedImage(mapWidth * tileSize, mapHeight * tileSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) mappedPath.getGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(new Color(0, 0, 0, 127));
        g2d.draw(pathLine);

        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(lineColor);
        g2d.draw(pathLine);
        g2d.dispose();
    }


    public void addPath(List<Cell> path) {
        int r = randomizer.nextInt(256);
        int g = randomizer.nextInt(256);
        int b = randomizer.nextInt(256);

        Color color = new Color(r, g, b, 127);
        addPath(path, color);
    }


    public void setPath(List<Cell> path, Color lineColor) {
        if (path == null) return;
        if (path.size() < 2) return;

        if(!restrictRecolor) {
            restrictRecolor = true;
            color = lineColor;
        }

        mappedPath = null;
        pathLine = new GeneralPath();
        addPath(path, color);
    }


    public void setPath(List<Cell> path) {
        int r = randomizer.nextInt(256);
        int g = randomizer.nextInt(256);
        int b = randomizer.nextInt(256);

        Color color = new Color(r, g, b, 127);
        setPath(path, color);
    }


    public void clear() {
        mappedPath = null;
        pathLine = new GeneralPath();
        restrictRecolor = false;
    }

    @Override
    public void draw(Graphics g) {
        if(mappedPath != null) {
            g.drawImage(mappedPath, 0, 0, null);
        }
    }
}
