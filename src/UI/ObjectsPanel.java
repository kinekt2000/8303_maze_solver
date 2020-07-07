package UI;

import java.awt.*;
import java.awt.image.BufferedImage;


/*
 * Panel which works with entities on map,
 * just like scout or chests
 */
class ObjectsPanel extends ButtonPanel{

    final static public int width = 160;
    final static public int height = 80;

    ObjectsPanel(int x, int y,
                 BufferedImage chest_texture,
                 BufferedImage scout_texture) {
        super(x, y, width, height);
        super.addButton(new Button("chest", chest_texture,40, 40, 30));
        super.addButton(new Button("scout", scout_texture,120, 40, 30));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void draw(Graphics g) {
        // draw cosmetics

        // draw buttons
        super.drawButtons(g);
    }

    @Override
    String press(int x, int y) {
        Button pressed = null;

        for(Button button: buttons) {
            if(button.isPointIn(x, y)) {
                button.press();
                pressed = button;
            }
        }

        if(pressed != null) {
            for(Button button: buttons) {
                if(button != pressed) {
                    button.release();
                }
            }

            return pressed.getID();
        }

        return null;
    }
}
