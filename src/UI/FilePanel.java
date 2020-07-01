package UI;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FilePanel extends ButtonPanel{

    final static public int width = 240;
    final static public int height = 80;

    FilePanel(int x, int y,
              BufferedImage resize_file_texture,
              BufferedImage load_file_texture,
              BufferedImage save_file_texture) {
        super(x, y, width, height);

        super.addButton(new Button("resize", resize_file_texture, 40, 40, 30));
        super.addButton(new Button("load", load_file_texture, 120, 40, 30));
        super.addButton(new Button("save", save_file_texture, 200, 40, 30));
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
    String press(int x, int y) {
        Button pressed = null;

        for(Button button: buttons) {
            if(button.isPointIn(x, y)) {
                button.press(500);
                pressed = button;
            }
        }

        if(pressed != null)
            return pressed.getID();
        return null;
    }

    @Override
    void draw(Graphics g) {
        // draw cosmetics

        // draw buttons
        super.drawButtons(g);
    }
}
