package game.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI().createAndShowGUI());
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame();
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.black);

        TitlePanel panel = new TitlePanel(frame);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(panel);
        frame.setVisible(true);

    }

    // == LOADING PANEL ==
    class LoadingPanel extends JPanel implements ActionListener {
        private Timer timer;
        private Timer progressTimer;
        private JFrame frame;
        private int progress = 0;
        private int angle = 0;

        public LoadingPanel(JFrame frame) {
            this.frame = frame;
            setLayout(null);
            setBackground(Color.BLACK);
        }

        public void startLoading() {
            timer = new Timer(50, e -> {
                angle = (angle + 5) % 360;
                repaint();
            });
            timer.start();

            progressTimer = new Timer(100, this);
            progressTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.WHITE);

            for (int i = 0; i < 150; i++) {
                int x = (int) (Math.random() * getWidth());
                int y = (int) (Math.random() * getHeight());
                g.fillRect(x, y, 2, 2);
            }

            drawLoadingWheel(g);

            g.setFont(new Font("Monospaced", Font.BOLD, 30));
            String loadingText = "Starting the brawl... " + progress + "%";
            int textWidth = g.getFontMetrics().stringWidth(loadingText);
            int textX = (getWidth() - textWidth) / 2;
            int textY = getHeight() / 2 + 100;
            g.drawString(loadingText, textX, textY);


            int barWidth = 300;
            int barHeight = 20;
            int barX = (getWidth() - barWidth) / 2;
            int barY = getHeight() / 2 + 150;

            g.drawRect(barX, barY, barWidth, barHeight);
            g.fillRect(barX, barY, (barWidth * progress) / 100, barHeight);
        }

        private void drawLoadingWheel(Graphics g) {
            int width = getWidth();
            int height = getHeight();
            int wheelSize = Math.min(width, height) / 3;
            int x = width / 2 - wheelSize / 2;
            int y = height / 3 - wheelSize / 2;

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(3f));

            int outerDiameter = wheelSize;
            int innerDiameter = (int)(wheelSize * 0.8);
            int outerRadius = outerDiameter / 2;
            int innerRadius = innerDiameter / 2;

            int centerX = x + outerRadius;
            int centerY = y + outerRadius;

            for(int i = 0; i < 12; i++) {
                double startAngle = Math.toRadians(i * 30 - angle);
                double endAngle = Math.toRadians(i * 30 + 60 - angle);

                int startX = (int) (centerX + outerRadius * Math.cos(startAngle));
                int startY = (int) (centerY + outerRadius * Math.sin(startAngle));
                int endX = (int) (centerX + innerRadius * Math.cos(endAngle));
                int endY = (int) (centerY + innerRadius * Math.sin(endAngle));

                g2d.drawLine(startX, startY, endX, endY);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            progress += 3;

            if (progress >= 100) {
                timer.stop();
                progressTimer.stop();

                // Transition sa next panel para mag start na ang game
                BattlePanel gamePanel = new BattlePanel(frame);
                frame.setContentPane(gamePanel);
                frame.revalidate();
                frame.repaint();
            }

            repaint();
        }

    }

    class TitlePanel extends JPanel implements ActionListener {

        private Timer timer;
        private JButton startButton, howToPlayButton, quitButton;
        private JFrame frame;

        private final ArrayList<Point> stars = new ArrayList<>();
        private final ArrayList<Integer> starMovement = new ArrayList<>();


        public TitlePanel(JFrame frame) {
            this.frame = frame;
            setLayout(null);
            setBackground(Color.BLACK);

            generateStars(150);

            // buttons sa title page
            startButton = createButton("START");
            howToPlayButton = createButton("DESCRIPTION");
            quitButton = createButton("QUIT");

            add(startButton);
            add(howToPlayButton);
            add(quitButton);

            // Kung mo-resize ang window, automatic re-center sa mga buttons
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    centerComponents();
                }
            });

            // Button actions para ika click mo lahus sa next action
            //Ge change nako(Tiro) kay para makita ang loading chuchu
            startButton.addActionListener(e -> {

                LoadingPanel loadingPanel = new LoadingPanel(frame);
                frame.setContentPane(loadingPanel);
                frame.revalidate();
                frame.repaint();
                loadingPanel.startLoading();
            });
            //SEAN IKAW LAN BAHALA SUMPAY ARI SA GAMEE SA START BUTTON 🙂
            howToPlayButton.addActionListener(e -> goToHowToPlayPage());
            quitButton.addActionListener(e -> System.exit(0));

            // Timer para i-refresh ang mga starts para mo sidlak sidlak sila
            timer = new Timer(50, this);
            timer.start();
        }

        private void generateStars(int count){

            Random ran = new Random();
            stars.clear();
            starMovement.clear();

            int w = 900;
            int h = 600;

            for(int i =0; i<count; i++){
                stars.add(new Point(ran.nextInt(w), ran.nextInt(h)));

                starMovement.add(1 + ran.nextInt(3));
            }
        }

        private JButton createButton(String text) {
            // Method para create ug uniform and neat ang style sa mga buttons para same sila tanan
            JButton button = new JButton(text);
            button.setFocusPainted(false);
            button.setFont(new Font("Monospaced", Font.BOLD, 30));
            button.setBackground(Color.BLACK);
            button.setForeground(Color.WHITE);
            button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            button.setSize(300, 60);
            return button;
        }

        private void centerComponents() {
            // Function para i-center ang mga buttons sa tunga
            int panelWidth = getWidth();
            int panelHeight = getHeight();

            int buttonWidth = startButton.getWidth();
            int x = (panelWidth - buttonWidth) / 2;
            int yStart = (panelHeight - 200) / 2;

            startButton.setLocation(x, yStart);
            howToPlayButton.setLocation(x, yStart + 80);
            quitButton.setLocation(x, yStart + 160);
        }

        private void goToHowToPlayPage() {
            // Kung i-click ang “How to Play” mo-move to next page which is naa didtu mga info sa duwa 🙂
            HowToPlayPanel nextPage = new HowToPlayPanel(frame);
            frame.setContentPane(nextPage);
            frame.revalidate();
            frame.repaint();
            nextPage.requestFocusInWindow();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.WHITE);

           for(Point p : stars){
               g.fillRect(p.x, p.y, 2, 2);
           }

            // Drawing sa title text and mga fonts niya
            g.setFont(new Font("Monospaced", Font.BOLD, 100));
            FontMetrics fm = g.getFontMetrics();
            String title = "N A S B";
            int x = (getWidth() - fm.stringWidth(title)) / 2;
            int y = 150;
            g.drawString(title, x, y);

            centerComponents();
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            Random ran = new Random();

            for(int i=0; i<stars.size(); i++){

                Point star = stars.get(i);
                int movement = starMovement.get(i);

                star.y += movement;

                if(star.y > getHeight()){
                    star.y = 0;
                    star.x = ran.nextInt(getWidth());
                }
            }

            repaint();

            // Para mu-refresh ang stars animation
        }
    }

    // === HOW TO PLAY PAGE ===
    class HowToPlayPanel extends JPanel implements ActionListener {

        private Timer timer;
        private JButton backButton;
        private JFrame frame;
        private JLabel titleLabel;
        private JTextArea textArea;
        private JScrollPane scrollPane;

        private final ArrayList<Point> stars = new ArrayList<>();
        private final ArrayList<Integer> starMovement = new ArrayList<>();

        public HowToPlayPanel(JFrame frame) {
            this.frame = frame;
            setLayout(null);
            setBackground(Color.BLACK);

            generateStars(150);

            // BACK BUTTON — mobalik sa main menu or sa main page or title screen
            backButton = new JButton("BACK");
            backButton.setFont(new Font("Monospaced", Font.BOLD, 20));
            backButton.setBackground(Color.BLACK);
            backButton.setForeground(Color.WHITE);
            backButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            backButton.setBounds(30, 30, 100, 40);
            add(backButton);

            backButton.addActionListener(e -> {
                TitlePanel mainPage = new TitlePanel(frame);
                frame.setContentPane(mainPage);
                frame.revalidate();
                frame.repaint();
                mainPage.requestFocusInWindow();
            });

            // Title label sa ibabaw
            titleLabel = new JLabel("HOW TO PLAY", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Monospaced", Font.BOLD, 40));
            titleLabel.setForeground(Color.WHITE);
            add(titleLabel);

            // Text area para sa full game info ug description
            textArea = new JTextArea(
                    "N A S B\n\n" +
                            "Non Academic School Brawl\n\n" +
                            "DESCRIPTION\n\n" +
                            "A turn-based brawl where the non-academic NAS squad fights through waves of schoolwork monsters. " +
                            "Each fighter represents a different NAS role, bringing a unique specialty, stat strength, and skill set " +
                            "to survive the chaos and win the brawl.\n\n" +
                            "CHARACTERS\n\n" +
                            "Char 1: Careza (TUITION PAYER) - HIGHEST DEFENSE\n" +
                            "Skill 1: T SQUARE - Uses a T square for offense.\n" +
                            "ULTIMATE: SUCCESSFUL FLOOR PLAN - Deals damage, gains limited defense, and heals Careza.\n\n" +
                            "Char 2: Cyrus (NAS MDC) - HIGHEST HP\n" +
                            "Skill 1: FIRST AID KIT - Heals a single ally.\n" +
                            "ULTIMATE: SYRINGE REVIVE - Restores the team's fighting power.\n\n" +
                            "Char 3: Briar (NAS CPE) - HIGHEST ATTACK\n" +
                            "Skill 1: CELLPHONE - Fires electric byte strikes.\n" +
                            "ULTIMATE: TRANSFORMATION - Boosts armor and power.\n\n" +
                            "Char 4: Dirk (NAS CE) - HIGHEST INTELLECT\n" +
                            "Skill 1: INFO DUMP - Confuses the enemy with too much information, dealing INT damage.\n" +
                            "ULTIMATE: HARD HATS - Provides safety gear and reduces enemy defence.\n\n" +
                            "Char 5: Brad (NAS PRESIDENT) - HIGHEST WISDOM\n" +
                            "Skill 1: PRESIDENT BOOK - Confuses enemies with leadership knowledge.\n" +
                            "ULTIMATE: EXISTENTIAL CRISIS - Heavy mental attack.\n\n"
            );

            // Text styling
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
            textArea.setForeground(Color.WHITE);
            textArea.setBackground(Color.BLACK);
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

            // Scroll pane para makita tanan text kung taas or sa ubos
            scrollPane = new JScrollPane(textArea);
            scrollPane.setBorder(null);
            scrollPane.getVerticalScrollBar().setUnitIncrement(15);
            add(scrollPane);

            // Kung i-resize ang window, mu-center gihapon ang content para kuan ba murag mobile view dimaguba format
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    centerContent();
                }
            });

            // Timer para sa star animation para mo sidlak
            timer = new Timer(50, this);
            timer.start();
        }

        private void generateStars(int count){

            Random ran = new Random();
            stars.clear();
            starMovement.clear();

            int w = 900;
            int h = 600;


            for(int i =0; i<count; i++){
                stars.add(new Point(ran.nextInt(w), ran.nextInt(h)));

                starMovement.add(1 + ran.nextInt(3));
            }
        }

        private void centerContent() {
            // Ibutang sa tunga ang title ug text area dynamically
            int panelWidth = getWidth();
            int panelHeight = getHeight();

            int textWidth = panelWidth - 200;
            int textHeight = panelHeight - 200;

            titleLabel.setBounds(0, 100, panelWidth, 50);
            scrollPane.setBounds((panelWidth - textWidth) / 2, 180, textWidth, textHeight);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.WHITE);

            // Background stars
           for(Point p : stars){
               g.fillRect(p.x, p.y, 2, 2);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            Random ran = new Random();

            for(int i=0; i<stars.size(); i++){
                Point star = stars.get(i);
                int movement = starMovement.get(i);

                star.y += movement;

                if(star.y > getHeight()){
                    star.y = 0;
                    star.x = ran.nextInt(getWidth());
                }
            }
            repaint(); // I-refresh para mu-glow ang mga bituon
        }
    }
}
