package ConcreteListeners;

import ConcreteDialogs.OpenFileDialog;
import ConcreteDialogs.ResizeDialog;
import ConcreteDialogs.SaveFileDialog;
import UI.dialog.DialogRaiser;
import DrawableModel.TileMap;

import java.awt.event.MouseEvent;
import java.util.logging.Logger;

/**
 * Raises dialogs when appropriate buttons in UI are pressed
 */
public class FileListener implements UI.Listener{

    static Logger LOGGER = Logger.getLogger(FileListener.class.getName());

    DialogRaiser raiser;
    TileMap map;

    /**
     * needs TileMap target to call appropriate dialog window
     * with this target, which changes it
     * @param raiser (application)
     * @param map target
     */
    public FileListener(DialogRaiser raiser, TileMap map){
        this.raiser = raiser;
        this.map = map;
    }


    /**
     * distributes sub-menu buttons pressing to appropriate dialogs
     * @param function
     * @param activate
     * @param e
     */
    @Override
    public void notify(String function, boolean activate, MouseEvent e) {
        LOGGER.info("Raise " + function + " dialog");

        if(function.equals("resize")) {
            raiser.raiseDialog(new ResizeDialog(map));
        }

        if(function.equals("load")) {
            raiser.raiseDialog(new OpenFileDialog(map));
        }

        if(function.equals("save")) {
            raiser.raiseDialog(new SaveFileDialog(map));
        }
    }

    @Override
    public void drop() {
        // dummy
    }
}
