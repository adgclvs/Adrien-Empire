package adrien.game;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;

public class ImageCache {
    private static final Map<String, Image> cache = new HashMap<>();

    public static Image getImage(String path) {
        if (cache.containsKey(path)) {
            return cache.get(path);
        }
        try {
            Image image = new Image(ImageCache.class.getResourceAsStream(path));
            cache.put(path, image);
            return image;
        } catch (Exception e) {
            System.err.println("Error loading image: " + path);
            return null;
        }
    }
}
