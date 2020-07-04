import UI.Button;
import UI.Line;
import UI.dialog.Dialog;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ResizeDialog implements Dialog{

    final static public int width = 200;
    final static public int height = 120;
    final static public String name = "resize";

    private int x = 0;
    private int y = 0;

    UI.Button accept;
    UI.Button cancel;

    private int mapWidth = 0;
    private int mapHeight = 0;

    Line widthLine;
    Line heightLine;

    Line target = null;

    boolean close = false;
    boolean accepted = false;
    boolean canceled = false;

    public ResizeDialog(){
        BufferedImage acceptTexture = null;
        try{
            acceptTexture = ImageIO.read(new File("assets/interface/accept.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        accept = new UI.Button("accept", acceptTexture, width - 15, height - 15, 10);

        BufferedImage cancelTexture = null;
        try{
            cancelTexture = ImageIO.read(new File("assets/interface/cancel.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        cancel = new UI.Button("cancel", cancelTexture, width - 45, height - 15, 10);

        widthLine = new Line(20, 10, 160, 30);
        widthLine.setLine("width: " + mapWidth);

        heightLine = new Line(20, 50, 160, 30);
        heightLine.setLine("height: " + mapHeight);
    }

    @Override
    public void setPosition(int x, int y) {
        int dx = x - this.x;
        int dy = y - this.y;

        accept.move(dx, dy);
        cancel.move(dx, dy);
        widthLine.move(dx, dy);
        heightLine.move(dx, dy);


        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    @Override
    public boolean isAccepted() {
        return accepted;
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(43, 42, 42, 255));
        g.fillRect(x, y, width, height);

        widthLine.draw(g);
        heightLine.draw(g);

        accept.draw(g);
        cancel.draw(g);
    }

    @Override
    public boolean closed() {
        return close;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(target == null) return;

        char c = e.getKeyChar();

        if(!Character.isDigit(c)) return;

        int digit = Character.getNumericValue(c);

        if(target == widthLine){
            int newMapWidth = mapWidth * 10 + digit;
            if (newMapWidth <= 50) {
                mapWidth = newMapWidth;
                widthLine.setLine("width: " + mapWidth);
            }

            return;
        }

        if(target == heightLine) {
            int newMapHeight = mapHeight * 10 + digit;
            if (newMapHeight <= 50) {
                mapHeight = newMapHeight;
                heightLine.setLine("height: " + mapHeight);
            }

            return;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(target == null) return;
        if(e.getKeyCode() != KeyEvent.VK_BACK_SPACE) return;

        if(target == widthLine) {
            mapWidth /= 10;
            widthLine.setLine("width: " + mapWidth);
            return;
        }

        if(target == heightLine) {
            mapHeight /= 10;
            heightLine.setLine("height: " + mapHeight);
            return;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if(accept.isPointIn(x, y)) {
            accepted = true;
            close = true;
            return;
        }

        if(cancel.isPointIn(x, y)) {
            canceled = true;
            close = true;
            return;
        }

        if(widthLine.isPointIn(x, y)) {
            if(target != null) target.setBackColor(Color.WHITE);
            target = widthLine;
            target.setBackColor(Color.LIGHT_GRAY);
        }

        if(heightLine.isPointIn(x, y)) {
            if(target != null) target.setBackColor(Color.WHITE);
            target = heightLine;
            target.setBackColor(Color.LIGHT_GRAY);
        }
    }
}
