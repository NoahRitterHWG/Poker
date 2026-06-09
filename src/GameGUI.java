import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;

/**
 * The graphical user interface for the game.
 * <p>
 * Originally generated with AI assistance (ChatGPT and Claude) and
 * heavily reworked by Nouri Ayadhi.
 * </p>
 *
 * @author Nouri Ayadhi
 * @version 1.0
 */
public class GameGUI extends JFrame {

    private final Game game;

    static final Color TABLE_COLOR = new Color(24, 92, 58);
    static final Color PANEL_COLOR = new Color(18, 70, 45);

    private PlayerMove selectedMove;
    private boolean continuePressed;
    

    private final JPanel activePlayerCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
    private final JPanel communityCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
    private final JPanel centerPanel = new JPanel();

    private final JLabel playerLabel = new JLabel("Waiting for player...");
    private final JLabel stageLabel = new JLabel("Texas Hold'em");
    private final JLabel potLabel = new JLabel("Pot: $0");
    private final JLabel targetBetLabel = new JLabel("Highest Bet: $0");
    private final JLabel roundBetLabel = new JLabel("Current Bet: $0");

    private final JPanel playersPanel = new JPanel();
    private final JTextArea playersArea = new JTextArea();
    
    private final JButton foldButton = new JButton("Fold");
    private final JButton callButton = new JButton("Call / Check");
    private final JButton raiseButton = new JButton("Raise");
    private final JButton continueButton = new JButton("Continue");

    /**
     * Creates and displays the game window.
     *
     * @param game the game instance this GUI is connected to
     */
    GameGUI(Game game) {
        super("Poker");
        this.game = game;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setResizable(false);

        buildInterface();
        registerListeners();

        setVisible(true);
    }

    /**
     * Builds and arranges all UI components in the main window.
     */
    private void buildInterface() {

        JPanel root = new JPanel(new BorderLayout(15, 15));
        root.setBackground(TABLE_COLOR);
        root.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(root);

        // top section()
        JPanel topPanel = new JPanel();                                                  
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);

        stageLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        stageLabel.setForeground(Color.WHITE);
        stageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        communityCardsPanel.setOpaque(false);

        topPanel.add(stageLabel);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(communityCardsPanel);

