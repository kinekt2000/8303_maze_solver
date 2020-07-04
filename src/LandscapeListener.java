import UI.dialog.DialogRaiser;
import tiles.TileType;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class LandscapeListener implements UI.Listener{

    tiles.TileType brush;
    DialogRaiser raiser;

    LandscapeListener(DialogRaiser raiser) {
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
