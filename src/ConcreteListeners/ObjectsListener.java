package ConcreteListeners;

import DrawableModel.TileMap;
import UI.Listener;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;


/**
 * Listener can set its Entity to Field, when Application asks
 */
public class ObjectsListener implements Listener {

    static Logger LOGGER = Logger.getLogger(ObjectsListener.class.getName());

    private enum EntityType {
        Scout, Chest;
    }

    private EntityType type =  null;

    /**
     * Set Entity of listener to map on position (x, y)
     * @param map
     * @param x
     * @param y
     */
    public void setEntity(TileMap map, int x, int y) {
        if(type == null) return;

        LOGGER.info("ObjectListener tries to set " + type.toString() + " on position (" + x + "; " + y + ")");

        if(type == EntityType.Scout) {
            map.addScout(x, y);
        } else if (type == EntityType.Chest) {
            map.addChest(x, y);
        }
    }

    /**
     * remove Entity of listener from map on position (x, y)
     * @param map
     * @param x
     * @param y
     */
    public void removeEntity(TileMap map, int x, int y) {
        LOGGER.info("ObjectListener tries to remove " + type.toString() + " from position (" + x + "; " + y + ")");
        if(type == EntityType.Scout) {
            map.removeScout(x, y);
        } else if (type == EntityType.Chest) {
            map.removeChest(x, y);
        }
    }

    /**
     * sets Entity of listener using function
     * @param function
     * @param activate
     * @param e
     */
    @Override
    public void notify(String function, boolean activate, MouseEvent e) {
        if(!SwingUtilities.isLeftMouseButton(e)) return;

        if(function == "scout") {
            LOGGER.info("Entity to place: Scout");
            type = EntityType.Scout;
        } else if (function == "chest") {
            LOGGER.info("Entity to place: Chest");
            type = EntityType.Chest;
        } else {

        }
    }


    /**
     * sets Entity to null
     */
    @Override
    public void drop() {
        LOGGER.info("Entity to place: null");
        type = null;
    }
}
