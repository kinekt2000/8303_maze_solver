package ConcreteDialogs;

import DrawableModel.TileMap;

import java.awt.event.MouseEvent;
import java.io.IOException;

public class OpenFileDialog extends FileDialog{
    final public String name = "open_file";

    TileMap target;

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
                    target.load("saves/" + chosen.getLine() + ".pfsv");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (ClassNotFoundException classNotFoundException) {
                    classNotFoundException.printStackTrace();
                }
            }
        }
    }
}
