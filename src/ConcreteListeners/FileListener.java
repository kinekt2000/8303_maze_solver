package ConcreteListeners;

import ConcreteDialogs.OpenFileDialog;
import ConcreteDialogs.ResizeDialog;
import ConcreteDialogs.SaveFileDialog;
import UI.dialog.DialogRaiser;
import DrawableModel.TileMap;

import java.awt.event.MouseEvent;

public class FileListener implements UI.Listener{

    DialogRaiser raiser;
    TileMap map;

    public FileListener(DialogRaiser raiser, TileMap map){
        this.raiser = raiser;
        this.map = map;
    }

    @Override
    public void notify(String function, boolean activate, MouseEvent e) {
        System.out.println(function);

        if(function.equals("resize")) {
            raiser.raiseDialog(new ResizeDialog(map));
        }

        if(function.equals("load")) {
            raiser.raiseDialog(new OpenFileDialog());
        }

        if(function.equals("save")) {
            raiser.raiseDialog(new SaveFileDialog());
        }
    }

    @Override
    public void drop() {
        // dummy
    }
}
