package UI;

import java.awt.*;
import java.awt.image.BufferedImage;

class LandscapePanel extends ButtonPanel {

    final static public int width = 480;
    final static public int height = 80;

    LandscapePanel(int x, int y,
                   BufferedImage resize_button_texture,
                   BufferedImage grass_brush_texture,
                   BufferedImage sand_brush_texture,
                   BufferedImage gravel_brush_texture,
                   BufferedImage snow_brush_texture,
                   BufferedImage cobblestone_brush_texture)
    {
        super(x, y, width, height);
        super.addButton(new Button("resize", resize_button_texture, 40, 40, 30));
        super.addButton(new Button("grass", grass_brush_texture, 120, 40, 30));
        super.addButton(new Button("sand", sand_brush_texture,200, 40, 30));
        super.addButton(new Button("gravel", gravel_brush_texture,280, 40, 30));
        super.addButton(new Button("snow", snow_brush_texture,360, 40, 30));
        super.addButton(new Button("cobblestone", cobblestone_brush_texture, 440, 40, 30));
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
                if(button.getID() == "resize") {
                    button.press(500);
                    return button.getID();
                } else {
                    button.press();
                    pressed = button;
                }
                break;
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
