package game.ui;

import javax.swing.ImageIcon;
import java.io.File;
import java.net.URL;
import java.util.List;

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

        for (File imageFile : possibleImageFiles(fileName)) {
            if (imageFile.exists()) {
                return new ImageIcon(imageFile.getAbsolutePath());
            }
        }

        System.err.println("Image not found: " + fileName);
        return new ImageIcon();
    }

    // Kini nga function mangita sa possible paths kay lahi-lahi ug working directory ang VS Code.
    private static List<File> possibleImageFiles(String fileName) {
        return List.of(
                new File("images", fileName),
                new File("Java Text Game/images", fileName),
                new File(System.getProperty("user.dir"), "images/" + fileName),
                new File(System.getProperty("user.dir"), "Java Text Game/images/" + fileName)
        );
    }
}
