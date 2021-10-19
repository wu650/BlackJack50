package com.blackjacksp.frontend;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;


public class UI extends JFrame {

    Game game;

    // Frame components
    private Dimension frameSize;
    public Image icon;

    // Will allow for sizing content pain while ignoring border measurements
    private Container container;

    // Holds all elements for the main game
    // Allows for constant menu access during game no matter which object is in focus
    public JPanel gamePanel;

    // Start screen components
    JLayeredPane start;
    JLabel logo;
    JLabel logoText;
    JButton startButton;
    JButton exitButton;

    // Game setup components
    JPanel setupPanel;
    JButton enterGame;
    JRadioButton[] deckSelect;
    ButtonGroup deckButtonGroup;
    JRadioButton[] handSelect;
    ButtonGroup handButtonGroup;
    JTextField playerName;

    // Card displays
    JPanel userHand;
    JLabel[] userCards;
    JPanel dealerHand;
    JLabel dealerCards[];
    JLabel userNameLabel;
    JLabel dealerNameLabel;

    JLabel discardPile;

    // Middle panel
    JPanel deckDisplay;
    Image deckBack;
    JTextPane messagePrompt;

    // Control panel
    JPanel dashboard;
    JButton[] controlButtons;
    JLabel[] gameTallies;

    // Game menu
    JPanel menu;
    JButton[] menuButtons;

    ImageIcon cardBack;
    private Color bgColor;
    public int cardWidth;
    public int cardHeight;

    public String getPlayerName() {
        return playerName.getText();
    }

