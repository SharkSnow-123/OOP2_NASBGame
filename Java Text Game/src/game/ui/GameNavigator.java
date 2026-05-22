package game.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;

final class GameNavigator {
    // Private constructor para utility class ra ni siya.
    private GameNavigator() {
    }

    // Kini nga function ilisdan ang current screen sa bag-ong panel.
    static void show(JFrame frame, JPanel panel) {
        frame.setContentPane(panel);
        frame.revalidate();
        frame.repaint();
        panel.requestFocusInWindow();
    }

    // Kini nga function mobalik sa character selection screen.
    static void showCharacterSelection(JFrame frame) {
        show(frame, new BattlePanel(frame));
    }

    // Kini nga function mobalik sa pinaka-una nga title/start page.
    static void showStartPage(JFrame frame) {
        GUI gui = new GUI();
        show(frame, gui.new TitlePanel(frame));
    }
}
