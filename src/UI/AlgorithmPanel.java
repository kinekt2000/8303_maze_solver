package UI;

import java.awt.*;
import java.awt.image.BufferedImage;

class AlgorithmPanel extends ButtonPanel{

    final static public int width = 240;
    final static public int height = 80;

    AlgorithmPanel(int x, int y,
                   BufferedImage run_button_texture,
                   BufferedImage step_forward_texture,
                   BufferedImage step_back_texture) {
        super(x, y, width, height);
        super.addButton(new Button("run", run_button_texture, 40, 40, 30));
        super.addButton(new Button("back", step_back_texture, 120, 40, 30));
        super.addButton(new Button("forward", step_forward_texture, 200, 40, 30));
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
                button.press(500);
                pressed = button;
            }
        }

        if(pressed != null)
            return pressed.getID();
        return null;
    }
}
