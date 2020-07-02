package UI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UI{

    int width;
    int height;

    MainPanel main;
    Map<String, ButtonPanel> additional;
    Map<String, Listener> listeners;

    String subMenuID;
    ButtonPanel subMenu;

    public UI(int width, int height) throws IOException {
        this.width = width;
        this.height = height;

        additional = new HashMap<>();
        listeners = new HashMap<>();
        ButtonPanel current;

        try {
            main = new MainPanel(width - MainPanel.width, 0,
                    ImageIO.read(new File("assets/interface/brush.png")),
                    ImageIO.read(new File("assets/interface/object.png")),
                    ImageIO.read(new File("assets/interface/gear.png")));

            additional.put("landscape_menu", new LandscapePanel(0, height - LandscapePanel.height,
                    ImageIO.read(new File("assets/interface/resize.png")),
                    ImageIO.read(new File("assets/interface/grass.png")),
                    ImageIO.read(new File("assets/interface/sand.png")),
                    ImageIO.read(new File("assets/interface/gravel.png")),
                    ImageIO.read(new File("assets/interface/snow.png")),
                    ImageIO.read(new File("assets/interface/cobblestone.png"))));

            additional.put("objects_menu", new ObjectsPanel(0, height - ObjectsPanel.height,
                    ImageIO.read(new File("assets/interface/chest.png")),
                    ImageIO.read(new File("assets/interface/scout.png"))));

            additional.put("algorithm_menu", new AlgorithmPanel(0, height - AlgorithmPanel.height));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new IOException("error during menu building", e);
        }
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;

        main.setPosition(width - MainPanel.width, 0);
        for(ButtonPanel panel: additional.values()) {
            panel.setPosition(0, height - panel.getHeight());
        }
    }

    public void addListener(String subMenuName, Listener listener) {
        listeners.put(subMenuName, listener);
    }

    public void update(int ms) {
        main.update(ms);
        for(ButtonPanel panel: additional.values()) {
            panel.update(ms);
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        // save context
        Object old_antialias = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);

        //change context
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        main.draw(g2d);
        if(subMenu != null) subMenu.draw(g2d);

        // restore context
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, old_antialias);
    }

    public boolean isPointIn(int x, int y) {
        if(main.isPointIn(x, y)) return true;
        if(subMenu != null) {
            if(subMenu.isPointIn(x, y)) return true;
        }
        return false;
    }

    public void press(int x, int y, MouseEvent e) {

        if(main.isPointIn(x, y)) {
            if(!SwingUtilities.isLeftMouseButton(e)) return;
            String subMenuID = main.press(x, y);

            if(subMenuID != null) {
                ButtonPanel subMenu = additional.getOrDefault(subMenuID, null);
                if(this.subMenu != null && this.subMenu != subMenu){
                    this.subMenu.drop();
                    listeners.get(subMenuID).drop();
                }
                this.subMenu = subMenu;
                this.subMenuID = subMenuID;
            } else {
                subMenuID = null;
            }
        }

        else if(subMenu != null) {
            if(subMenu.isPointIn(x, y)) {
                String button = subMenu.press(x, y);
                if(button == null) return;

                System.out.println(subMenuID);

                boolean pressed = subMenu.isButtonPressed(button);
                try {
                    listeners.get(subMenuID).notify(button, pressed, e);
                } catch (NullPointerException exception) {
                    System.out.println("Desync between menu name and listener name");
                    exception.printStackTrace();
                }

                if(!SwingUtilities.isLeftMouseButton(e)) {
                    subMenu.release(button);
                }
            }
        }
    }

}
