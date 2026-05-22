package game.ui;

import game.logic.characters.Character;
import game.logic.combat.ActionResult;
import game.logic.combat.TurnSystem;


import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.stream.IntStream;


class BattleScenePanel extends JPanel implements ActionListener {
    private static final int STAR_COUNT = 150;
    private static final int DIALOGUE_DURATION_MS = 3000;

    private final JFrame frame;
    private final ArrayList<Character> characters;
    private final StarField starField = new StarField(STAR_COUNT);

    private javax.swing.Timer battleTimer;

    private JLabel[] characterDialogueLabels;
    private JLabel monsterDialogueLabel;

    private JLabel[] HPText, MPText;
    private JLabel monsterHPLabel;

    private Character monster;
    private TurnSystem turnSystem;

    private JLabel battleTextLabel;

    // Kini nga constructor mag-andam sa first battle scene.
    public BattleScenePanel(JFrame frame, ArrayList<Character> characters) {
        this.frame = frame;
        this.characters = characters;
        this.monster = new Character.HomeworkMonster();
        turnSystem = new TurnSystem(characters, monster);

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        setupBattleUI();

        battleTimer = new javax.swing.Timer(50, this);
        battleTimer.start();
    }

    // Kini nga function mag-layout sa top bar, character cards, enemy panel, ug action buttons.
    private void setupBattleUI() {
        // Top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel battleLabel = new JLabel("BATTLE IN PROGRESS!", SwingConstants.CENTER);
        battleLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        battleLabel.setForeground(new Color(255, 100, 100));
        topPanel.add(battleLabel, BorderLayout.CENTER);

        JButton backButton = new JButton("BACK TO SELECTION");
        backButton.setFont(new Font("Monospaced", Font.PLAIN, 12));
        backButton.setBackground(new Color(60, 60, 60));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> GameNavigator.showCharacterSelection(frame));
        topPanel.add(backButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel characterPanel = createCharacterPanel();
        JPanel monsterPanel = createMonsterPanel();
        centerPanel.add(characterPanel);
        centerPanel.add(monsterPanel);
        add(centerPanel, BorderLayout.CENTER);

        JPanel actionPanel = createActionPanel();
        add(actionPanel, BorderLayout.SOUTH);

        JPanel textPanel = createTextPanel();

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setOpaque(false);
        centerContainer.add(centerPanel, BorderLayout.CENTER);
        centerContainer.add(textPanel, BorderLayout.SOUTH);

        add(centerContainer, BorderLayout.CENTER);
    }

    // Kini nga function mohimo sa panel nga mopakita sa battle messages.
    private JPanel createTextPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        panel.setPreferredSize(new Dimension(800, 40));

        battleTextLabel = new JLabel("", SwingConstants.CENTER);
        battleTextLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        battleTextLabel.setForeground(Color.YELLOW);
        battleTextLabel.setOpaque(true);
        battleTextLabel.setBackground(new Color(0, 0, 0, 180));

