package ConcreteListeners;

import DrawableModel.TileMap;
import UI.Listener;

import java.awt.event.MouseEvent;
import java.util.logging.Logger;

/**
 * implements listener to be able to hear events
 * after appropriate button pressed in UI
 */
public class AlgorithmListener implements Listener{

    static Logger LOGGER = Logger.getLogger(AlgorithmListener.class.getName());

    TileMap target;

    /**
     * need TileMap to call appropriate methods, when ui notifies Listsner
     * @param map
     * @throws NullPointerException
     */
    public AlgorithmListener(TileMap map) throws NullPointerException{
        if(map == null) {
            throw new NullPointerException("null pointer map casted to AlgorithmListener");
        }
        this.target = map;
    }

    /**
     * Distribute functions to methods of target (TileMap)
     * UI calls this method
     * @param function
     * @param activate
     * @param e
     */
    @Override
    public void notify(String function, boolean activate, MouseEvent e) {

        LOGGER.info("Call function: " + function);

        if(function.equals("run")) {
            // run algorithm
            target.run();
        }

        if(function.equals("back")) {
            target.stepBack();
        }

        if(function.equals("forward")) {
            // make step of algorithm
            target.stepForward();
        }
    }

    @Override
    public void drop() {
        // dummy
    }
}
