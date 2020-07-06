package ConcreteListeners;

import DrawableModel.TileMap;
import UI.Listener;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class ObjectsListener implements Listener {

    private enum EntityType {
        Scout, Chest;
    }

    private EntityType type =  null;

    public void setEntity(TileMap map, int x, int y) {
        if(type == EntityType.Scout) {
            map.addScout(x, y);
        } else if (type == EntityType.Chest) {
            map.addChest(x, y);
        }
    }

    public void removeEntity(TileMap map, int x, int y) {
        if(type == EntityType.Scout) {
            map.removeScout(x, y);
        } else if (type == EntityType.Chest) {
            map.removeChest(x, y);
        }
    }

    @Override
    public void notify(String function, boolean activate, MouseEvent e) {
        if(!SwingUtilities.isLeftMouseButton(e)) return;

        if(function == "scout") {
            type = EntityType.Scout;
        } else if (function == "chest") {
            type = EntityType.Chest;
        } else {

        }
    }


    @Override
    public void drop() {
        type = null;
    }
}
