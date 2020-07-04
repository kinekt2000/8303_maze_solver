package UI;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

abstract class ButtonPanel {
    protected List<Button> buttons;

    private int x;
    private int y;

    private int width;
    private int height;

    protected ButtonPanel(int x, int y, int width, int height) {
        buttons = new ArrayList<>();

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    abstract public int getWidth();
    abstract public int getHeight();


    void setPosition(int x, int y) {
        int dx = x - this.x;
        int dy = y - this.y;

        for(Button button: buttons) {
            button.move(dx, dy);
        }

        this.x = x;
        this.y = y;
    }

    boolean isPointIn(int x, int y) {
        if(x >= this.x && x < this.x + width
        && y >= this.y && y < this.y + height) {
            return true;
        }
        return false;
    }

    boolean isButtonPressed(String name) {
        boolean pressed = false;
        for(Button button: buttons) {
            if(button.getID().equals(name)) {
                pressed = button.isPressed();
                break;
            }
        }
        return pressed;
    }

    void addButton(Button button) {
        button.move(x, y);
        buttons.add(button);
    }

    void drawButtons(Graphics g) {
        for(Button button: buttons) {
            button.draw(g);
        }
    }

    abstract String press(int x, int y);

    void release(String name) {
        for(Button button: buttons) {
            if(button.getID().equals(name)){
                button.release();
                break;
            }
        }
    }

    void drop() {
        for(Button button: buttons) {
            button.release();
        }
    }

    void update(int ms) {
        for(Button button: buttons) {
            button.update(ms);
        }
    }

    abstract void draw(Graphics g);


}
