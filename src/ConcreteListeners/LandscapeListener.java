package ConcreteListeners;

import ConcreteDialogs.ChangeTileTypeDialog;
import UI.dialog.DialogRaiser;
import logic.TileType;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class LandscapeListener implements UI.Listener{

    TileType brush;
    DialogRaiser raiser;

    public LandscapeListener(DialogRaiser raiser) {
        this.raiser = raiser;
    }

    @Override
    public void notify(String function, boolean activate, MouseEvent e) {

        if(SwingUtilities.isLeftMouseButton(e)){
            brush = TileType.ID(function);
        } else {
            raiser.raiseDialog(new ChangeTileTypeDialog(function));
        }
    }

    @Override
    public void drop() {
        brush = null;
    }

    public TileType getBrush() {
        return brush;
    }
}
