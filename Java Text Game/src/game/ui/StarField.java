package game.ui;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

final class StarField {
    private static final int DEFAULT_WIDTH = 900;
    private static final int DEFAULT_HEIGHT = 600;
    private static final int STAR_SIZE = 2;

    private final Random random = new Random();
    private final List<Point> stars = new ArrayList<>();
    private final List<Integer> speeds = new ArrayList<>();

    StarField(int count) {
        for (int i = 0; i < count; i++) {
            stars.add(new Point(random.nextInt(DEFAULT_WIDTH), random.nextInt(DEFAULT_HEIGHT)));
            speeds.add(1 + random.nextInt(3));
        }
    }

    void update(int width, int height) {
        int safeWidth = Math.max(1, width);
        int safeHeight = Math.max(1, height);

        for (int i = 0; i < stars.size(); i++) {
            Point star = stars.get(i);
            star.y += speeds.get(i);

            if (star.y > safeHeight) {
                star.y = 0;
                star.x = random.nextInt(safeWidth);
            }
        }
    }

    void paint(Graphics2D graphics) {
        for (Point star : stars) {
            graphics.fillRect(star.x, star.y, STAR_SIZE, STAR_SIZE);
        }
    }
}
