package rm;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResourceManager {

    static private ResourceManager instance;

    private Map<String, BufferedImage> table;

    private ResourceManager() {
        table = new HashMap<>();
    }


    static public BufferedImage getTexture(String name) {
        if(instance == null || name == null) {
            return null;
        }

        return instance.table.getOrDefault(name, null);
    }

    static public void loadTexture(String name, String local_path) {
        if(name == null || local_path == null) {
            return;
        }

        if(instance == null) {
            instance = new ResourceManager();
        }

        BufferedImage img;
        try {
            img = ImageIO.read(ResourceManager.class.getResource(File.separator + local_path));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        instance.table.put(name, img);
    }
}
