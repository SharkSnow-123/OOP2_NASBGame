package game.ui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class RoundThreePanel extends JPanel implements ActionListener {

    private JFrame frame;
    private Timer starTimer;
    private final java.util.List<Point> stars = new java.util.ArrayList<>();
    private final java.util.List<Integer> starSpeeds = new java.util.ArrayList<>();

    private JButton proceedButton;
    private JButton mainMenuButton;

    public  RoundThreePanel (JFrame frame) {
        this.frame = frame;
        setBackground(Color.BLACK);
        setLayout(null);

        generateStars(150);

        // Create buttons with Game Over panel style
        proceedButton = createButton("PROCEED ROUND 3");
        proceedButton.addActionListener(e -> {
            RoundThreeFight nextRound = new RoundThreeFight(frame);
            frame.setContentPane(nextRound);
            frame.revalidate();
            frame.repaint();
        });

        mainMenuButton = createButton("MAIN MENU");
        mainMenuButton.addActionListener(e -> {
            starTimer.stop();
            GameNavigator.showStartPage(frame);
        });

        add(proceedButton);
        add(mainMenuButton);

        starTimer = new Timer(50, this);
        starTimer.start();

        // Listen for resize event to reposition buttons
        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                repositionButtons();
            }
        });

        // Initial position of buttons
        SwingUtilities.invokeLater(this::repositionButtons);
    }

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

    private void generateStars(int count) {
        Random rand = new Random();
        int w = frame.getWidth() > 0 ? frame.getWidth() : 900;
        int h = frame.getHeight() > 0 ? frame.getHeight() : 600;

        for (int i = 0; i < count; i++) {
            stars.add(new Point(rand.nextInt(w), rand.nextInt(h)));
            starSpeeds.add(1 + rand.nextInt(3));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);

        // Draw stars
        for (Point star : stars) {
            g.fillRect(star.x, star.y, 2, 2);
        }

        // Draw message text
        g.setFont(new Font("Monospaced", Font.BOLD, 40));
        String text = "YOU DEFEATED THE MIDTERM MONSTER!";
        FontMetrics fm = g.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(text)) / 2;
        int y = getHeight() / 2 - 50;
        g.drawString(text, x, y);

        // Position buttons on repaint
        repositionButtons();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
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

    private void repositionButtons() {
        int w = getWidth();
        int h = getHeight();
        int buttonWidth = 300;
        int buttonHeight = 60;
        int spacing = 30;

        int totalHeight = buttonHeight * 2 + spacing;
        int startY = h / 2 + 10;

        int x = (w - buttonWidth) / 2;

        proceedButton.setLocation(x, startY);
        mainMenuButton.setLocation(x, startY + buttonHeight + spacing);
    }
}
