package DrawableModel;

import logic.Cell;
import render.Drawable;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;

import java.util.List;

public class DrawablePath implements Drawable {

    int mapWidth;
    int mapHeight;
    int tileSize;

    BufferedImage mappedPath;

    public DrawablePath(int mapWidth, int mapHeight, int tileSize) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.tileSize = tileSize;
    }

    public void build(List<Cell> path) {
        if(mappedPath != null) return;
        if(path == null) return;
        if(path.size() < 2) return;

        System.out.println("try to build");

        mappedPath = new BufferedImage(mapWidth * tileSize, mapHeight * tileSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) mappedPath.getGraphics();

        GeneralPath pathLine = new GeneralPath();

        int first_x = tileSize/2 + path.get(0).getX() * tileSize;
        int first_y = tileSize/2 + path.get(0).getY() * tileSize;
        pathLine.moveTo(first_x, first_y);

        for(int i = 1; i < path.size(); i ++) {
            Cell next = path.get(i);

            int next_x = tileSize/2 + next.getX() * tileSize;
            int next_y = tileSize/2 + next.getY() * tileSize;

            pathLine.lineTo(next_x, next_y);
        }

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(Color.BLACK);
        g2d.draw(pathLine);

        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(Color.MAGENTA);
        g2d.draw(pathLine);
        g2d.dispose();
    }

    public void clear() {
        mappedPath = null;
    }

    @Override
    public void draw(Graphics g) {
        if(mappedPath != null) {
            g.drawImage(mappedPath, 0, 0, null);
        }
    }
}
