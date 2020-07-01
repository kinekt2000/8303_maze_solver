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

public class ChangeTileTypeDialog implements Dialog {

    public final int width = 310;
    public final int height = 120;
    public final String name = "change_tile_type";

    private int x;
    private int y;

    Button accept;
    Button cancel;

    Line labelLine;
    Line valueLine;
    boolean targeted = false;

    tiles.TileType typeToChange;

    boolean close = false;
    boolean accepted = false;
    boolean canceled = false;

    private int overcomeTime = 0;

    public ChangeTileTypeDialog(String typeName) {
        typeToChange = tiles.TileType.ID(typeName);
        if(typeToChange == null) {
            throw new tiles.TileTypeException("Wrong type cast in ChangeTileTypeDialog");
        } else {
            overcomeTime = typeToChange.getTime();
        }

        BufferedImage acceptTexture = null;
        try{
            acceptTexture = ImageIO.read(new File("assets/interface/accept.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        accept = new Button("accept", acceptTexture, width - 15, height - 15, 10);

        BufferedImage cancelTexture = null;
        try{
            cancelTexture = ImageIO.read(new File("assets/interface/cancel.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        cancel = new Button("cancel", cancelTexture, width - 45, height - 15, 10);

        labelLine = new Line(20, 10, 160, 30);
        labelLine.setLine("Set new overcome time");
        labelLine.setBackColor(new Color(43, 42, 42, 255));
        labelLine.setFrontColor(Color.LIGHT_GRAY);

        valueLine = new Line(75, 50, 160, 30);
        valueLine.setLine("time: " + overcomeTime);
    }

    @Override
    public void setPosition(int x, int y) {
        int dx = x - this.x;
        int dy = y - this.y;

        accept.move(dx, dy);
        cancel.move(dx, dy);
        labelLine.move(dx, dy);
        valueLine.move(dx, dy);

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

        labelLine.draw(g);
        valueLine.draw(g);

        accept.draw(g);
        cancel.draw(g);
    }

    @Override
    public boolean closed() {
        return close;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(!targeted) return;

        char c = e.getKeyChar();

        if(!Character.isDigit(c)) return;

        int digit = Character.getNumericValue(c);

        int newTime = overcomeTime * 10 + digit;
        if(newTime <= 20){
            overcomeTime = newTime;
            valueLine.setLine("time: " + overcomeTime);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(!targeted) return;
        if(e.getKeyCode() != KeyEvent.VK_BACK_SPACE) return;

        overcomeTime /= 10;
        valueLine.setLine("time: " + overcomeTime);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if(accept.isPointIn(x, y)) {
            if(overcomeTime != 0) {
                typeToChange.setTime(overcomeTime);
            }

            accepted = true;
            close = true;
            return;
        }

        if(cancel.isPointIn(x, y)) {
            canceled = true;
            close = true;
            return;
        }

        if(valueLine.isPointIn(x, y)) {
            targeted = !targeted;
            if(targeted) valueLine.setBackColor(Color.LIGHT_GRAY);
            else valueLine.setBackColor(Color.WHITE);
        }
    }

}
