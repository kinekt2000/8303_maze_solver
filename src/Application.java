import ConcreteListeners.AlgorithmListener;
import ConcreteListeners.FileListener;
import ConcreteListeners.LandscapeListener;
import ConcreteListeners.ObjectsListener;

import UI.UI;
import UI.dialog.Dialog;
import UI.dialog.DialogRaiser;

import DrawableModel.TileHighlighter;
import DrawableModel.TileMap;

import render.Canvas;
import render.Drawable;

import resources.ResourceManager;

import logic.TileType;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/*
 * Application class
 * Catches system events and distributes then between modules.
 * Starts timer for update and rerender
 *
 * Logs state into default logger
 */

public class Application extends JPanel implements MouseMotionListener, MouseListener, DialogRaiser {

    static Logger LOGGER = Logger.getLogger(Application.class.getName());


    private int renderDelay = 30;
    private int logicDelay = 100;

    private int oldMouseX = 0;
    private int oldMouseY = 0;
    private double scale = (double) java.awt.Toolkit.getDefaultToolkit().getScreenResolution() / 96;

    Timer renderTimer;
    Timer logicTimer;

    double time = System.currentTimeMillis();

    BufferedImage background;       // gradient image behind canvas
    render.Canvas canvas;
    TileHighlighter highlighter;    // frame around tile

    UI ui;
    Dialog dialog = null;

    FileListener fileListener;
    LandscapeListener landscapeListener;
    ObjectsListener objectsListener;
    AlgorithmListener algorithmListener;

    TileMap map;
    final int tileSize = 16;