        root.add(topPanel, BorderLayout.NORTH);

      
        // middle section
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        playerLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        playerLabel.setForeground(Color.WHITE);
        playerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        activePlayerCardsPanel.setOpaque(false);

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(playerLabel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(activePlayerCardsPanel);
        centerPanel.add(Box.createVerticalGlue());

        root.add(centerPanel, BorderLayout.CENTER);

        //right infopanel
        JPanel playersPanel = createPlayersPanel();
        root.add(playersPanel, BorderLayout.WEST);
        JPanel infoPanel = createInfoPanel();
        root.add(infoPanel, BorderLayout.EAST);
        

        //Buttons

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        actionPanel.setOpaque(false);

        configureButton(foldButton);
        configureButton(callButton);
        configureButton(raiseButton);
        configureButton(continueButton);
        continueButton.setVisible(false);

        actionPanel.add(foldButton);
        actionPanel.add(callButton);
        actionPanel.add(raiseButton);
        actionPanel.add(continueButton);

        root.add(actionPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates the right-side info panel showing pot size and bet info.
     *
     * @return the configured info panel
     */
    private JPanel createInfoPanel() {

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(280, 0));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(PANEL_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        styleInfoLabel(potLabel);
        styleInfoLabel(targetBetLabel);
        styleInfoLabel(roundBetLabel);

        panel.add(potLabel);
        panel.add(Box.createVerticalStrut(20));

        panel.add(targetBetLabel);
        panel.add(Box.createVerticalStrut(20));

        panel.add(roundBetLabel);

        return panel;
    }

    /**
     * Applies a consistent style to an info label.
     *
     * @param label the label to style
     */
    private void styleInfoLabel(JLabel label) {
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    /**
     * Applies a consistent style to a button.
     *
     * @param button the button to configure
     */
    private void configureButton(JButton button) {
        button.setFocusable(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(160, 45));
    }

    /**
     * Registers listeners for all buttons.
     */
    private void registerListeners() {

        foldButton.addActionListener(e -> submitMove(new PlayerMove(PlayerAction.FOLD, 0)));

        callButton.addActionListener(e -> submitMove(new PlayerMove(PlayerAction.CALL, 0)));

        raiseButton.addActionListener(e -> {

            Player activePlayer = game.players.get(game.activePlayerIndex);

            Integer raiseTo = game.askForValidInt(
                    "Raise to how much?",
                    game.currentTargetBet + 1,
                    activePlayer.playerBalance + activePlayer.roundBet);

            if (raiseTo != null) {
                submitMove(new PlayerMove(PlayerAction.RAISE, raiseTo));
            }
        });
        continueButton.addActionListener(e -> continueGame());
    }

    /**
     * Returns a label displaying the image of the given card.
     *
     * @param card the card to display
     * @return a {@link JLabel} with the card image 
     */
    public JLabel getCardLabel(Card card) {

        URL imageUrl = getClass().getResource("/cards/" + card.toString() + ".png");

        ImageIcon icon = new ImageIcon(imageUrl);
        Image scaled = icon.getImage().getScaledInstance(110, 150, Image.SCALE_SMOOTH);

        return new JLabel(new ImageIcon(scaled));
    }

    /**
     * Returns a label displaying the card back image.
     *
     * @return a {@link JLabel} with the card back image 
     */
    private JLabel getBackCardLabel() {

        URL imageUrl = getClass().getResource("/cards/BACK.png");

        ImageIcon icon = new ImageIcon(imageUrl);
        Image scaled = icon.getImage().getScaledInstance(110, 150, Image.SCALE_SMOOTH);

        return new JLabel(new ImageIcon(scaled));
    }

    /**
     * Updates the community cards panel to show the given number of revealed cards.
     * Unrevealed cards are shown face-down.
     *
     * @param middle         the middle object containing the community cards
     * @param uncoveredCards the number of cards to reveal
     */
    public void updateMiddleCards(Middle middle, int uncoveredCards) {

        SwingUtilities.invokeLater(() -> {

            communityCardsPanel.removeAll();

            for (int i = 0; i < middle.middleCards.size(); i++) {

                if (i < uncoveredCards) {
                    communityCardsPanel.add(getCardLabel(middle.middleCards.get(i)));
                } else {
                    communityCardsPanel.add(getBackCardLabel());
                }
            }

            updateStageLabel(uncoveredCards);

            communityCardsPanel.revalidate();
            communityCardsPanel.repaint();
        });
    }

    /**
     * Updates the stage label based on how many community cards are revealed.
     *
     * @param uncoveredCards the number of revealed cards
     */
    private void updateStageLabel(int uncoveredCards) {

        switch (uncoveredCards) {
            case 0 -> stageLabel.setText("Pre-Flop");
            case 3 -> stageLabel.setText("Flop");
            case 4 -> stageLabel.setText("Turn");
            case 5 -> stageLabel.setText("River");
            default -> stageLabel.setText("Texas Hold'em");
        }
    }

    /**
     * Updates the active player's card display.
     *
     * @param player the player whose cards should be shown
     */
    public void updateActivePlayerCards(Player player) {

        SwingUtilities.invokeLater(() -> {

            activePlayerCardsPanel.removeAll();

            for (Card card : player.handCards) {
                activePlayerCardsPanel.add(getCardLabel(card));
            }

            activePlayerCardsPanel.revalidate();
            activePlayerCardsPanel.repaint();
        });
    }

    /**
     * Updates the label showing whose turn it is.
     *
     * @param player the active player
     */
    public void updatePlayerLabel(Player player) {

        SwingUtilities.invokeLater(() -> playerLabel.setText(player.name + "'s Turn"));
    }

    /**
     * Blocks the game thread until the active player selects a move.
     * Enables the action buttons while waiting.
     *
     * @return the {@link PlayerMove} chosen by the player
     */
    public synchronized PlayerMove waitForMove() {

        selectedMove = null;

        setActionButtonsEnabled(true);

        while (selectedMove == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        setActionButtonsEnabled(false);

        return selectedMove;
    }

    /**
     * Blocks the game thread until the continue button is pressed.
     */
    public synchronized void waitForContinue() {

        continuePressed = false;

        while (!continuePressed) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Stores the selected move and notifies the waiting game thread.
     *
     * @param move the move to submit
     */
    private synchronized void submitMove(PlayerMove move) {

        selectedMove = move;
        notifyAll();
    }

    /**
     * Signals that the continue button was pressed and notifies the waiting game
     * thread.
     */
    private synchronized void continueGame() {

        continuePressed = true;
        notifyAll();
    }

    /**
     * Enables or disables the action buttons (Fold, Call, Raise).
     *
     * @param enabled {@code true} to enable, {@code false} to disable
     */
    private void setActionButtonsEnabled(boolean enabled) {

        SwingUtilities.invokeLater(() -> {
            foldButton.setEnabled(enabled);
            callButton.setEnabled(enabled);
            raiseButton.setEnabled(enabled);
        });
    }

    /**
     * Updates the info panel with the current pot, highest bet, and active player's
     * bet.
     *
     * @param player the currently active player
     */
    public void updateInfo(Player player) {

        SwingUtilities.invokeLater(() -> {

            potLabel.setText("Pot: $" + game.middle.gamePott);

            String raiser = "";

            for (Player p : game.players) {
                if (p.isLastRaiser) {
                    raiser = " (" + p.name + ")";
                    break;
                }
            }

            targetBetLabel.setText("Highest Bet: $" + game.currentTargetBet + raiser);
            roundBetLabel.setText("Current Bet: $" + player.roundBet);
            updatePlayersPanel();
        });
    }

    /**
     * Creates the left-side panel listing all players and their balances.
     *
     * @return the configured players panel
     */
    private JPanel createPlayersPanel() {

        playersPanel.setPreferredSize(new Dimension(250, 0));
        playersPanel.setLayout(new BorderLayout());
        playersPanel.setBackground(PANEL_COLOR);
        playersPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Players");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        playersArea.setEditable(false);
        playersArea.setFocusable(false);
        playersArea.setBackground(PANEL_COLOR);
        playersArea.setForeground(Color.WHITE);
        playersArea.setFont(new Font("Monospaced", Font.PLAIN, 16));

        playersPanel.add(title, BorderLayout.NORTH);
        playersPanel.add(playersArea, BorderLayout.CENTER);

        return playersPanel;
    }
    
    /**
     * Refreshes the players panel with current names, balances, and the active
     * player indicator.
     */
    public void updatePlayersPanel() {

        StringBuilder sb = new StringBuilder();

        for (Player p : game.players) {

            String line = String.format("%-9s $%6d",p.name,p.playerBalance);

            sb.append(line);

            if (p == game.players.get(game.activePlayerIndex)) {
                sb.append("  ←");
            }

            sb.append("\n");
        }

        playersArea.setText(sb.toString());
    }

    /**
     * Displays the showdown screen with all remaining players' hands.
     * Before reveal, player cards are shown face-down. After reveal, hand types and
     * winners are shown.
     *
     * @param players the players still in the round
     * @param winners the players who won a pot (highlighted with a yellow border)
     * @param reveal  {@code true} to show cards and hand types, {@code false} to
     *                keep them hidden
     */
    public void showShowdown(ArrayList<Player> players, ArrayList<Player> winners, boolean reveal) {

        SwingUtilities.invokeLater(() -> {

        centerPanel.removeAll();
        String inputtitel = "SHOWDOWN";
        if (game.roundEndedEarly){
            String winnerName = players.get(0).name;
            inputtitel = winnerName + " winns!";
        }
        JLabel title = new JLabel(inputtitel);

        title.setFont(new Font("SansSerif",Font.BOLD,32));

        title.setForeground(Color.WHITE);

        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(title);
        centerPanel.add(Box.createVerticalStrut(20));
        int columns;

        if (players.size() <= 4) {
            columns = 2;
        } 
        else {
            columns = 3;
        }

        JPanel showdownGrid = new JPanel(new GridLayout(0,columns,20,20));

        showdownGrid.setOpaque(false);

        showdownGrid.setOpaque(false);

        for (Player p : players) {
            boolean isWinner = winners.contains(p);
            
            JPanel playerPanel =new JPanel();

            playerPanel.setOpaque(false);
            playerPanel.setLayout(new BoxLayout(playerPanel,BoxLayout.Y_AXIS));

            JLabel nameLabel = new JLabel(p.name);
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel handLabel;

            if (reveal && !game.roundEndedEarly) {
                handLabel = new JLabel(p.bestHand.toString());
            } 
            else if (!reveal && !game.roundEndedEarly) {
                handLabel = new JLabel("???");
            }
            else {
                    handLabel = new JLabel("");
            }

            handLabel.setForeground(Color.WHITE);
            handLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            playerPanel.add(nameLabel);
            playerPanel.add(handLabel);

            JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

            cardsPanel.setOpaque(false);

            for (Card card : p.handCards) {

                if (reveal) {
                    cardsPanel.add(getCardLabel(card));
                } 
                else {
                    cardsPanel.add(getBackCardLabel());
                }
            }
            if (isWinner) {
                playerPanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW,4));
            }

            playerPanel.add(cardsPanel);
            showdownGrid.add(playerPanel);
        }
        centerPanel.add(showdownGrid);
        centerPanel.revalidate();
        centerPanel.repaint();
    });
    }
    
    /**
     * Restores the normal player view after a showdown.
     */
    public void showPlayerView() {

        SwingUtilities.invokeLater(() -> {

            centerPanel.removeAll();

            centerPanel.add(
                    Box.createVerticalGlue());

            centerPanel.add(playerLabel);

            centerPanel.add(
                    Box.createVerticalStrut(20));

            centerPanel.add(
                    activePlayerCardsPanel);

            centerPanel.add(
                    Box.createVerticalGlue());

            centerPanel.revalidate();
            centerPanel.repaint();
        });
    }

    /**
     * Switches the UI to showdown mode by hiding action buttons and showing the
     * continue button.
     */
    public void enterShowdownMode() {

        foldButton.setVisible(false);
        callButton.setVisible(false);
        raiseButton.setVisible(false);

        continueButton.setVisible(true);

        revalidate();
        repaint();
    }

    /**
     * Switches the UI back to normal mode by showing action buttons and hiding the
     * continue button.
     */
    public void exitShowdownMode() {

        foldButton.setVisible(true);
        callButton.setVisible(true);
        raiseButton.setVisible(true);

        continueButton.setVisible(false);

        revalidate();
        repaint();
    }

    /**
     * Sets the text on the continue button.
     *
     * @param text the new button label
     */
    public void setContinueButtonText(String text) {
        continueButton.setText(text);
    }
    /**
     * Switches the UI to a screen hiding the cards of the next player. Only continues after the button is pressed
     * 
     * @param player the player that is next to play
     */
    public void showHandoffScreen(Player player) {

        SwingUtilities.invokeLater(() -> {

            foldButton.setVisible(false);
            callButton.setVisible(false);
            raiseButton.setVisible(false);

            centerPanel.removeAll();

            JLabel title = new JLabel("NEXT PLAYER");
            title.setFont(new Font("SansSerif", Font.BOLD, 32));
            title.setForeground(Color.WHITE);
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerPanel.add(title);

            centerPanel.add(Box.createVerticalStrut(20));

            JPanel playerPanel = new JPanel();
            playerPanel.setOpaque(false);
            playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));

            JLabel nameLabel = new JLabel(player.name);
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


            JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            cardsPanel.setOpaque(false);
            cardsPanel.add(getBackCardLabel());
            cardsPanel.add(getBackCardLabel());

            playerPanel.add(nameLabel);
            playerPanel.add(cardsPanel);

            centerPanel.add(playerPanel);
            centerPanel.revalidate();
            centerPanel.repaint();

            setContinueButtonText("Show " + player.name + "'s cards");
            continueButton.setPreferredSize(new Dimension(320, 45));
            continueButton.setVisible(true);

        });

        waitForContinue();
        continueButton.setPreferredSize(new Dimension(160, 45));

        SwingUtilities.invokeLater(() -> {
            continueButton.setVisible(false);
            showPlayerView();
            foldButton.setVisible(true);
            callButton.setVisible(true);
            raiseButton.setVisible(true);
            centerPanel.revalidate();
            centerPanel.repaint();
        });
    }
}

