package UI;

import java.awt.*;
import java.awt.image.BufferedImage;

class MainPanel extends ButtonPanel{

    final static public int width = 160;
    final static public int height = 640;

    MainPanel(int x, int y,
                     BufferedImage file_button_texture,
                     BufferedImage landscape_button_texture,
                     BufferedImage add_object_button_texture,
                     BufferedImage algorithm_button_texture)
    {
        super(x, y , width, height);
        super.addButton(new Button("file_menu", file_button_texture, 80, 80, 60));
        super.addButton(new Button("landscape_menu", landscape_button_texture, 80, 240, 60));
        super.addButton(new Button("objects_menu", add_object_button_texture, 80, 400, 60));
        super.addButton(new Button("algorithm_menu", algorithm_button_texture, 80, 560, 60));
    }

    public int getWidth() {
        return width;
    }


    public int getHeight() {
        return height;
    }

    @Override
    void draw(Graphics g) {
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