    public Application() {
        LOGGER.info("Application initiating");

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        addMouseMotionListener(this);
        addMouseListener(this);
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                LOGGER.info("Application size set to " + getWidth() + "x" + getHeight());

                ui.setSize(getWidth(), getHeight());
                LOGGER.info("UI resized");

                if(dialog != null) {
                    dialog.setPosition((getWidth() - dialog.getWidth())/2,
                            (getHeight() - dialog.getHeight())/2);
                    LOGGER.info("Dialog \"" + dialog.getName() + "\" moved to center");
                }

                background = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = (Graphics2D) background.getGraphics();
                RadialGradientPaint gradient= new RadialGradientPaint(background.getWidth()/2, background.getHeight()/2,
                        Math.max(background.getWidth()/2, background.getHeight()/2),
                        new float[] {0.0f, 1f}, new Color[]{Color.DARK_GRAY, Color.LIGHT_GRAY});
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, background.getWidth(), background.getHeight());
                g2d.dispose();
                LOGGER.info("background image is repainted");
            }

            @Override
            public void componentMoved(ComponentEvent e) { }
            @Override
            public void componentShown(ComponentEvent e) { }
            @Override
            public void componentHidden(ComponentEvent e) { }
        });

        renderTimer = new Timer(renderDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
                renderTimer.start();
            }
        });

        logicTimer = new Timer(logicDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                double delta = System.currentTimeMillis() - time;
                time += delta;

                update((int) delta);
                logicTimer.start();
            }
        });

        LOGGER.info("Create main components of application");
        canvas = new Canvas();
        LOGGER.info("Canvas is created");

        background = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) background.getGraphics();
        RadialGradientPaint gradient= new RadialGradientPaint(background.getWidth()/2, background.getHeight()/2,1,
                new float[] {0.0f, 0.5f}, new Color[]{Color.DARK_GRAY, Color.WHITE});
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, background.getWidth(), background.getHeight());
        g2d.dispose();
        LOGGER.info("Background image is drawn");

        map = new TileMap(10, 10, 16);
        LOGGER.info("Map is created");

        try {
            ui = new UI(1024, 1024);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        LOGGER.info("UI is built");

        LOGGER.info("Create UI listeners");
        fileListener = new FileListener(this, map);
        LOGGER.info("File menu listener is created");

        landscapeListener = new LandscapeListener(this);
        LOGGER.info("Landscape menu listener is created");

        objectsListener = new ObjectsListener();
        LOGGER.info("Objects menu listener is created");

        algorithmListener = new AlgorithmListener(map);
        LOGGER.info("Algorithm neu listener created");

        ui.addListener("file_menu", fileListener);
        ui.addListener("landscape_menu", landscapeListener);
        ui.addListener("objects_menu", objectsListener);
        ui.addListener("algorithm_menu", algorithmListener);

        renderTimer.start();
        logicTimer.start();

        LOGGER.info("Application initiated");
    }


    public void paint(Graphics g){
        super.paint(g);
        g.drawImage(background, 0, 0, null);

        // compile a list of objects
        List<Drawable> objectsToDraw = new ArrayList<>();
        objectsToDraw.add(map);
        if(highlighter != null) objectsToDraw.add(highlighter);

        // draw on canvas
        canvas.draw(g, objectsToDraw);

        // draw ui
        ui.draw(g);

        if(dialog != null) dialog.draw(g);

        g.dispose();
    }


    public void update(int ms) {
        ui.update(ms);
        if(dialog != null) {
            if(dialog.closed()) dropDialog();
        }
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        LOGGER.fine("mouse dragged to position (" + e.getX() * scale + "; " + e.getY() * scale + ")");

        if(SwingUtilities.isLeftMouseButton(e)) {
            int newMouseX = (int) (e.getX() * scale);
            int newMouseY = (int) (e.getY() * scale);

            int offsetX = newMouseX - oldMouseX;
            int offsetY = newMouseY - oldMouseY;

            canvas.getCamera().move(offsetX, offsetY);

            oldMouseX = newMouseX;
            oldMouseY = newMouseY;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        LOGGER.fine("mouse moved to position (" + e.getX() * scale + "; " + e.getY() * scale + ")");

        // get true coordinates of pixel field
        double x = e.getX() * scale / canvas.getCamera().getZoomFactor() - canvas.getCamera().getCameraOffsetX();
        double y = e.getY() * scale / canvas.getCamera().getZoomFactor() - canvas.getCamera().getCameraOffsetY();

        if(x >= 0 && x < (map.getWidth() * tileSize)
        && y >= 0 && y < (map.getHeight() * tileSize)) {
            int mappedX = (int)(x/tileSize);
            int mappedY = (int)(y/tileSize);

            if(highlighter == null) {
                highlighter = new TileHighlighter(mappedX, mappedY, tileSize);
            } else {
                highlighter.setPos(mappedX, mappedY);
            }
        } else {
            highlighter = null;
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        LOGGER.info("mouse clicked on position (" + e.getX() + "; " + e.getY() + ")");

        if(ui.isPointIn(e.getX(), e.getY())) {
            LOGGER.info("click on UI");
            ui.press(e.getX(), e.getY(), e);
            return;
        }

        LOGGER.info("click on canvas");

        double x = e.getX() * scale / canvas.getCamera().getZoomFactor() - canvas.getCamera().getCameraOffsetX();
        double y = e.getY() * scale / canvas.getCamera().getZoomFactor() - canvas.getCamera().getCameraOffsetY();

        int mappedX = (int)(x/tileSize);
        int mappedY = (int)(y/tileSize);

        LOGGER.info("point on field is (" + mappedX + "; " + mappedY + ")");

        TileType brush = landscapeListener.getBrush();
        if(brush != null) {
            map.setCell(mappedX, mappedY, brush);
        }

        if(SwingUtilities.isLeftMouseButton(e)) {
            objectsListener.setEntity(map, mappedX, mappedY);
        } else if (SwingUtilities.isRightMouseButton(e)) {
            objectsListener.removeEntity(map, mappedX, mappedY);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        oldMouseX = (int) (e.getX() * scale);
        oldMouseY = (int) (e.getY() * scale);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        oldMouseX = (int) (e.getX() * scale);
        oldMouseY = (int) (e.getY() * scale);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
        highlighter = null;
    }

    @Override
    public void raiseDialog(Dialog dialog) {
        if(dialog == null) return;

        LOGGER.info("Raised \"" + dialog.getName() + "\"");

        this.dialog = dialog;

        removeMouseListener(this);
        removeMouseMotionListener(this);
        addMouseListener(dialog);

        addKeyListener(dialog);

        dialog.setPosition(getWidth()/2 - dialog.getWidth()/2, getHeight()/2 - dialog.getHeight()/2);
    }

    @Override
    public void dropDialog() {
        removeMouseListener(dialog);
        removeKeyListener(dialog);

        addMouseListener(this);
        addMouseMotionListener(this);

        LOGGER.info("Dropped \"" + dialog.getName() + "\"");
        dialog = null;
    }


    public static void main(String [] args) throws InterruptedException {
        initResources();
        Application.LOGGER.setLevel(Level.INFO);


        // define and initiate window
        JFrame window = new JFrame();
        Application field = new Application();

        window.setSize(700, 700);
        window.setLocationRelativeTo(null);

        window.setTitle("Path finder");
        window.setResizable(true);
        window.setMinimumSize(new Dimension(700, 700));

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(field);

        window.setVisible(true);
    }


    /*
     * Initializes all field used textures
     */

    private static void initResources() {
        // landscape
        ResourceManager.loadTexture("grass", "assets/grass.png");
        ResourceManager.loadTexture("sand", "assets/sand.png");
        ResourceManager.loadTexture("snow", "assets/snow.png");
        ResourceManager.loadTexture("gravel", "assets/gravel.png");
        ResourceManager.loadTexture("cobblestone", "assets/cobblestone.png");

        // objects
        ResourceManager.loadTexture("scout", "assets/objects/scout.png");
        ResourceManager.loadTexture("chest", "assets/objects/chest.png");
    }

}
