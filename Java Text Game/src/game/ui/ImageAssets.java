package game.ui;

import javax.swing.ImageIcon;
import java.io.File;
import java.net.URL;

final class ImageAssets {
    // Private constructor para dili mahimo nga object ang helper class.
    private ImageAssets() {
    }

    // Kini nga function mangita ug moload sa image gikan sa resources o images folder.
    static ImageIcon load(String fileName) {
        URL resource = ImageAssets.class.getResource("/images/" + fileName);
        if (resource != null) {
            return new ImageIcon(resource);
        }

        File imageFile = new File("images", fileName);
        return new ImageIcon(imageFile.getPath());
    }
}
