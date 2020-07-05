package ConcreteDialogs;

import DrawableModel.TileMap;
import UI.Line;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class SaveFileDialog extends FileDialog{

    int maxInputName = 10;
    boolean input = false;

    TileMap target;

    public SaveFileDialog(TileMap target) {
        this.target = target;
    }

    @Override
    public String getName() {
        return "save";
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(chosen.isPointIn(e.getX(), e.getY())) {
            input = true;
            chosen.setBackColor(Color.LIGHT_GRAY);
            for(Line line: files) {
                line.setBackColor(Color.WHITE);
            }
            return;
        }

        super.mouseClicked(e);

        if(accepted) {
            if(target != null && chosen.getLine().length() > 0) {
                try {
                    target.save("saves/" + chosen.getLine() + ".pfsv");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(!input) return;
        if(chosen.getLine().length() >= maxInputName) return;

        char c = e.getKeyChar();
        if(!Character.isLetterOrDigit(c)) return;

        chosen.setLine(chosen.getLine() + c);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(!input) return;
        int inputLength = chosen.getLine().length();
        if(inputLength == 0) return;

        if(e.getKeyCode() != KeyEvent.VK_BACK_SPACE) return;

        chosen.setLine(chosen.getLine().substring(0, inputLength-1));
    }
}
