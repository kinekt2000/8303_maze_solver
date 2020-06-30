import UI.Listener;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class ObjectsListener implements Listener {

    boolean setScout = false;
    boolean setChest = false;

    @Override
    public void notify(String function, boolean activate, MouseEvent e) {
        if(!SwingUtilities.isLeftMouseButton(e)) return;

        if(function == "scout") {
            setChest = false;
            setScout = true;
        } else if (function == "chest") {
            setScout = false;
            setChest = true;
        } else {
            System.out.println("Unknown function in ObjectsListener");
        }
    }

    public boolean isSetScout() {
        return setScout;
    }

    public boolean isSetChest() {
        return setChest;
    }

    @Override
    public void drop() {
        setChest = false;
        setScout = false;
    }
}
