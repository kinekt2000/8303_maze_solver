package UI.dialog;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public interface Dialog extends MouseListener, KeyListener {

    void setPosition(int x, int y);

    int getX();
    int getY();
    int getWidth();
    int getHeight();
    String getName();

    boolean isAccepted();
    boolean isCanceled();

    void draw(Graphics g);
    boolean closed();

    @Override
    default void keyTyped(KeyEvent e) { }

    @Override
    default void keyPressed(KeyEvent e) { }

    @Override
    default void keyReleased(KeyEvent e) { }

    @Override
    default void mouseClicked(MouseEvent e) { }

    @Override
    default void mousePressed(MouseEvent e) { }

    @Override
    default void mouseReleased(MouseEvent e) { }

    @Override
    default void mouseEntered(MouseEvent e) { }

    @Override
    default void mouseExited(MouseEvent e) { }
}
