package UI;

import java.awt.event.MouseEvent;


/*
 * interfaces of objects which will be
 * called when appropriate buttons pressed
 */
public interface Listener {
    void notify(String function, boolean activate, MouseEvent e);
    void drop();
}
