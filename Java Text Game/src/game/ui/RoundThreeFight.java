package game.ui;

import game.logic.characters.Character;
import game.logic.characters.CharacterFactory;
import game.logic.characters.CharacterInfo;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


public class RoundThreeFight extends JPanel implements ActionListener {

    final ArrayList<Character> chosenCharacters = new ArrayList<>();

    private JFrame frame;
    private final ArrayList<JButton> selectedCharacters = new ArrayList<>();
    private final ArrayList<JButton> characterButtons = new ArrayList<>();
    private final ArrayList<Point> stars = new ArrayList<>();
    private final ArrayList<Integer> starSpeeds = new ArrayList<>();

    private JButton startBattleButton;
    private javax.swing.Timer starTimer;
    private Rectangle monsterBounds;
    private JPanel titlePanel;
    private JLabel characterInfoLabel;

    private ArrayList<String> selectedCharacterNames = new ArrayList<>();

    // Kini nga constructor mag-andam sa character selection para sa round 3.
    public RoundThreeFight(JFrame frame) {
        this.frame = frame;
        setLayout(null);
        setBackground(Color.BLACK);

        generateStars(180);
        starTimer = new javax.swing.Timer(50, this);
        starTimer.start();

        titlePanel = new JPanel();
        titlePanel.setBackground(Color.BLACK);
        titlePanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        titlePanel.setLayout(null);
        add(titlePanel);

        JLabel titleLabel = new JLabel("Choose 2 characters to defeat the Schoolworks Monster!");
        titleLabel.setFont(new Font("Monospaced", Font.PLAIN, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(40, 25, 700, 30);
        titlePanel.add(titleLabel);

        JLabel counterLabel = new JLabel("Selected: 0/2");
        counterLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        counterLabel.setForeground(Color.GREEN);
        counterLabel.setBounds(40, 55, 200, 20);
        titlePanel.add(counterLabel);

        characterInfoLabel = new JLabel("", SwingConstants.CENTER);
        characterInfoLabel.setFont(new Font("Monospaced", Font.PLAIN, 12));
        characterInfoLabel.setForeground(Color.WHITE);
        characterInfoLabel.setBounds(0, 0, 800, 80);
        add(characterInfoLabel);

        String[] characters = CharacterInfo.HERO_NAMES;
        for (String name : characters) {
            JButton charBtn = createCharacterButton(name);
            charBtn.addActionListener(e -> handleCharacterClick(charBtn, counterLabel));
            add(charBtn);
            characterButtons.add(charBtn);
        }

        startBattleButton = new JButton("START BATTLE");
        startBattleButton.setFont(new Font("Monospaced", Font.BOLD, 16));
        startBattleButton.setForeground(Color.WHITE);
        startBattleButton.setBackground(Color.BLACK);
        startBattleButton.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
        startBattleButton.setFocusPainted(false);
        startBattleButton.addActionListener(e -> startBattleAction());
        startBattleButton.setEnabled(false);
        add(startBattleButton);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            // Kini nga function mo-adjust sa positions kung mausab ang window size.
            public void componentResized(ComponentEvent e) {
                repositionComponents();
            }
        });

        SwingUtilities.invokeLater(this::repositionComponents);
    }

    // Kini nga function mohimo ug button para sa character.
    private JButton createCharacterButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(Color.BLACK);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Monospaced", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        btn.setContentAreaFilled(true);
        return btn;
    }

    // Kini nga function modala sa pagpili ug pag-deselect sa character.
    private void handleCharacterClick(JButton button, JLabel counterLabel) {
        String characterName = button.getText();

        String info = CharacterInfo.descriptionFor(characterName);
        characterInfoLabel.setText("<html><center>" + info + "</center></html>");

        if (selectedCharacters.contains(button)) {
            // Deselect character
            button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            button.setBackground(new Color(40, 40, 40));
            selectedCharacters.remove(button);
            selectedCharacterNames.remove(characterName);
            chosenCharacters.removeIf(c -> c.getName().equals(characterName));

        } else {
            if (selectedCharacters.size() >= 2) {
                JOptionPane.showMessageDialog(this, "You can only select 2 characters!");
                return;
            }

            // Select character
            button.setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
            button.setBackground(new Color(0, 80, 120));
            selectedCharacters.add(button);
            selectedCharacterNames.add(characterName);

            Character chosen = CharacterFactory.createHero(characterName);
            if (chosen != null) {
                chosenCharacters.add(chosen);
            }
        }

        // Update counter
        counterLabel.setText("Selected: " + selectedCharacters.size() + "/2");
        startBattleButton.setEnabled(selectedCharacters.size() == 2);
        startBattleButton.setBackground(selectedCharacters.size() == 2 ?
                new Color(0, 150, 0) : new Color(0, 100, 0));
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
    // Kini nga function modrawing sa stars ug finals monster preview.
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Stars
        g2d.setColor(Color.WHITE);
        for (Point star : stars) g2d.fillRect(star.x, star.y, 2, 2);

        if (monsterBounds == null) return;

        int wid = 200, height = 180;
        int x = (getWidth() - wid) / 2;
        int y = (getHeight() - (height - 60)) / 2;
        ImageIcon monsterImageIcon = ImageAssets.load("HomeworkMonsterThree.png");
        Image monsterImage = monsterImageIcon.getImage();
        g2d.drawImage(monsterImage, x, y, wid, height, this);
    }


    // Kini nga function mobutang sa title, buttons, monster, ug start button.
    private void repositionComponents() {
        int panelWidth = Math.max(getWidth(), 900);
        int panelHeight = Math.max(getHeight(), 600);

        int titleWidth = 700, titleHeight = 90;
        int titleX = (panelWidth - titleWidth) / 2;
        int titleY = 30;
        titlePanel.setBounds(titleX, titleY, titleWidth, titleHeight);

        int buttonWidth = 120, buttonHeight = 80, spacing = 20;
        int totalWidth = characterButtons.size() * buttonWidth + (characterButtons.size() - 1) * spacing;
        int startX = (panelWidth - totalWidth) / 2;
        int charY = titleY + titleHeight + 20;

        for (int i = 0; i < characterButtons.size(); i++) {
            JButton btn = characterButtons.get(i);
            btn.setBounds(startX + i * (buttonWidth + spacing), charY, buttonWidth, buttonHeight);
        }

        // position sa monster
        int monsterWidth = 200, monsterHeight = 180;
        int monsterX = (panelWidth - monsterWidth) / 2;
        int monsterY = charY + buttonHeight + 10;

        monsterBounds = new Rectangle(monsterX, monsterY, monsterWidth, monsterHeight);

        int infoX = (panelWidth - 600) / 2;
        int infoY = monsterY + monsterHeight - 25;
        characterInfoLabel.setBounds(infoX, infoY, 600, 60);

        int battleButtonWidth = 200, battleButtonHeight = 50;
        int battleButtonX = (panelWidth - battleButtonWidth) / 2;
        int battleButtonY = monsterY + monsterHeight + 40;

        startBattleButton.setBounds(battleButtonX, battleButtonY, battleButtonWidth, battleButtonHeight);

        repaint();
    }

    @Override
    // Kini nga function mopalihok sa stars kada timer tick.
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

    // Kini nga function magsugod sa round 3 battle kung duha na ka character ang napili.
    private void startBattleAction() {
        if (selectedCharacterNames.size() != 2) {
            JOptionPane.showMessageDialog(this, "Please select exactly 2 characters!");
            return;
        }

        FightThree roundThreeScene = new FightThree(frame, chosenCharacters);
        frame.setContentPane(roundThreeScene);
        frame.revalidate();
        frame.repaint();

    }

}
