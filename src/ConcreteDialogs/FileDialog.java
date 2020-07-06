package ConcreteDialogs;

import UI.dialog.Dialog;
import UI.dialog.DialogException;
import UI.Line;

import javax.imageio.ImageIO;
import java.awt.*;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

abstract class FileDialog implements Dialog {
    private class Triangle {
        int height;
        int x = 0;
        int y = 0;

        private double x1, y1;
        private double x2, y2;
        private double x3, y3;

        boolean blocked = true;

        Triangle(int height) {
            this.height = height;
            double side = (int)((2 * height) / Math.sqrt(3));

            x1 = 0;
            y1 = -(double)height * 2/3;

            x2 = side / 2;
            y2 = (double)height / 3;

            x3 =  -side / 2;
            y3 = (double)height / 3;
        }

        void move(int dx, int dy) { // set center
            x1 += dx;
            x2 += dx;
            x3 += dx;

            y1 += dy;
            y2 += dy;
            y3 += dy;

            x += dx;
            y += dy;
        }

        Triangle reverse() {
            double side = (int)((2 * height) / Math.sqrt(3));

            x1 = x;
            y1 = (double)height * 2/3;

            x2 = side / 2;
            y2 = -(double)height / 3;

            x3 =  -side / 2;
            y3 = -(double)height / 3;

            return this;
        }

        void lock() {
            blocked = true;
        }

        void unlock() {
            blocked = false;
        }

        void draw(Graphics g) {
            g.fillPolygon(new int[]{(int)x1, (int)x2, (int)x3},
                          new int[]{(int)y1, (int)y2, (int)y3},
                          3);
        }

        boolean isPointIn(int x, int y) {
            double def1 = (x1 - x) * (y2 - y1) - (x2 - x1) * (y1 - y);
            double def2 = (x2 - x) * (y3 - y2) - (x3 - x2) * (y2 - y);
            double def3 = (x3 - x) * (y1 - y3) - (x1 - x3) * (y3 - y);

            return ((def1 >= 0 && def2 >= 0 && def3 >= 0)
                    || (def1 <= 0 && def2 <= 0 && def3 <= 0));
        }


    }
    final static public int width = 200;

    //scroll buttons constants
    final static private int SCROLL_HEIGHT = 15;

    // lines constants
    final static private int VISIBLE_LINES = 4;
    final static private int LINE_HEIGHT = 20;
    final static private int LINE_PADDING = 5;
    final static private int DISTANCE = 10; // distance between files field and chosen

    //lines field constants
    final static private int LINES_FIELD_X = 10;
    final static private int LINES_FIELD_Y = 10 + SCROLL_HEIGHT + LINE_PADDING;
    final static private int LINES_FIELD_WIDTH = width - LINES_FIELD_X*2;
    final static private int LINES_FIELD_HEIGHT = LINE_PADDING + VISIBLE_LINES * (LINE_HEIGHT + LINE_PADDING);

    final static public int height = LINES_FIELD_Y
            + LINES_FIELD_HEIGHT
            + LINE_PADDING // distance between lines field and scroll button;
            + SCROLL_HEIGHT // size of scroll button
            + DISTANCE // distance between scroll button and chosen line
            + LINE_HEIGHT // size of chosen line
            + 35; // space for exit buttons;

    private int x;
    private int y;

    protected UI.Button accept;
    protected UI.Button cancel;

    protected Triangle up;
    protected Triangle down;

    protected List<Line> files;
    protected Line chosen;
    private int lineOffset = 0;

    boolean close = false;
    boolean accepted = false;
    boolean canceled = false;

