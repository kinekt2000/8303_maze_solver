package ConcreteListeners;

import DrawableModel.TileMap;
import UI.Listener;

import java.awt.event.MouseEvent;

public class AlgorithmListener implements Listener{

    TileMap target;

    public AlgorithmListener(TileMap map) throws NullPointerException{
        if(map == null) {
            throw new NullPointerException("null pointer map casted to AlgorithmListener");
        }
        this.target = map;
    }

    @Override
    public void notify(String function, boolean activate, MouseEvent e) {
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
