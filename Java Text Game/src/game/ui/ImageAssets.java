package game.ui;

import javax.swing.ImageIcon;
import java.io.File;
import java.net.URL;

final class ImageAssets {
    private ImageAssets() {
    }

    static ImageIcon load(String fileName) {
        URL resource = ImageAssets.class.getResource("/images/" + fileName);
        if (resource != null) {
            return new ImageIcon(resource);
        }

        File imageFile = new File("images", fileName);
        return new ImageIcon(imageFile.getPath());
    }
}
