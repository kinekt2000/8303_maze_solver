package render;

import javax.imageio.ImageIO;
import java.awt.*;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Canvas {
    private BufferedImage img;

    Camera camera;

    public Canvas() {
        camera = new Camera();

        try {
            img = ImageIO.read(new File("assets/gravel.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics renderPlace, List<Drawable> objects) {
        if(objects == null) {
            return;
        }

        Graphics2D graph2D = (Graphics2D) renderPlace;

        AffineTransform oldTransform = graph2D.getTransform();
        AffineTransform newTransform = new AffineTransform();

        newTransform.scale(camera.getZoomFactor(), camera.getZoomFactor());
        newTransform.translate(camera.getCameraOffsetX(), camera.getCameraOffsetY());

        graph2D.setTransform(newTransform);


        // render all objects
        for(Drawable obj: objects) {
            if(obj != null) obj.draw(graph2D);
        }

        graph2D.setTransform(oldTransform);
    }



    public Camera getCamera() {
        return camera;
    }
}
