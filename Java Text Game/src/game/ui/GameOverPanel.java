package game.ui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GameOverPanel extends JPanel implements ActionListener {

    private JFrame frame;
    private Timer starTimer;
    private final java.util.List<Point> stars = new java.util.ArrayList<>();
    private final java.util.List<Integer> starSpeeds = new java.util.ArrayList<>();
    private JButton mainMenuButton;
    private JButton playAgainButton;

    // Kini nga constructor mag-andam sa game over screen.
    public GameOverPanel(JFrame frame) {
        this.frame = frame;
        setBackground(Color.BLACK);
        setLayout(null);

        playAgainButton = createButton("PLAY AGAIN");
        add(playAgainButton);
        playAgainButton.addActionListener(e -> playAgain());

        mainMenuButton = createButton("MAIN MENU");
        add(mainMenuButton);
        mainMenuButton.addActionListener(e -> {
            starTimer.stop();
            GameNavigator.showStartPage(frame);
        });

        generateStars(150);

        // Timer for star twinkling effect
        starTimer = new Timer(50, this);
        starTimer.start();
    }

    // Kini nga function mohimo ug styled button para sa game over screen.
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Monospaced", Font.BOLD, 30));
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        button.setSize(300, 60);
        return button;
    }

    // Kini nga function magsugod balik sa character selection.
    private void playAgain() {
        // Restart game by going back to BattlePanel (or wherever your game starts)
        BattlePanel battlePanel = new BattlePanel(frame);
        frame.setContentPane(battlePanel);
        frame.revalidate();
        frame.repaint();
        battlePanel.requestFocusInWindow();
    }

    // Kini nga function mohimo sa moving stars sa background.
    private void generateStars(int count) {
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            stars.add(new Point(rand.nextInt(900), rand.nextInt(600)));
            starSpeeds.add(1 + rand.nextInt(3));
        }
    }

    @Override
    // Kini nga function modrawing sa game over text, stars, ug buttons.
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);

        // Draw stars
        for (Point star : stars) {
            g.fillRect(star.x, star.y, 2, 2);
        }

        // Draw "GAME OVER" text centered
        g.setFont(new Font("Monospaced", Font.BOLD, 70));
        FontMetrics fm = g.getFontMetrics();
        String text = "GAME OVER";
        int x = (getWidth() - fm.stringWidth(text)) / 2;
        int y = getHeight() / 2 - 50;
        g.drawString(text, x, y);

        positionButtons();
    }

    // Kini nga function mobutang sa buttons sa tunga sa screen.
    private void positionButtons() {
        int w = getWidth();
        int h = getHeight();
        int buttonWidth = 300;
        int buttonHeight = 60;
        int x = (w - buttonWidth) / 2;
        playAgainButton.setLocation(x, h / 2 + 50);
        mainMenuButton.setLocation(x, h / 2 + 130);
    }

    @Override
    // Kini nga function mopalihok sa stars kada timer tick.
    public void actionPerformed(ActionEvent e) {
        // Move stars down for twinkling effect
        Random rand = new Random();
        for (int i = 0; i < stars.size(); i++) {
            Point star = stars.get(i);
            star.y += starSpeeds.get(i);
            if (star.y > getHeight()) {
                star.y = 0;
                star.x = rand.nextInt(getWidth());
            }
        }
        repaint();
    }
}
