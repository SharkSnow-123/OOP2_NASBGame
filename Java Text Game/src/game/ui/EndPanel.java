package game.ui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class EndPanel extends JPanel implements ActionListener {

    private JFrame frame;
    private Timer starTimer;
    private Timer delayTimer;
    private final java.util.List<Point> stars = new java.util.ArrayList<>();
    private final java.util.List<Integer> starSpeeds = new java.util.ArrayList<>();
    private JButton mainMenuButton;
    private JButton playAgainButton;

    public EndPanel (JFrame frame) {
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

        // Timer for sidlak-sidlak effect
        starTimer = new Timer(50, this);
        starTimer.start();


    }

    private JButton createButton(String str){

        JButton button = new JButton(str);
        button.setFocusPainted(false);
        button.setFont(new Font("Monospaced", Font.BOLD, 30));
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        button.setSize(300, 60);
        return button;
    }

    private void playAgain(){

        BattlePanel battlePanel = new BattlePanel(frame);
        frame.setContentPane(battlePanel);
        frame.revalidate();
        frame.repaint();
        battlePanel.requestFocusInWindow();
    }

    private void generateStars(int count) {
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            stars.add(new Point(rand.nextInt(900), rand.nextInt(600)));
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

        // Draw “ROUND 2 FIGHT” text
        g.setFont(new Font("Monospaced", Font.BOLD, 70));
        FontMetrics fm = g.getFontMetrics();
        String text = "FINISH ROUND";
        int x = (getWidth() - fm.stringWidth(text)) / 2;
        int y = getHeight() / 2 - 50;
        g.drawString(text, x, y);

        buttonPosition();
    }

    private void buttonPosition() {

        int w = getWidth();
        int h = getHeight();
        int buttonWidth = 300;
        int buttonHeight = 60;
        int x = (w - buttonWidth) / 2;
        playAgainButton.setLocation(x, h / 2 + 50);
        mainMenuButton.setLocation(x, h / 2 + 130);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // Make stars move down for sidlak-sidlak effect
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