    public UI(Game game) {

        this.game = game;

        bgColor = new Color(0,81,0);
        cardWidth = 712;
        cardHeight = 1008;
        frameSize = new Dimension(1024, 766);
        container = this.getContentPane();
        container.setPreferredSize(frameSize);
        container.setBackground(bgColor);
        this.gamePanel = new JPanel();
        this.gamePanel.setBounds(0,0,1024,766);
        this.gamePanel.setLayout(null);
        this.gamePanel.setVisible(false);
        this.gamePanel.setBackground(null);
        this.add(gamePanel);
        this.pack();
        this.setTitle("blackjackSP");
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        // Allow for absolute positioning of panels
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create universal cardback icon
        try {
            this.icon = ImageIO.read(new File(getClass().getResource("res/cardBack.png").toURI()));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        this.setIconImage(icon);

        startScreenUI();
        gameSetupUI();
        cardDisplayUI();
        dashBoardUI();
        menuUI();

        this.setVisible(true);
    }

    public void startScreenUI() {
        start = new JLayeredPane();
        start.setBounds(0,0,1024,766);
        start.setVisible(true);

        // Load in and add card image
        int logoW = 332;
        int logoH = 384;
        logo = new JLabel();
        logo.setBounds(346, 70, logoW, logoH);
        try {
            Image logoImage = ImageIO.read(new File(getClass().getResource("res/logo.png").toURI()));
            logoImage = logoImage.getScaledInstance(logoW, logoH, Image.SCALE_SMOOTH);
            logo.setIcon(new ImageIcon(logoImage));
        }
        catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        logo.setVisible(true);
        start.add(logo, 1, 0);

        // Load in and add logo text
        int logoTextW = 365;
        int logoTextH = 110;
        logoText = new JLabel();
        logoText.setBounds(329, 330,logoTextW,logoTextH);
        logoText.setForeground(Color.BLACK);
        logoText.setFont(new Font("Impact", Font.BOLD, 67));
        logoText.setText("blackjackSP");
        logoText.setVisible(true);
        start.add(logoText, 2, 0);

        startButton = new JButton("Start Game");
        startButton.setBackground(Color.LIGHT_GRAY);
        startButton.setForeground(Color.WHITE);
        startButton.setFont(new Font("Times New Roman", Font.BOLD, 34));
        startButton.setFocusPainted(false);
        startButton.setBounds(387,511,250,55);
        startButton.addActionListener(e -> game.renderSetupUI());
        start.add(startButton);

        exitButton = new JButton("Exit");
        exitButton.setBackground(Color.LIGHT_GRAY);
        exitButton.setForeground(Color.WHITE);
        exitButton.setFont(new Font("Times New Roman", Font.BOLD, 34));
        exitButton.setFocusPainted(false);
        exitButton.setBounds(450, 606, 125, 55);
        exitButton.addActionListener(e -> System.exit(0));
        start.add(exitButton);

        this.add(start);
    }

    public void gameSetupUI() {
        setupPanel = new JPanel();
        setupPanel.setLayout(new GridLayout(4,1));
        setupPanel.setBorder(new LineBorder(Color.WHITE, 2));
        setupPanel.setOpaque(false);
        setupPanel.setBounds(50,50,924, 666);


        JPanel[] setupSubPanels = new JPanel[4];
        for (int i = 0; i < 4; i++) {
            setupSubPanels[i] = new JPanel();
            setupSubPanels[i].setLayout(new FlowLayout(FlowLayout.LEFT, 30,0));
            setupSubPanels[i].setOpaque(false);
            setupPanel.add(setupSubPanels[i]);
        }

        // Add the 3 user prompts to setup the game
        JLabel decksLabel = new JLabel("Select the number of decks to use:");
        decksLabel.setForeground(Color.WHITE);
        decksLabel.setFont(new Font("Times New Roman", Font.PLAIN, 34));
        setupSubPanels[0].add(decksLabel);

        JLabel handsLabel = new JLabel("Number of hands to be played:");
        handsLabel.setForeground(Color.WHITE);
        handsLabel.setFont(new Font("Times New Roman", Font.PLAIN, 34));
        setupSubPanels[1].add(handsLabel);

        JLabel nameLabel = new JLabel("Your name:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 34));
        setupSubPanels[2].add(nameLabel);

        deckSelect = new JRadioButton[5];
        deckSelect[0] = new JRadioButton("1");
        deckSelect[1] = new JRadioButton("2");
        deckSelect[2] = new JRadioButton("4");
        deckSelect[3] = new JRadioButton("6");
        deckSelect[4] = new JRadioButton("8");

        for (int i = 0; i < 5; i++) {
            int num = Integer.parseInt(deckSelect[i].getText());
            deckSelect[i].addActionListener(e -> game.gamePlay.setNumDecks(num));
        }

        // Ensure only one radio button can be selected at a time by grouping
        // Add buttons to display after they are added to the group
        deckButtonGroup = new ButtonGroup();
        for (JRadioButton button : deckSelect) {
            deckButtonGroup.add(button);
            button.setOpaque(false);
            button.setFont(new Font("Times New Roman", Font.PLAIN, 34));
            button.setForeground(Color.WHITE);
            button.addActionListener(e -> game.gamePlay.setNumHands(Integer.parseInt(button.getText())));
            setupSubPanels[0].add(button);
        }

        handSelect = new JRadioButton[5];
        handSelect[0] = new JRadioButton("1");
        handSelect[1] = new JRadioButton("3");
        handSelect[2] = new JRadioButton("5");
        handSelect[3] = new JRadioButton("7");
        handSelect[4] = new JRadioButton("11");

        for (int i = 0; i < 5; i++) {
            int num = Integer.parseInt(handSelect[i].getText());
            handSelect[i].addActionListener(e -> game.gamePlay.setNumHands(num));
        }

        handButtonGroup = new ButtonGroup();
        for (JRadioButton button : handSelect) {
            handButtonGroup.add(button);
            button.setOpaque(false);
            button.setFont(new Font("Times New Roman", Font.PLAIN, 34));
            button.setForeground(Color.WHITE);
            button.addActionListener(e -> game.gamePlay.setNumDecks(Integer.parseInt(button.getText())));
            setupSubPanels[1].add(button);
        }

        // Add area for user to enter their name
        playerName = new JTextField(20);
        playerName.setPreferredSize(new Dimension(20,30));
        setupSubPanels[2].add(playerName);

        // Add button to move to main gamestate
        enterGame = new JButton("Proceed to Game");
        enterGame.setBackground(Color.LIGHT_GRAY);
        enterGame.setForeground(Color.WHITE);
        enterGame.setFont(new Font("Times New Roman", Font.BOLD, 34));
        enterGame.setFocusPainted(false);
        enterGame.addActionListener(game.actionHandler);
        enterGame.setActionCommand("enterGame");
        setupSubPanels[3].add(enterGame);
        setupSubPanels[3].setLayout(new FlowLayout());

        setupPanel.setVisible(false);
        this.add(setupPanel);
    }

    public void cardDisplayUI() {
        // Creating panel that will display the user hand
        userHand = new JPanel();
        userHand.setBorder(new LineBorder(Color.WHITE, 2));
        userHand.setOpaque(false);
        userHand.setBounds(17,514,cardWidth + 6, cardHeight/5);
        userHand.setLayout(new GridLayout(1,5,5,0));

        // Creating slots to hold user cards
        userCards = new JLabel[5];
        for (int i = 0; i < 5; i++) {
            userCards[i] = (new JLabel());
            userCards[i].setVisible(true);
            userHand.add(userCards[i]);
        }

        // Creating panel that will display the dealer hand
        dealerHand = new JPanel();
        dealerHand.setBorder(new LineBorder(Color.WHITE, 2));
        dealerHand.setOpaque(false);
        dealerHand.setBounds(17,50,cardWidth + 6, cardHeight/5);
        dealerHand.setLayout(new GridLayout(1,5,5,0));

        // Creating slots to hold dealer cards
        dealerCards = new JLabel[5];
        for (int i = 0; i < 5; i++) {
            dealerCards[i] = new JLabel();
            dealerCards[i].setVisible(true);
            dealerHand.add(dealerCards[i]);
        }

        // Creating middle panel to display deck and scores
        deckDisplay = new JPanel();
        deckDisplay.setOpaque(false);
        deckDisplay.setBounds(17, 282, cardWidth + 6, cardHeight/5);
        deckDisplay.setLayout(null);

        // Add image for the deck to middle panel
        deckBack = icon.getScaledInstance(cardWidth/5, cardHeight/5,Image.SCALE_SMOOTH);
        ImageIcon deck = new ImageIcon(deckBack);
        JLabel deckHolder = new JLabel();
        deckHolder.setIcon(deck);
        deckHolder.setBounds(0, 0, cardWidth/5, cardHeight/5);
        deckDisplay.add(deckHolder);

        // Create discard pile area
        discardPile = new JLabel();
        discardPile.setBounds(cardWidth / 5, 0, cardWidth / 5, cardHeight / 5);
        discardPile.setVisible(false);
        deckDisplay.add(discardPile);

        // Add text are for user prompts
        messagePrompt = new JTextPane();
        messagePrompt.setOpaque(false);
        messagePrompt.setForeground(Color.WHITE);
        messagePrompt.setFont(new Font("Times New Roman", Font.PLAIN, 28));
        messagePrompt.setBounds(335, 75, 377, 51);
        messagePrompt.setText("Testing part 2");
        messagePrompt.setFocusable(false);
        deckDisplay.add(messagePrompt);

        userNameLabel = new JLabel();
        userNameLabel.setBounds(17, 488, 120, 25);
        userNameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        userNameLabel.setForeground(Color.WHITE);

        dealerNameLabel = new JLabel();
        dealerNameLabel.setBounds(17, 23, 120, 25);
        dealerNameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        dealerNameLabel.setForeground(Color.WHITE);

        userHand.setVisible(true);
        dealerHand.setVisible(true);
        deckDisplay.setVisible(true);
        userNameLabel.setVisible(true);
        dealerNameLabel.setVisible(true);

        // Adding subcomponents to main display
        gamePanel.add(userHand);
        gamePanel.add(dealerHand);
        gamePanel.add(deckDisplay);
        gamePanel.add(userNameLabel);
        gamePanel.add(dealerNameLabel);
    }

    public void dashBoardUI() {
        dashboard = new JPanel();
        dashboard.setBackground(bgColor);
        dashboard.setBorder(new BevelBorder(BevelBorder.RAISED));
        dashboard.setBounds(752,50,252,cardWidth-46);
        dashboard.setLayout(new GridLayout(7,1,0,20));

        // Instantiate and format the three control buttons
        controlButtons = new JButton[3];
        for (int i = 0; i < 3; i++) {
            controlButtons[i] = new JButton("Test");
            controlButtons[i].setBackground(null);
            controlButtons[i].setForeground(Color.WHITE);
            controlButtons[i].setFocusPainted(false);
            controlButtons[i].setBorder(null);
            controlButtons[i].setFont(new Font("Times New Roman", Font.PLAIN, 34));
            controlButtons[i].setVisible(true);
            controlButtons[i].setEnabled(false);
            controlButtons[i].addActionListener(game.actionHandler);
            dashboard.add(controlButtons[i]);
        }

        controlButtons[0].setText("Hit");
        controlButtons[1].setText("Stick");
        controlButtons[2].setText("Continue");

        controlButtons[0].setActionCommand("hit");
        controlButtons[1].setActionCommand("stick");
        controlButtons[2].setActionCommand("continue");

        // Instantiate and format game scores
        gameTallies = new JLabel[4];
        for (int i = 0; i < 4; i++) {
            gameTallies[i] = new JLabel();
            gameTallies[i].setBackground(null);
            gameTallies[i].setForeground(Color.WHITE);
            gameTallies[i].setFont(new Font("Times New Roman", Font.PLAIN, 34));
            gameTallies[i].setHorizontalAlignment(SwingConstants.CENTER);
            dashboard.add(gameTallies[i]);
        }

        gameTallies[0].setText("Played: -");
        gameTallies[1].setText("Won: -");
        gameTallies[2].setText("Lost: -");
        gameTallies[3].setText("Tied: -");

        // Pause menu instruction
        JLabel pauseInstruction = new JLabel();
        pauseInstruction.setBackground(null);
        pauseInstruction.setForeground(Color.WHITE);
        pauseInstruction.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        pauseInstruction.setText("*Press P to pause");
        pauseInstruction.setBounds(895, 741,130, 25);
        pauseInstruction.setVisible(true);
        gamePanel.add(pauseInstruction);

        dashboard.setVisible(true);
        gamePanel.add(dashboard);
    }

    // Screen to be shown on pause menu or at end of game
    public void menuUI() {
        int menuW = 342;
        int menuH = 256;

        menu = new JPanel();
        menu.setBounds(341, 255, 342,256);
        menu.setBackground(Color.LIGHT_GRAY);
        menu.setOpaque(true);
        menu.setBorder(new BevelBorder(BevelBorder.RAISED));
        menu.setLayout(new GridLayout(3,1,0,5));

        // Adding menu buttons
        menuButtons = new JButton[3];
        for (int i = 0; i < 3; i++) {
            menuButtons[i] = new JButton();
            menuButtons[i].setBackground(null);
            menuButtons[i].setForeground(Color.WHITE);
            menuButtons[i].setFont(new Font("Times New Roman", Font.BOLD, 34));
            menuButtons[i].setFocusPainted(false);
            menuButtons[i].setHorizontalAlignment(SwingConstants.CENTER);
            menuButtons[i].addActionListener(game.actionHandler);
            menuButtons[i].setEnabled(true);
            menu.add(menuButtons[i]);
        }

        menuButtons[0].setText("Resume");
        menuButtons[1].setText("New Game");
        menuButtons[2].setText("Quit");

        menuButtons[0].setActionCommand("resume");
        menuButtons[1].setActionCommand("newGame");
        menuButtons[2].setActionCommand("quit");

        menuButtons[2].addActionListener(e -> System.exit(0));

        menu.setVisible(false);
        gamePanel.add(menu);
    }
}
