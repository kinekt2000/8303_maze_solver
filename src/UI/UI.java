package UI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UI{

    static Logger LOGGER = Logger.getLogger(UI.class.getName());

    int width;
    int height;

    MainPanel main;
    Map<String, ButtonPanel> additional;
    Map<String, Listener> listeners;

    String subMenuID;
    ButtonPanel subMenu;

    /**
     * Builds interface with button panels on right and bottom borders
     * @param width         width of window, to calculate positions of UI
     * @param height        height of window, to calculate positions of UI
     * @throws IOException  thrown when there is no appropriate assets
     */
    public UI(int width, int height) throws IOException {

        URL url = getClass().getResource("/assets/interface/file.png");
        System.out.println(url);

        this.width = width;
        this.height = height;

        additional = new HashMap<>();
        listeners = new HashMap<>();
        ButtonPanel current;

        LOGGER.info("UI building");
        try {
            main = new MainPanel(width - MainPanel.width, 0,
                    ImageIO.read(getClass().getResource("/assets/interface/file.png")),
                    ImageIO.read(getClass().getResource("/assets/interface/brush.png")),
                    ImageIO.read(getClass().getResource("/assets/interface/object.png")),
                    ImageIO.read(getClass().getResource("/assets/interface/path.png")));
            LOGGER.info("Main panel is built");

            additional.put("file_menu", new FilePanel(0, height - FilePanel.height,
                    ImageIO.read(getClass().getResource("/assets/interface/resize.png")),
                    ImageIO.read(getClass().getResource("/assets/interface/load.png")),
                    ImageIO.read(getClass().getResource("/assets/interface/save.png"))));
            LOGGER.info("File sub-menu is built");

            additional.put("landscape_menu", new LandscapePanel(0, height - LandscapePanel.height,
                    ImageIO.read(getClass().getResource("/assets/interface/grass.png")),
                    ImageIO.read(getClass().getResource("/assets/interface/sand.png")),
                    ImageIO.read(getClass().getResource("/assets/interface/gravel.png")),
                    ImageIO.read(getClass().getResource("/assets/interface/snow.png")),
                    ImageIO.read(getClass().getResource("/assets/interface/cobblestone.png"))));
            LOGGER.info("Landscape sub-menu is built");

            additional.put("objects_menu", new ObjectsPanel(0, height - ObjectsPanel.height,
                    ImageIO.read(getClass().getResource("/assets/interface/chest.png")),
                    ImageIO.read(getClass().getResource("/assets/interface/scout.png"))));
            LOGGER.info("Objects sub-menu is built");

            additional.put("algorithm_menu", new AlgorithmPanel(0, height - AlgorithmPanel.height,
                    ImageIO.read(getClass().getResource("/assets/interface/play.png")),
                    ImageIO.read(getClass().getResource("/assets/interface/forward.png")),
                    ImageIO.read(getClass().getResource("/assets/interface/back.png")),
                    ImageIO.read(getClass().getResource("/assets/interface/find_all.png")),
                    ImageIO.read(getClass().getResource("/assets/interface/clear.png"))));
            LOGGER.info("Algorithm sub-menu is built");

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "UI building error", e);
            throw new IOException("error during menu building", e);
        }

        LOGGER.info("UI initialized");
    }

    /**
     * Changes position of inner button panels
     * @param width     same as constructor
     * @param height    same as constructor
     */
    public void setSize(int width, int height) {

        LOGGER.info("UI resized to " + width + "x" + height);

        this.width = width;
        this.height = height;

        main.setPosition(width - MainPanel.width, 0);
        for(ButtonPanel panel: additional.values()) {
            panel.setPosition(0, height - panel.getHeight());
        }
    }

    /**
     * UI has several menus. There is a "landscape_menu", "file_menu",
     * "objects menu", "algorithm menu".
     * User should add Listener to sub-menu using id. Listener will be notified
     * when any button of sub-menu is pressed
     * @param subMenuName   ID of sub-menu
     * @param listener      Listener for sub-menu with this ID
     */
    public void addListener(String subMenuName, Listener listener) {
        LOGGER.info("Added new listener " + listener.getClass() + " for " + subMenuName);
        listeners.put(subMenuName, listener);
    }

    /**
     * update state of panels
     * @param ms    time from past state
     */
    public void update(int ms) {
        main.update(ms);
        for(ButtonPanel panel: additional.values()) {
            panel.update(ms);
        }
    }

    /**
     * Draw UI on AWT Graphics
     * @param g UI will be drawn from 0,0 position
     */
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        // save context
        Object old_antialias = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        Color old_color = g2d.getColor();

        //change context
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // draw cosmetics
        g2d.setColor(new Color(43, 42, 42, 255));
        g2d.fillRect(main.getX(), main.getY(), main.getWidth(), main.getHeight());

        g2d.setColor(new Color(60, 60, 60, 255));
        g2d.fillRect(main.getX()+5, main.getY()+5,
                main.getWidth()-10, main.getHeight()-10);


        main.draw(g2d);
        if(subMenu != null) {
            // draw cosmetics
            g2d.setColor(new Color(43, 42, 42, 255));
            g2d.fillRect(subMenu.getX(), subMenu.getY(), subMenu.getWidth(), subMenu.getHeight());

            g2d.setColor(new Color(60, 60, 60, 255));
            g2d.fillRect(subMenu.getX()+2, subMenu.getY()+2,
                    subMenu.getWidth()-4, subMenu.getHeight()-4);

            subMenu.draw(g2d);
        }

        // restore context
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, old_antialias);
    }

    /**
     * You can check is point (x, y) in main panel or
     * on the sub-menu panel if there it is
     * @param x
     * @param y
     * @return  return true if point in, else - false;
     */
    public boolean isPointIn(int x, int y) {
        if(main.isPointIn(x, y)) return true;
        if(subMenu != null) {
            if(subMenu.isPointIn(x, y)) return true;
        }
        return false;
    }


    /**
     * If (x, y) on main panel and some button pressed, then
     * there is sub-menu switch
     * If(x, y) on sub-menu panel and some button press, then
     * appropriate listener notified
     * @param x
     * @param y
     * @param e MouseEvent object. (You can't get keyCode if mouseClicked called)
     */
    public void press(int x, int y, MouseEvent e) {

        if(main.isPointIn(x, y)) {

            LOGGER.info("Pressed on Main panel");

            if(!SwingUtilities.isLeftMouseButton(e)) return;
            String subMenuID = main.press(x, y);

            if(subMenuID != null) {
                LOGGER.info("Called sub-menu is " + subMenuID);

                ButtonPanel subMenu = additional.getOrDefault(subMenuID, null);
                if(this.subMenu != null && this.subMenu != subMenu){
                    this.subMenu.drop();
                    try {
                        listeners.get(this.subMenuID).drop();
                    } catch (NullPointerException dummy) {}
                }
                this.subMenu = subMenu;
                this.subMenuID = subMenuID;
            } else {
                subMenuID = null;
            }
        }

        else if(subMenu != null) {
            if(subMenu.isPointIn(x, y)) {

                LOGGER.info("Pressed on subMenu " + subMenuID);

                String button = subMenu.press(x, y);
                if(button == null) return;

                LOGGER.info("Pressed button with name: " + button);

                boolean pressed = subMenu.isButtonPressed(button);
                try {
                    listeners.get(subMenuID).notify(button, pressed, e);
                } catch (NullPointerException exception) {
                    LOGGER.log(Level.WARNING, "Can't find appropriate listener for \"" + subMenuID + "\"", exception);
                }

                if(!SwingUtilities.isLeftMouseButton(e)) {
                    subMenu.release(button);
                }
            }
        }
    }

}