        panel.add(battleTextLabel, BorderLayout.CENTER);
        return panel;
    }

    // Kini nga function mopakita ug temporary battle message sa screen.
    private void showBattleText(String text) {
        battleTextLabel.setText(text);

        javax.swing.Timer clearTimer = new javax.swing.Timer(DIALOGUE_DURATION_MS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                battleTextLabel.setText("");
                ((javax.swing.Timer) e.getSource()).stop();
            }
        });
        clearTimer.start();
        clearTimer.setRepeats(false);
    }

    // Kini nga function mohimo sa team panel nga naay image, HP, MP, ug dialogue.
    private JPanel createCharacterPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0, 150, 255), 3),
                "YOUR TEAM",
                TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Monospaced", Font.BOLD, 16), new Color(100, 200, 255)
        ));

        HPText = new JLabel[characters.size()];
        MPText = new JLabel[characters.size()];
        characterDialogueLabels = new JLabel[characters.size()];
        for (int i = 0; i < characters.size(); i++) {
            Character c = characters.get(i);

            JPanel charCard = new JPanel(new BorderLayout(10, 10));
            charCard.setBackground(new Color(40, 40, 80, 220));
            charCard.setBorder(BorderFactory.createLineBorder(new Color(100, 150, 255), 2));

            JLabel imageLabel = createImageForCharacter(c.getName());

            JPanel infoPanel = new JPanel(new GridLayout(4, 1));
            infoPanel.setBackground(new Color(40, 40, 80, 220));

            JLabel nameLabel = new JLabel(c.getName(), SwingConstants.CENTER);
            nameLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
            nameLabel.setForeground(new Color(200, 200, 255));

            characterDialogueLabels[i] = new JLabel("", SwingConstants.CENTER);
            characterDialogueLabels[i].setFont(new Font("Monospaced", Font.ITALIC, 13));
            characterDialogueLabels[i].setForeground(new Color(255, 255, 150));

            HPText[i] = new JLabel("HP: " + c.HP + "/" + c.maxHP, SwingConstants.CENTER);
            HPText[i].setFont(new Font("Monospaced", Font.BOLD, 12));
            HPText[i].setForeground(new Color(100, 255, 100));

            MPText[i] =  new JLabel("MP: " + c.MP + "/" + c.maxMP, SwingConstants.CENTER);
            MPText[i].setFont(new Font("Monospaced", Font.BOLD, 12));
            MPText[i].setForeground(new Color(100, 255, 100));

            infoPanel.add(nameLabel);
            infoPanel.add(characterDialogueLabels[i]);
            infoPanel.add(HPText[i]);
            infoPanel.add(MPText[i]);

            charCard.add(imageLabel, BorderLayout.WEST);
            charCard.add(infoPanel, BorderLayout.CENTER);

            panel.add(charCard);
        }

        return panel;
    }

    // Kini nga function mohimo sa enemy panel nga naay monster image ug HP.
    private JPanel createMonsterPanel() {
        JPanel monsterPanel = new JPanel(new BorderLayout())
        {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
            }
        };

        monsterPanel.setOpaque(false);
        monsterPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 100, 100), 3),
                "ENEMY",
                TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Monospaced", Font.BOLD, 16), new Color(255, 150, 150)
        ));

        JLabel monsterImageLabel = createMonsterImage(monster.getName());
        monsterPanel.add(monsterImageLabel, BorderLayout.CENTER);


        JPanel statsPanel = new JPanel(new GridLayout(3, 1));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel(monster.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
        nameLabel.setForeground(new Color(255, 100, 100));

        monsterDialogueLabel = new JLabel("", SwingConstants.CENTER);
        monsterDialogueLabel.setFont(new Font("Monospaced", Font.ITALIC, 13));
        monsterDialogueLabel.setForeground(new Color(255, 150, 150));

        monsterHPLabel = new JLabel("HP: " + monster.HP + "/" + monster.maxHP, SwingConstants.CENTER);
        monsterHPLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        monsterHPLabel.setForeground(new Color(255, 150, 150));

        statsPanel.add(nameLabel);
        statsPanel.add(monsterDialogueLabel);
        statsPanel.add(monsterHPLabel);
        monsterPanel.add(statsPanel, BorderLayout.SOUTH);

        return monsterPanel;
    }

    // Kini nga function moload ug moscale sa monster image.
    private JLabel createMonsterImage(String monsterName) {
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        ImageIcon icon = ImageAssets.load("HomeworkMonster.png");
        Image scaled = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaled));

        return imageLabel;
    }

    //ang new buttons
    // Kini nga function mohimo sa attack, skill, ug ultimate buttons sa kada character.
    private JPanel createActionPanel()
    {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (Character c : characters)
        {
            JPanel charActionPanel = new JPanel(new FlowLayout());
            charActionPanel.setBackground(new Color(40, 40, 80, 220));
            charActionPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(100, 100, 255), 2),
                    c.getName() + "'s Skills",
                    TitledBorder.CENTER, TitledBorder.TOP,
                    new Font("Monospaced", Font.BOLD, 12),
                    new Color(200, 200, 255)
            ));

            JButton attackBtn = new JButton("ATTACK");
            attackBtn.setFont(new Font("Monospaced", Font.BOLD, 10));
            attackBtn.setBackground(new Color(80, 80, 120));
            attackBtn.setForeground(Color.WHITE);
            attackBtn.setPreferredSize(new Dimension(100, 35));
            attackBtn.addActionListener(e ->
            {
                performAction(c, TurnSystem.BasicAttack, "ATTACK");
            });
            charActionPanel.add(attackBtn);

            JButton Skill = new JButton("Skill");
            Skill.setFont(new Font("Monospaced", Font.BOLD, 10));
            Skill.setBackground(new Color(83, 61, 143));
            Skill.setForeground(Color.WHITE);
            Skill.setPreferredSize(new Dimension(100, 35));
            Skill.addActionListener(e ->
            {
                performAction(c, c.skill1, "SKILL");
            });
            charActionPanel.add(Skill);

            JButton ultBtn = new JButton("ULTIMATE");
            ultBtn.setFont(new Font("Monospaced", Font.BOLD, 10));
            ultBtn.setBackground(new Color(100, 50, 120));
            ultBtn.setForeground(Color.WHITE);
            ultBtn.setPreferredSize(new Dimension(100, 35));
            ultBtn.addActionListener(e ->
            {
                performAction(c, c.Ultimate, "ULTIMATE");
            });
            charActionPanel.add(ultBtn);

            panel.add(charActionPanel);
        }

        return panel;
    }

    // Kini nga function modala sa common flow sa pag-attack o paggamit ug skill.
    private void performAction(Character actor, game.logic.skills.Skill skill, String actionName) {
        if (actor.isDown()) {
            showBattleText(actor.getName() + " is down!");
            return;
        }

        if (!actor.hasEnoughMp(skill.getMpCost())) {
            showBattleText("Not enough MP!");
            return;
        }

        Character target = targetFor(actor);
        ActionResult result = turnSystem.ActionChosen(actor, target, skill);
        HP();
        MP();
        showDialogue(actor.getName(), actionName);
        showBattleText(result.getMessage());
    }

    // Kini nga function mopili sa sakto nga target; si Cyrus motarget ug ally, ang uban motarget sa monster.
    private Character targetFor(Character actor) {
        if (!actor.getName().equals("Cyrus")) {
            return monster;
        }

        for (Character ally : characters) {
            if (ally != actor) {
                return ally;
            }
        }

        return actor;
    }

    // Kini nga function moload ug moscale sa image sa character.
    private JLabel createImageForCharacter(String characterName) {
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(80, 80));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        ImageIcon icon = ImageAssets.load(characterName.toLowerCase() + ".png");
        if (icon.getIconWidth() > 0)
        {
            Image scaled = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));
        }
        else
        {
            imageLabel.setOpaque(true);
            imageLabel.setBackground(new Color(60, 60, 120));
            imageLabel.setText(characterName.substring(0, 2).toUpperCase());
            imageLabel.setForeground(Color.WHITE);
        }

        return imageLabel;
    }

    // Kini nga function mopakita sa character ug monster dialogue depende sa action.
    private void showDialogue(String characterName, String action) {
        int charIndex = IntStream.range(0, characters.size()).filter(i -> characters.get(i).getName().equals(characterName)).findFirst().orElse(-1);

        if (charIndex != -1) {
            String dialogue = "";
            switch (characterName) {
                case "Briar" -> {
                    dialogue = switch (action) {
                        case "ATTACK" -> "Time to debug your existence!";
                        case "SKILL" -> "Let me hack through your defenses!";
                        case "ULTIMATE" -> "Deploy the mainframe attack!";
                        default -> dialogue;
                    };
                }
                case "Dirk" -> {
                    dialogue = switch (action) {
                        case "ATTACK" -> "This formula calculates your defeat!";
                        case "SKILL" -> "My calculations predict your downfall!";
                        case "ULTIMATE" -> "Maximum structural overload!";
                        default -> dialogue;
                    };
                }
                case "Careza" -> {
                    dialogue = switch (action) {
                        case "ATTACK" -> "This blueprint leads to your end!";
                        case "SKILL" -> "Let me draw your defeat!";
                        case "ULTIMATE" -> "Grand TUITION PAYERural annihilation!";
                        default -> dialogue;
                    };
                }
                case "Cyrus" -> {
                    dialogue = switch (action) {
                        case "ATTACK" -> "Time for some medical intervention!";
                        case "SKILL" -> "This medicine will cure your hostility!";
                        case "ULTIMATE" -> "Full system recovery initiated!";
                        default -> dialogue;
                    };
                }
                case "Brad" -> {
                    dialogue = switch (action) {
                        case "ATTACK" -> "Let me analyze your weaknesses!";
                        case "SKILL" -> "Your mind is no match for me!";
                        case "ULTIMATE" -> "Total mental breakdown initiated!";
                        default -> dialogue;
                    };
                }
            }

            String monsterSay = switch (action) {
                case "ATTACK" -> "Ouch! That hurt! You're grades will go down with you!";
                case "SKILL" -> "I'm assigning you more homework!";
                case "ULTIMATE" -> "I'm deducting participation points!";
                default -> "";
            };

            characterDialogueLabels[charIndex].setText(dialogue);
            monsterDialogueLabel.setText(monsterSay);

            new javax.swing.Timer(DIALOGUE_DURATION_MS, e -> {
                characterDialogueLabels[charIndex].setText("");
                monsterDialogueLabel.setText("");
                ((javax.swing.Timer)e.getSource()).stop();
            }).start();

            System.out.println(characterName + ": " + dialogue);
            System.out.println("Monster: " + monsterSay);
        }
    }

    // Kini nga function mo-update sa HP labels ug mo-check kung pildi o daog na.
    private void HP() {
        boolean allPlayersDead = true;

        for (int i = 0; i < characters.size(); i++) {
            Character c = characters.get(i);
            HPText[i].setText("HP: " + c.HP + "/" + c.maxHP);

            if (c.HP > 0) {
                allPlayersDead = false;
            }
        }

        monsterHPLabel.setText("HP: " + monster.HP + "/" + monster.maxHP);

        if (allPlayersDead) {

            javax.swing.Timer delay = new javax.swing.Timer(1000, new ActionListener() {
                @Override
                public  void actionPerformed(ActionEvent e) {
                    goToGameOver();
                }
            });

            delay.start();
            delay.setRepeats(false);
            return;
        }

        if (monster.HP <= 0) {
            monsterHPLabel.setText("HP: " + monster.HP + "/" + monster.maxHP);

            javax.swing.Timer delayTimer = new javax.swing.Timer(1000, new ActionListener() {
                @Override
                public  void actionPerformed(ActionEvent e) {
                    goToNextBattle();
                }
            });
            delayTimer.start();
            delayTimer.setRepeats(false);
            return;
        }


        if (turnSystem.hasEnded()) {

            battleTimer.stop();
        }
    }


    // Kini nga function mo-update sa MP labels sa team.
    private void MP()
    {
        for(int i = 0; i < characters.size(); i++)
        {
            Character c = characters.get(i);
            MPText[i].setText("MP: " + c.MP + "/" + c.maxMP);
        }
    }

    // Kini nga function mobalhin sa game over screen.
    private void goToGameOver() {
        battleTimer.stop();

        GameOverPanel gameOverPanel = new GameOverPanel(frame);
        frame.setContentPane(gameOverPanel);
        frame.revalidate();
        frame.repaint();
    }


    // Kini nga function mobalhin sa next battle transition screen.
    private void goToNextBattle() {
        battleTimer.stop();


        NextBattlePanel nextBattle = new NextBattlePanel(frame);
        frame.setContentPane(nextBattle);
        frame.revalidate();
        frame.repaint();
    }

    @Override
    // Kini nga function modrawing sa black background ug moving stars.
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setColor(Color.WHITE);
        starField.paint(g2d);
    }

    @Override
    // Kini nga function mopadagan sa star animation kada timer tick.
    public void actionPerformed(ActionEvent e) {
        starField.update(getWidth(), getHeight());
        repaint();
    }
}
