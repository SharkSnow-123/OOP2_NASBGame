package game.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;

final class GameNavigator {
    private GameNavigator() {
    }

    static void show(JFrame frame, JPanel panel) {
        frame.setContentPane(panel);
        frame.revalidate();
        frame.repaint();
        panel.requestFocusInWindow();
    }

    static void showCharacterSelection(JFrame frame) {
        show(frame, new BattlePanel(frame));
    }
}
