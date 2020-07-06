package UI;

import java.awt.event.MouseEvent;

public interface Listener {
    void notify(String function, boolean activate, MouseEvent e);
    void drop();
}
