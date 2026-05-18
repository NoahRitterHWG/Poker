import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class GameGUI extends JFrame {

Game game;
static final Color greenBackground = new Color(55, 100, 80);
int boardWidth = 1000;
int boardHeight = boardWidth;
boolean dealerHideCard;
JPanel gamePanel = new JPanel();
JPanel buttonPanel = new JPanel();
JPanel activePlayerCardsPanel = new JPanel();
JPanel dealerCardsPanel = new JPanel();
JPanel dealerPanel = new JPanel();
JPanel playerPanel = new JPanel();
JButton hitButton = new JButton("Hit");
JButton stayButton = new JButton("Stay");
JLabel dealerLabel = new JLabel("Dealer: \n ");
JLabel playerLabel = new JLabel("");


GameGUI(Game game){
    super("BlackJack");
    this.game = game;
    
    
    setSize(boardWidth, boardHeight);
    setLocationRelativeTo(null);
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    gamePanel.setLayout(new BorderLayout());
    gamePanel.setBackground(greenBackground);
    add(gamePanel);

    buttonPanel.setBackground(greenBackground);
    hitButton.setFocusable(false);
    stayButton.setFocusable(false);
    buttonPanel.add(hitButton);
    buttonPanel.add(stayButton);
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

    dealerPanel.setLayout(new BoxLayout(dealerPanel, BoxLayout.Y_AXIS));
    dealerPanel.setBackground(greenBackground);
    gamePanel.add(dealerPanel, BorderLayout.NORTH);

    dealerLabel.setFont(new Font("Arial", Font.BOLD, 30));
    dealerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
    dealerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    dealerPanel.add(dealerLabel); 

    dealerCardsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    dealerCardsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
    dealerCardsPanel.setBackground(greenBackground);
    dealerPanel.add(dealerCardsPanel);

    hitButton.addActionListener(e -> {
        Card card = game.cards.deal();
        game.players.get(game.activePlayerIndex).hit(card);
        updateActivePlayerCards(game.players.get(game.activePlayerIndex));
        if(game.players.get(game.activePlayerIndex).hand.isBust()){
            JOptionPane.showMessageDialog(null, "Your turn has ended since you went over 21");
            game.activePlayerIndex +=1;
            if(game.activePlayerIndex >= game.players.size()){
                game.activePlayerIndex = 0;
                game.dealerTurn();
                game.determineWinner();
                game.resetRound(); 
                if (!game.players.isEmpty()){
                game.betTurn();
                game.dealCards();
                updateDealerCards(game.dealer);
                updateActivePlayerCards(game.players.get(game.activePlayerIndex));
                updatePlayerLabel();    
                }
                else {
                    JOptionPane.showMessageDialog(null, "Game over!");
                    System.exit(0);
                }
            }
            else{
                updateActivePlayerCards(game.players.get(game.activePlayerIndex));
                updatePlayerLabel();
            }
        }
    });

    stayButton.addActionListener(e -> {
        JOptionPane.showMessageDialog(null, "Your turn has ended.");
        game.activePlayerIndex +=1;
        if (game.activePlayerIndex >= game.players.size()){
            game.activePlayerIndex = 0;
            game.dealerTurn();  
            game.determineWinner();
            game.resetRound();
            if (!game.players.isEmpty()) {
                game.betTurn();
                game.dealCards();
                updateDealerCards(game.dealer);
                updateActivePlayerCards(game.players.get(game.activePlayerIndex));
                updatePlayerLabel();
            } else {
                JOptionPane.showMessageDialog(null, "Game over!");
                System.exit(0);
            }
        }
        else {
            updateActivePlayerCards(game.players.get(game.activePlayerIndex));
            updatePlayerLabel();
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
public void updateDealerCards(Dealer dealer){
    dealerCardsPanel.removeAll();
        for (int i = 0; i < dealer.hand.handCards.size(); i++){
            if(i == 0 && dealerHideCard){
                URL imageUrl = getClass().getResource("/cards/BACK.png");
                ImageIcon icon = new ImageIcon(imageUrl);
                Image scaledImage = icon.getImage().getScaledInstance(175, 245, Image.SCALE_SMOOTH);
                JLabel cardLabel = new JLabel(new ImageIcon(scaledImage));
                dealerCardsPanel.add(cardLabel);
            }
            else{
            Card dealerCard = dealer.hand.handCards.get(i);
            JLabel cardLabel = getCardLabel(dealerCard);
            dealerCardsPanel.add(cardLabel);
            }  
        }

    dealerCardsPanel.revalidate();
    dealerCardsPanel.repaint();
}
public void updateActivePlayerCards(Player player){
    activePlayerCardsPanel.removeAll();
    for (int i = 0; i < player.hand.handCards.size(); i++){
        Card playerCard = player.hand.handCards.get(i);
        JLabel cardLabel = getCardLabel(playerCard);
        activePlayerCardsPanel.add(cardLabel);
    }
    activePlayerCardsPanel.revalidate();
    activePlayerCardsPanel.repaint();
}
public void updatePlayerLabel(){
    playerLabel.setText(game.players.get(game.activePlayerIndex).name+":");
}
}