    protected FileDialog() {
        files = new ArrayList<>();

        // filter for save files searching
        FilenameFilter extensionFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                int dotIndex = name.lastIndexOf('.');
                if(dotIndex > 0) {
                    String extension = name.substring(dotIndex);
                    if(extension.equals(".pfsv")) {
                        if(name.length() - 5 <= 10)
                            return true;
                    }
                }
                return false;
            }
        };

        // save files searching and building lines list
        try {
            File savesFolder = new File("saves");

            boolean fileExists = savesFolder.exists();

            if(fileExists && savesFolder.isFile()) {
                throw new DialogException("There is file \"saves\" instead folder. Rid of the file.");
            }

            if(!fileExists) {
                try {
                    savesFolder.mkdir();
                } catch (SecurityException exception) {
                    // weird.
                    // try to create directory of program,
                    // there can't be security error
                    System.out.println("load/save function not available");
                    System.out.println(exception.getMessage());
                }
            }

            int i = 0;
            File [] saves = savesFolder.listFiles(extensionFilter);
            Arrays.sort(saves);
            for (File file : saves) {
                Line fileLine = new Line(LINES_FIELD_X + LINE_PADDING,
                        LINES_FIELD_Y + LINE_PADDING + i * (LINE_HEIGHT + LINE_PADDING),
                        LINES_FIELD_WIDTH - LINE_PADDING * 2,
                        LINE_HEIGHT);

                String fileName = file.getName();
                fileName = fileName.substring(0, fileName.length()-5);
                fileLine.setLine(fileName);
                files.add(fileLine);
                i++;
            }
        } catch (SecurityException exception) {
            // weird.
            // try to read directory of program,
            // there can't be security error
            System.out.println(exception.getMessage());
        }

        // init scroll buttons. Which are just triangle shapes;
        up = new Triangle(SCROLL_HEIGHT);
        up.move(width/2, LINES_FIELD_Y - LINE_PADDING - SCROLL_HEIGHT/3);

        down = new Triangle(SCROLL_HEIGHT).reverse();
        down.move(width/2, LINES_FIELD_Y + LINES_FIELD_HEIGHT + LINE_PADDING + SCROLL_HEIGHT/3);

        // init exit buttons
        BufferedImage acceptTexture = null;
        try{
            acceptTexture = ImageIO.read(new File("assets/interface/accept.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        accept = new UI.Button("accept", acceptTexture, width - 15, height - 15, 10);

        BufferedImage cancelTexture = null;
        try{
            cancelTexture = ImageIO.read(new File("assets/interface/cancel.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        cancel = new UI.Button("cancel", cancelTexture, width - 45, height - 15, 10);

        // init chosen line
        chosen = new Line(LINES_FIELD_X + LINE_PADDING,
                LINES_FIELD_Y + LINES_FIELD_HEIGHT + LINE_PADDING + SCROLL_HEIGHT + DISTANCE,
                LINES_FIELD_WIDTH - LINE_PADDING * 2,
                LINE_HEIGHT);
        chosen.setLine("");
    }

    @Override
    public void setPosition(int x, int y) {
        int dx = x - this.x;
        int dy = y - this.y;

        accept.move(dx, dy);
        cancel.move(dx, dy);

        for(Line line: files) {
            line.move(dx, dy);
        }

        chosen.move(dx, dy);

        up.move(dx, dy);
        down.move(dx, dy);

        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public boolean isAccepted() {
        return accepted;
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public boolean closed() {
        return close;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if(accept.isPointIn(x, y)) {
            accepted = true;
            close = true;
            return;
        }

        if(cancel.isPointIn(x, y)) {
            canceled = true;
            close = true;
            return;
        }

        if(up.isPointIn(x, y)) {
            if(lineOffset > 0) {
                lineOffset--;
                for(Line line: files) {
                    line.move(0, +(LINE_PADDING + LINE_HEIGHT));
                }
            }
            return;
        }

        if(down.isPointIn(x, y)) {
            if(lineOffset < files.size() - VISIBLE_LINES) {
                lineOffset++;
                for(Line line: files) {
                    line.move(0, -(LINE_PADDING + LINE_HEIGHT));
                }
            }
            return;
        }

        Line clicked = null;

        int subListEnd = (lineOffset + VISIBLE_LINES > files.size()) ? files.size() : lineOffset + VISIBLE_LINES;
        for(Line line : files.subList(lineOffset, subListEnd)) {
            if(line.isPointIn(x, y)) {
                line.setBackColor(Color.LIGHT_GRAY);
                clicked = line;
                chosen.setLine(clicked.getLine());
                chosen.setBackColor(Color.WHITE);
                break;
            }
        }

        if(clicked != null) {
            for(Line line: files) {
                if(line != clicked) {
                    line.setBackColor(Color.WHITE);
                }
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(43, 42, 42, 255));
        g.fillRect(x, y, width, height);

        g.setColor(new Color(60, 60, 60, 255));
        g.fillRect(x + LINES_FIELD_X, y + LINES_FIELD_Y, LINES_FIELD_WIDTH, LINES_FIELD_HEIGHT);

        if(!up.blocked) {
            g.setColor(Color.LIGHT_GRAY);
        }
        up.draw(g);

        if(down.blocked) {
            g.setColor(new Color(60, 60, 60, 255));
        } else {
            g.setColor(Color.LIGHT_GRAY);
        }
        down.draw(g);

        int subListEnd = (lineOffset + VISIBLE_LINES > files.size()) ? files.size() : lineOffset + VISIBLE_LINES;
        for(Line line: files.subList(lineOffset, subListEnd)) {
            line.draw(g);
        }

        chosen.draw(g);

        accept.draw(g);
        cancel.draw(g);
    }
}
