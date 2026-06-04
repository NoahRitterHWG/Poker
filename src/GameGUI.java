import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class GameGUI extends JFrame {

    Game game;

    static final Color greenBackground = new Color(55, 100, 80);
    int boardWidth = 1200;
    int boardHeight = boardWidth;
    boolean dealerHideCard;
    PlayerMove selectedMove;
    JPanel gamePanel = new JPanel();
    JPanel buttonPanel = new JPanel();
    JPanel activePlayerCardsPanel = new JPanel();
    JPanel comunutyCardsPanel = new JPanel();
    JPanel middlePanel = new JPanel();
    JPanel playerPanel = new JPanel();
    JPanel infoPanel = new JPanel();
    JButton foldButton = new JButton("Fold");
    JButton callButton = new JButton("Call/Check");
    JButton raiseButton = new JButton("Raise");
    JLabel middleLabel = new JLabel("Middle: \n ");
    JLabel playerLabel = new JLabel("");
    JLabel potLabel = new JLabel("Pot: ");
    JLabel targetBetLabel = new JLabel("Current highest Bet:");
    JLabel balanceLabel = new JLabel("Balance:");
    JLabel roundBetLabel = new JLabel("Your current Bet:");


    GameGUI(Game game){
        super("Poker");
        this.game = game;
        
        
        setSize(boardWidth, boardHeight);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(greenBackground);
        add(gamePanel);

        buttonPanel.setBackground(greenBackground);
        foldButton.setFocusable(false);
        callButton.setFocusable(false);
        raiseButton.setFocusable(false);
        buttonPanel.add(foldButton);
        buttonPanel.add(callButton);
        buttonPanel.add(raiseButton);
        gamePanel.add(buttonPanel, BorderLayout.SOUTH);

        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
        playerPanel.setBorder(BorderFactory.createEmptyBorder(270, 0, 0, 0));
        playerPanel.setBackground(greenBackground); 
        gamePanel.add(playerPanel);

        playerLabel.setFont(new Font("Arial", Font.BOLD, 30));
        playerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerPanel.add(playerLabel);

        activePlayerCardsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        activePlayerCardsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        activePlayerCardsPanel.setBackground(greenBackground);
        playerPanel.add(activePlayerCardsPanel);

        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
        middlePanel.setBackground(greenBackground);
        gamePanel.add(middlePanel, BorderLayout.NORTH);

        

        middleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        middleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        middleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        middlePanel.add(middleLabel); 


        comunutyCardsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        comunutyCardsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        comunutyCardsPanel.setBackground(greenBackground);
        middlePanel.add(comunutyCardsPanel);

        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        infoPanel.setBackground(greenBackground);
        infoPanel.setPreferredSize(new Dimension(250, 1200));
        gamePanel.add(infoPanel, BorderLayout.EAST);

        potLabel.setFont(new Font("Arial", Font.BOLD, 22));
        potLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(potLabel);
        infoPanel.add(Box.createVerticalStrut(20));

        targetBetLabel.setFont(new Font("Arial", Font.BOLD, 22));
        targetBetLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(targetBetLabel);
        infoPanel.add(Box.createVerticalStrut(20));

        balanceLabel.setFont(new Font("Arial", Font.BOLD, 22));
        balanceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(balanceLabel);
        infoPanel.add(Box.createVerticalStrut(20));

        roundBetLabel.setFont(new Font("Arial", Font.BOLD, 22));
        roundBetLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(roundBetLabel);

        foldButton.addActionListener(e -> {
            submitMove(new PlayerMove(PlayerAction.FOLD, 0));
        });

        callButton.addActionListener(e -> {
            submitMove(new PlayerMove(PlayerAction.CALL, 0));
        });
        raiseButton.addActionListener(e -> {
            Integer raiseTo = game.askForValidInt("Raise to how much?", game.currentTargetBet + 1, game.players.get(game.activePlayerIndex).playerBalance + game.players.get(game.activePlayerIndex).roundBet);
            if (raiseTo != null) {
            submitMove(new PlayerMove(PlayerAction.RAISE,raiseTo));
        } 
        });
        setVisible(true);
    }

    public JLabel getCardLabel(Card card) {
        URL imageUrl = getClass().getResource("/cards/" + card.toString() + ".png");
        ImageIcon icon = new ImageIcon(imageUrl);
        Image scaledImage = icon.getImage().getScaledInstance(175, 245, Image.SCALE_SMOOTH);
        JLabel cardLabel = new JLabel(new ImageIcon(scaledImage));
        return cardLabel;
    }
    public void updateMiddleCards(Middle middle, int uncovertCarts){
        comunutyCardsPanel.removeAll();
            for (int i = 0; i < middle.middleCards.size(); i++){
            
                if (uncovertCarts > 0){
                Card comunityCard = middle.middleCards.get(i);
                JLabel cardLabel = getCardLabel(comunityCard);
                comunutyCardsPanel.add(cardLabel);
                uncovertCarts--;
                }  
                else {
                    URL imageUrl = getClass().getResource("/cards/BACK.png");
                    ImageIcon icon = new ImageIcon(imageUrl);
                    Image scaledImage = icon.getImage().getScaledInstance(175, 245, Image.SCALE_SMOOTH);
                    JLabel cardLabel = new JLabel(new ImageIcon(scaledImage));
                    comunutyCardsPanel.add(cardLabel);
                }
            }

        comunutyCardsPanel.revalidate();
        comunutyCardsPanel.repaint();
    }
    public void updateActivePlayerCards(Player player){
        activePlayerCardsPanel.removeAll();
        for (int i = 0; i < player.handCards.size(); i++){
            Card playerCard = player.handCards.get(i);
            JLabel cardLabel = getCardLabel(playerCard);
            activePlayerCardsPanel.add(cardLabel);
        }
        activePlayerCardsPanel.revalidate();
        activePlayerCardsPanel.repaint();
    }
    public void updatePlayerLabel(Player player){
        playerLabel.setText(player.name+":");
    }

    public synchronized PlayerMove waitForMove() {

        selectedMove = null;

        while (selectedMove == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return selectedMove;
    }

    private synchronized void submitMove(PlayerMove move) {

        System.out.println("Move submitted: " + move.getAction());//Test

        selectedMove = move;

        notifyAll();
    }

    public void updateInfo(Player player){

        potLabel.setText("Pot: $" + game.middle.gamePott);
        if (game.currentTargetBet > 0){
            for (Player playerWithHighestBet:game.players){
                if (playerWithHighestBet.isLastRaiser)
                    targetBetLabel.setText("Highest Bet: $" + game.currentTargetBet + " by "+ playerWithHighestBet.name);
            }
        }
        else{
        targetBetLabel.setText("Current highest Bet: $0");
        }
        balanceLabel.setText("Balance: $" + player.playerBalance);
        roundBetLabel.setText("Your current Bet: $" + player.roundBet);
    }
}
