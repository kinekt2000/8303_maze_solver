import UI.UI;
import UI.dialog.Dialog;
import UI.dialog.DialogRaiser;

import render.Canvas;
import render.Drawable;
import resources.ResourceManager;
import tiles.TileType;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import java.io.IOException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Application extends JPanel implements MouseMotionListener, MouseListener, DialogRaiser {

    private int renderDelay = 30;
    private int logicDelay = 100;

    private int oldMouseX = 0;
    private int oldMouseY = 0;

    Timer renderTimer;
    Timer logicTimer;

    double time = System.currentTimeMillis();

    render.Canvas canvas;
    TileHighlighter highlighter;

    UI ui;
    Dialog dialog = null;

    FileListener fileListener;
    LandscapeListener landscapeListener;
    ObjectsListener objectsListener;

    Scout scout;
    List<Chest> chests;

    TileMap map;
    final int tileSize = 16;

    public Application() {
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        addMouseMotionListener(this);
        addMouseListener(this);
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                ui.setSize(getWidth(), getHeight());
                if(dialog != null) {
                    dialog.setPosition((getWidth() - dialog.getWidth())/2,
                            (getHeight() - dialog.getHeight())/2);
                }
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

        canvas = new Canvas();

        map = new TileMap(10, 10);

        try {
            ui = new UI(1024, 1024);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        fileListener = new FileListener(this);
        landscapeListener = new LandscapeListener(this);
        objectsListener = new ObjectsListener();

        ui.addListener("file_menu", fileListener);
        ui.addListener("landscape_menu", landscapeListener);
        ui.addListener("objects_menu", objectsListener);

        chests = new LinkedList<>();

        renderTimer.start();
        logicTimer.start();
    }


    public void paint(Graphics g){
        super.paint(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getSize().width, getSize().height);

        // compile a list of objects
        List<Drawable> objectsToDraw = new ArrayList<>(map.getRenderObjects());
        if(highlighter != null) objectsToDraw.add(highlighter);
        if(scout != null) objectsToDraw.add(scout);
        objectsToDraw.addAll(chests);

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

    public void mapResize(int width, int height) {
        map = new TileMap(width, height);
        scout = null;
        chests.clear();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e)) {
            int newMouseX = e.getX();
            int newMouseY = e.getY();

            int offsetX = newMouseX - oldMouseX;
            int offsetY = newMouseY - oldMouseY;

            canvas.getCamera().move(offsetX, offsetY);

            oldMouseX = newMouseX;
            oldMouseY = newMouseY;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // get true coordinates of pixel field
        double x = e.getX() / canvas.getCamera().getZoomFactor() - canvas.getCamera().getCameraOffsetX();
        double y = e.getY() / canvas.getCamera().getZoomFactor() - canvas.getCamera().getCameraOffsetY();

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
        if(ui.isPointIn(e.getX(), e.getY())) {
            ui.press(e.getX(), e.getY(), e);
            return;
        }

        double x = e.getX() / canvas.getCamera().getZoomFactor() - canvas.getCamera().getCameraOffsetX();
        double y = e.getY() / canvas.getCamera().getZoomFactor() - canvas.getCamera().getCameraOffsetY();

        int mappedX = (int)(x/tileSize);
        int mappedY = (int)(y/tileSize);

        TileType brush = landscapeListener.getBrush();
        if(brush != null) {
            System.out.println("set type of tile (" + mappedX + "; " + mappedY + ") in " + brush.toString());
        }

        if(objectsListener.isSetScout()){
            if(SwingUtilities.isLeftMouseButton(e)) {
                if (scout == null) {
                    scout = new Scout(ResourceManager.getTexture("scout"), mappedX, mappedY, tileSize);
                } else {
                    System.out.println("There can be only ONE!");
                }
            } else if (SwingUtilities.isRightMouseButton(e)) {
                if(scout != null) {
                    if(scout.getX() == mappedX && scout.getY() == mappedY) {
                        scout = null;
                    }
                }
            }
        }

        if(objectsListener.isSetChest()) {
            int i = 0;
            for(Chest chest: chests) {
                if(chest.getX() == mappedX && chest.getY() == mappedY)
                    break;
                i++;
            }

            if(SwingUtilities.isLeftMouseButton(e)) {
                if(i == chests.size()) {
                    chests.add(new Chest(ResourceManager.getTexture("chest"), mappedX, mappedY, tileSize));
                } else {
                    System.out.println("Chest already placed");
                }
            } else if (SwingUtilities.isRightMouseButton(e)) {
                if(i != chests.size()) {
                    chests.remove(i);
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        oldMouseX = e.getX();
        oldMouseY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        oldMouseX = e.getX();
        oldMouseY = e.getY();
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
        this.dialog = dialog;

        removeMouseListener(this);
        removeMouseMotionListener(this);
        addMouseListener(dialog);

        addKeyListener(dialog);

        dialog.setPosition(getWidth()/2 - dialog.getHeight()/2, getHeight()/2 - dialog.getHeight()/2);
    }

    @Override
    public void dropDialog() {
        removeMouseListener(dialog);
        removeKeyListener(dialog);

        addMouseListener(this);
        addMouseMotionListener(this);

        if(dialog.isAccepted()) checkDialogOut(dialog);

        dialog = null;
    }

    private void checkDialogOut(Dialog dialog) {
        if(dialog.getName().equals("resize")) {
            ResizeDialog rDialog = (ResizeDialog) dialog;
            mapResize(rDialog.getMapWidth(), rDialog.getMapHeight());
        }
    }


    public static void main(String [] args) throws InterruptedException {
        initResources();

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
