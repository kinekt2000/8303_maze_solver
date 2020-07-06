package ConcreteListeners;

import ConcreteDialogs.ChangeTileTypeDialog;
import UI.dialog.DialogRaiser;
import logic.TileType;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

/**
 * Listener for landscape sub-menu
 * Has brush field which used by Application to change tiles
 * Can throw dialog, if right mouse button pressed
 */
public class LandscapeListener implements UI.Listener{

    static Logger LOGGER = Logger.getLogger(LandscapeListener.class.getName());

    TileType brush;
    DialogRaiser raiser;

    /**
     * needs raiser (Application in this case) to ask user
     * about new value of tile overlap time
     * @param raiser
     */
    public LandscapeListener(DialogRaiser raiser) {
        this.raiser = raiser;
    }

    /**
     * If MouseEvent is Left Mouse Button, then set brush to appropriate value
     * Else raise Change Tile TYpe Dialog
     * @param function
     * @param activate
     * @param e
     */
    @Override
    public void notify(String function, boolean activate, MouseEvent e) {

        if(SwingUtilities.isLeftMouseButton(e)){
            brush = TileType.ID(function);
            LOGGER.info("Brush set as: " + brush.toString());
        } else {
            raiser.raiseDialog(new ChangeTileTypeDialog(function));
            LOGGER.info("Right button clicked. Raise ChangeTileTypeDialog.");
        }
    }

    /**
     * Sets fields of Listener to default
     */
    @Override
    public void drop() {
        LOGGER.info("brush set as null");
        brush = null;
    }

    public TileType getBrush() {
        return brush;
    }
}
