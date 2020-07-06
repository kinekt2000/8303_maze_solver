package ConcreteDialogs;

import DrawableModel.TileMap;

import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OpenFileDialog extends FileDialog{
    static Logger LOGGER = Logger.getLogger(OpenFileDialog.class.getName());

    final public String name = "open_file";

    TileMap target;

    /**
     * Calls load method for target
     * @param target
     */
    public OpenFileDialog(TileMap target) {
        this.target = target;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);

        if(accepted) {
            if(target != null && chosen.getLine().length() > 0) {
                try {
                    target.load("saves" + File.separator + chosen.getLine() + ".pfsv");
                    LOGGER.info("level \"" + chosen.getLine() + ".pfsv\" loaded");
                } catch (IOException ioException) {
                    LOGGER.log(Level.WARNING, "can't open level \"" + chosen.getLine() + ".pfsv\"", ioException);
                } catch (ClassNotFoundException classNotFoundException) {
                    LOGGER.log(Level.WARNING, "level \"" + chosen.getLine() + ".pfsv\" corrupted", classNotFoundException);
                }
            }
        }
    }
}
