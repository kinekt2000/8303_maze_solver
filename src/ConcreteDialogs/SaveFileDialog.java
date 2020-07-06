package ConcreteDialogs;

import DrawableModel.TileMap;
import UI.Line;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaveFileDialog extends FileDialog{

    static Logger LOGGER = Logger.getLogger(SaveFileDialog.class.getName());

    int maxInputName = 10;
    boolean input = false;

    TileMap target;

    /**
     * calls save method for target
     * @param target
     */
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
                    target.save(saveFolder.getAbsolutePath() + File.separator + chosen.getLine() + ".pfsv");
                    LOGGER.info("level saved into \"" + chosen.getLine() + ".pfsv\"");
                } catch (IOException ioException) {
                    LOGGER.log(Level.WARNING, "can't access to \"" + chosen.getLine() + ".pfsv\"", ioException);
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
