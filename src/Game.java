import java.util.ArrayList;

import javax.swing.JOptionPane;


public class Game {

    Game() {
        Gui = new GameGUI(this);
        startGame();
        Gui.updatePlayerLabel();
        betTurn();
        dealCards();
        Gui.updateDealerCards(dealer);
        Gui.updateActivePlayerCards(players.get(activePlayerIndex));
        
    }

    GameGUI Gui;
    Deck cards = new Deck();
    Dealer dealer;
    ArrayList<Player> players;
    ArrayList<Player> stoppedPlaying;
    ArrayList<Card> comuniyCards;
    int activePlayerIndex = 0;

    public void startGame() {
        this.dealer = new Dealer();
        players = new ArrayList<Player>();
        int numbplayers = getValidInt("How many players are there? (1-5)", 1,5);
        int i = 0;
        while (i < numbplayers){
            int j = i+1;
            String name = JOptionPane.showInputDialog("Player " +j+", what is your name?");
            if (name != null){
                players.add(new Player(name));
                i++;
            }
            else{
                int confirm = JOptionPane.showConfirmDialog(null, "Do you realy want to end the game?");
                if(confirm == JOptionPane.YES_OPTION){
                        System.exit(0);
                    }
                    else{
                        continue;
                    }
                }
        }
    }

    public void betTurn() {
        for (int i = 0; i < players.size(); i++) {
            int bet = getValidInt(players.get(i).name + ", your balance is: " + players.get(i).playerBalance + "€\nHow much do you want to bet?", 1, players.get(i).playerBalance);
            players.get(i).bet(bet); 
        }
    }

    public void dealCards() {
        cards.shuffledeck();
        for (int i = 0; i < players.size(); i++) {
            players.get(i).hit(cards.deal());
            players.get(i).hit(cards.deal());
        }
        dealer.hit(cards.deal());
        dealer.hit(cards.deal());
        Gui.dealerHideCard = true;
    }

    public void dealerTurn() {
        Gui.dealerHideCard = false;
        Gui.updateDealerCards(dealer);
        while (dealer.shouldHit()) {
            dealer.hit(cards.deal());
            Gui.updateDealerCards(dealer);
        }
    }

    public void determineWinner() {
        stoppedPlaying = new ArrayList<Player>();
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).hand.getTotalHandValue() > 21) {
                JOptionPane.showMessageDialog(null, players.get(i).name+", you lose "+ players.get(i).bet+"$ since you went over 21.\n Your balance now is: "+players.get(i).playerBalance+"$");
                
            } 
            else if (dealer.hand.getTotalHandValue() > 21) {
                players.get(i).playerBalance += players.get(i).bet * 2;
                JOptionPane.showMessageDialog(null, players.get(i).name + ", you win " + players.get(i).bet + "$ since the dealer went over 21.\n Your balance now is: " + players.get(i).playerBalance + "$");
            } 
            else if (dealer.hand.getTotalHandValue() > players.get(i).hand.getTotalHandValue()) {
                JOptionPane.showMessageDialog(null, players.get(i).name + ", you lose " + players.get(i).bet + "$ since the dealer beat you.\n Your balance now is: " + players.get(i).playerBalance + "$");
            } 
            else if (players.get(i).hand.getTotalHandValue() == 21 && players.get(i).hand.handCards.size() == 2) {
                players.get(i).playerBalance += (int) (players.get(i).bet * 2.5);
                JOptionPane.showMessageDialog(null, "Congrats "+players.get(i).name+", you got a BlackJack!\n You win "+ players.get(i).bet*2 +"$\n Your balance now is: " + players.get(i).playerBalance + "$");
            } 
            else if (dealer.hand.getTotalHandValue() < players.get(i).hand.getTotalHandValue()) {
                players.get(i).playerBalance += players.get(i).bet * 2;
                JOptionPane.showMessageDialog(null, players.get(i).name + ", you win " + players.get(i).bet + "$ since you beat the dealer.\n Your balance now is: " + players.get(i).playerBalance + "$");
            } 
            else {
                players.get(i).playerBalance += players.get(i).bet;
                JOptionPane.showMessageDialog(null, players.get(i).name + ", you tied the dealer! \n Your balance now is: " + players.get(i).playerBalance + "$");
            }
            if (players.get(i).playerBalance < 1){
                JOptionPane.showMessageDialog(null, "You've ran out of money! \n Thanks for playing!");
                stoppedPlaying.add(players.get(i));
            }
            else{
            int continueplaying = JOptionPane.showConfirmDialog(null, "Do you want to continue playing?");
            if(continueplaying != JOptionPane.YES_OPTION){
                stoppedPlaying.add(players.get(i));
                JOptionPane.showMessageDialog(null, "Thanks for playing!");
            }
            }
        }
    }

    public void resetRound() {
        players.removeAll(stoppedPlaying);
        for (int i = 0; i < players.size(); i++) {
            players.get(i).bet = 0;
            players.get(i).hand.handCards.clear();
        }
        dealer.hand.handCards.clear();
        cards = new Deck();
    }
    
    public int getValidInt(String message, int min, int max) {
        while (true) {
            try {
                String input = JOptionPane.showInputDialog(message);
                if(input != null){
                    int value = Integer.parseInt(input);
                    if (value >= min && value <= max){
                        return value;
                    }
                    JOptionPane.showMessageDialog(null, "Please enter a number between " + min + " and " + max);
                }
                else {
                    int confirm = JOptionPane.showConfirmDialog(null, "Do you realy want to end the game?");

                    if(confirm == JOptionPane.YES_OPTION){
                        System.exit(0);
                    }
                    else{
                        continue;
                    }
                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number!");
            }
        }
    }
}