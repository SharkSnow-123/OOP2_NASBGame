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

    static void showStartPage(JFrame frame) {
        GUI gui = new GUI();
        show(frame, gui.new TitlePanel(frame));
    }
}
