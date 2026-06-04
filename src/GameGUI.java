import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;

public class GameGUI extends JFrame {

    private final Game game;

    static final Color TABLE_COLOR = new Color(24, 92, 58);
    static final Color PANEL_COLOR = new Color(18, 70, 45);

    private PlayerMove selectedMove;

    private final JPanel activePlayerCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
    private final JPanel communityCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

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

    private void buildInterface() {

        JPanel root = new JPanel(new BorderLayout(15, 15));
        root.setBackground(TABLE_COLOR);
        root.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(root);

        // ===== TOP =====

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

        // ===== CENTER =====

        JPanel centerPanel = new JPanel();
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

        // ===== RIGHT INFO PANEL =====
        JPanel playersPanel = createPlayersPanel();
        root.add(playersPanel, BorderLayout.WEST);
        JPanel infoPanel = createInfoPanel();
        root.add(infoPanel, BorderLayout.EAST);
        

        // ===== BOTTOM ACTIONS =====

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        actionPanel.setOpaque(false);

        configureButton(foldButton);
        configureButton(callButton);
        configureButton(raiseButton);

        actionPanel.add(foldButton);
        actionPanel.add(callButton);
        actionPanel.add(raiseButton);

        root.add(actionPanel, BorderLayout.SOUTH);
    }

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

    private void styleInfoLabel(JLabel label) {
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void configureButton(JButton button) {
        button.setFocusable(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(160, 45));
    }

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
    }

    public JLabel getCardLabel(Card card) {

        URL imageUrl = getClass().getResource("/cards/" + card.toString() + ".png");

        if (imageUrl == null) {
            JLabel fallback = new JLabel(card.toString(), SwingConstants.CENTER);
            fallback.setPreferredSize(new Dimension(110, 150));
            fallback.setOpaque(true);
            fallback.setBackground(Color.WHITE);
            fallback.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            return fallback;
        }

        ImageIcon icon = new ImageIcon(imageUrl);
        Image scaled = icon.getImage().getScaledInstance(110, 150, Image.SCALE_SMOOTH);

        return new JLabel(new ImageIcon(scaled));
    }

    private JLabel getBackCardLabel() {

        URL imageUrl = getClass().getResource("/cards/BACK.png");

        if (imageUrl == null) {
            JLabel fallback = new JLabel("CARD", SwingConstants.CENTER);
            fallback.setPreferredSize(new Dimension(110, 150));
            fallback.setOpaque(true);
            fallback.setBackground(Color.LIGHT_GRAY);
            fallback.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            return fallback;
        }

        ImageIcon icon = new ImageIcon(imageUrl);
        Image scaled = icon.getImage().getScaledInstance(110, 150, Image.SCALE_SMOOTH);

        return new JLabel(new ImageIcon(scaled));
    }

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

    private void updateStageLabel(int uncoveredCards) {

        switch (uncoveredCards) {
            case 0 -> stageLabel.setText("Pre-Flop");
            case 3 -> stageLabel.setText("Flop");
            case 4 -> stageLabel.setText("Turn");
            case 5 -> stageLabel.setText("River");
            default -> stageLabel.setText("Texas Hold'em");
        }
    }

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

    public void updatePlayerLabel(Player player) {

        SwingUtilities.invokeLater(() -> playerLabel.setText(player.name + "'s Turn"));
    }

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

    private synchronized void submitMove(PlayerMove move) {

        selectedMove = move;
        notifyAll();
    }

    private void setActionButtonsEnabled(boolean enabled) {

        SwingUtilities.invokeLater(() -> {
            foldButton.setEnabled(enabled);
            callButton.setEnabled(enabled);
            raiseButton.setEnabled(enabled);
        });
    }

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
    
    public void updatePlayersPanel() {

        StringBuilder sb = new StringBuilder();

        for (Player p : game.players) {

            sb.append(p.name)
                    .append("  $")
                    .append(p.playerBalance);

            if (p == game.players.get(game.activePlayerIndex)) {
                sb.append("  ←");
            }

            sb.append("\n");
        }

        playersArea.setText(sb.toString());
    }
}