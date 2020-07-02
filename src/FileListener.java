import UI.dialog.DialogRaiser;

import java.awt.event.MouseEvent;

public class FileListener implements UI.Listener{

    DialogRaiser raiser;

    FileListener(DialogRaiser raiser){
        this.raiser = raiser;
    }

    @Override
    public void notify(String function, boolean activate, MouseEvent e) {
        System.out.println(function);

        if(function.equals("resize")) {
            raiser.raiseDialog(new ResizeDialog());
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
