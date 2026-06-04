import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JOptionPane;


public class Game {
    GameGUI gui;
    int currentTargetBet;
    int numberOfPlayers;
    int activePlayerIndex;
    int smallBlindIndex;
    int bigBlindIndex = 1;
    public ArrayList<Player> players;
    ArrayList<Player> playersInRound;
    ArrayList<Player> stoppedPlaying;
    ArrayList<Player> removeFromRound;
    Middle middle;

    Game() {
        gui = new GameGUI(this);
        players = new ArrayList<Player>();

    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void startGame() {
        Integer numberOfPlayersInteger = askForValidInt("Wie viele Spieler sollen teilnehmen? (2-6)", 2, 6);
        if (numberOfPlayersInteger == null) {
            System.exit(0);
        }
        numberOfPlayers = numberOfPlayersInteger;
        String uebersichtPlayers = "The Players: ";
        for (int i = 0; i < numberOfPlayers; i++) {
            String playerName = JOptionPane.showInputDialog(null,"Player " + (i + 1) + ", whats your name?");
            if (playerName == null) {
                players.clear();
                this.startGame();
                return;
            }
            Player player = new Player(playerName, i);
            players.add(player);
            uebersichtPlayers += player.name;
            if (i != numberOfPlayers - 1) {
                uebersichtPlayers += ", ";
            }
        }
        int antwort = JOptionPane.showConfirmDialog(null,uebersichtPlayers,"The Players",JOptionPane.OK_CANCEL_OPTION);
        if (antwort == JOptionPane.CANCEL_OPTION) {
            players.clear();
            startGame();
        } else if (antwort == JOptionPane.OK_OPTION) {
            startRound();
        }
    }

    public void startRound() {
        resetplayers();
        currentTargetBet = 200;
        Player first = players.remove(0);
        players.add(first);
        playersInRound = new ArrayList<>(players);
        smallBlindIndex = players.size()-2;
        bigBlindIndex = players.size()-1;
        Deck deck = new Deck();
        deck.shuffledeck();
        middle = new Middle();
        middle.takeCards(deck);
        gui.updateMiddleCards(middle, 0);
        for (Player p:players) {
            p.takeCards(deck);
            System.out.println(p.name+" has "+p.handCards.get(0).toString()+" and "+p.handCards.get(1).toString()); //Test
        }
        players.get(smallBlindIndex).playerBalance -= 100;
        players.get(smallBlindIndex).roundBet = 100;
        
        System.out.println(players.get(smallBlindIndex).name);// Test
  
        middle.gamePott += 100;

        players.get(bigBlindIndex).playerBalance -= 200;
        players.get(bigBlindIndex).roundBet = 200;
        middle.gamePott += 200;
        players.get(bigBlindIndex).isBigBlind = true;
        
        System.out.println(players.get(bigBlindIndex).name);// Test
        

        betRound();
        //test
        System.out.println("1");// Test
        gui.updateMiddleCards(middle, 3);
        betRound();
        System.out.println("2");// Test
        gui.updateMiddleCards(middle, 4);
        betRound();
        System.out.println("3");// Test
        gui.updateMiddleCards(middle, 5);
        betRound();
        System.out.println("4");// Test
        showdown();
        askForContinuation();
    }

    public void showdown(){
        Player p = determinedWinner(middle);
        for (Player player:playersInRound){
            gui.updatePlayerLabel(player);
            gui.updateActivePlayerCards(player);
            JOptionPane.showMessageDialog(null, player.name + " has a "+ player.bestHand);
        }
        JOptionPane.showMessageDialog(null, p.name + " wins $"+middle.gamePott);
        p.playerBalance += middle.gamePott;
    }

    public void betRound(){
        main: while(true){
          
            removeFromRound = new ArrayList<>();
            for(Player p:playersInRound){
                if(playersInRound.size()-1 <= removeFromRound.size()){
                    playersInRound.removeAll(removeFromRound);
                    showdown();
                    askForContinuation();
                }
                if (!removeFromRound.contains(p)){
                    if (p.isBigBlind){
                        PlayerMove move = askPlayerMove(p);
                        handleAction(p, move);
                        p.isBigBlind = false;
                        System.out.println("BB has acted");//Test
                    }
                    else if (!p.isLastRaiser && !p.hasChecked){
                        PlayerMove move = askPlayerMove(p);
                        handleAction(p, move);
                    }
            
                    else{
                        break main;
                }
            }
            }
            playersInRound.removeAll(removeFromRound);
        }
        playersInRound.removeAll(removeFromRound);
        for (Player p : playersInRound){
            System.out.println(p.roundBet);//Test
            p.roundBet = 0;
        }
        noOneHasChecked();
        for (Player p : players) {
            p.isLastRaiser = false;
        }
        currentTargetBet = 0;
        
    }
    
    public void handleAction(Player player, PlayerMove move) {

        switch (move.getAction()) {

            case FOLD:
                removeFromRound.add(player);
                break;

            case CALL:
                int amountNeeded = currentTargetBet - player.roundBet;

                player.playerBalance -= amountNeeded;
                player.roundBet += amountNeeded;
                middle.gamePott += amountNeeded;
                player.hasChecked = true;

                break;

            case RAISE:

                int newTotalBet = move.getRaiseAmount();

                int amountToAdd = newTotalBet - player.roundBet;

                player.playerBalance -= amountToAdd;
                player.roundBet = newTotalBet;
                middle.gamePott += amountToAdd;

                currentTargetBet = newTotalBet;
                newLastRaiser(player);
                noOneHasChecked();
                gui.updatePlayersPanel();

                break;
        }
    }

    private PlayerMove askPlayerMove(Player player) {

        System.out.println("Waiting for move from " + player.name);//test

        activePlayerIndex = players.indexOf(player);

        gui.updatePlayerLabel(player);
        gui.updateActivePlayerCards(player);
        gui.updateInfo(player);

        return gui.waitForMove();
    }
    

    public Player determinedWinner(Middle middle) {
        Player winner = playersInRound.get(0);

        winner.evaluate(middle.getMiddleCards()); // evaluate best hand of first player

        for (int i = 1; i < playersInRound.size(); i++) {
            Player playerToEvaluate = playersInRound.get(i);
            playerToEvaluate.evaluate(middle.getMiddleCards());

            int winnerHandValue = winner.bestHand.getHandValue();
            int currentHandValue = playerToEvaluate.bestHand.getHandValue();

            if (currentHandValue > winnerHandValue) {
                winner = playerToEvaluate;
            } else if (currentHandValue == winnerHandValue) {
                int[] winnerTiebreak = winner.getTiebreakValues();
                int[] playerToEvaluateTiebreak = playerToEvaluate.getTiebreakValues();
                System.out.println("current: "+Arrays.toString(winnerTiebreak));//test
                if (playerToEvaluate.winsTiebreak(winnerTiebreak, playerToEvaluateTiebreak)) {
                    winner = playerToEvaluate;
                    System.out.println("new: " + Arrays.toString(playerToEvaluateTiebreak));// test
                }
            }
        }
        
        System.out.println("final: "+ Arrays.toString(winner.getTiebreakValues())); //test
        
        return winner;
    }

    public void askForContinuation(){
        stoppedPlaying = new ArrayList<>();
        for (Player p:players){
            if(p.hasStoppedPlaying()){
                stoppedPlaying.add(p);
                JOptionPane.showMessageDialog(null, "Thanks for playing");
            }
        }
        players.removeAll(stoppedPlaying);
        if(players.size() >= 2){
            numberOfPlayers = players.size();
            startRound();
        }
        else{
            JOptionPane.showMessageDialog(null, "Game Over");
            System.exit(0);
        }
    }

    public void resetplayers(){
        for (Player p:players){
            p.resetplayer();
        }
    }
    
    public void newLastRaiser(Player player){
      for (Player p:players){
        p.isLastRaiser = false;
      }
      player.isLastRaiser = true;
    }
    public void noOneHasChecked(){
        for (Player p:players){
            p.hasChecked = false;
        }
    }
    

    public Integer askForValidInt(String message, int min, int max) {
        while (true) {
            String value = JOptionPane.showInputDialog(null, message);
            if (value == null) {
                return null;
            }
            try {
                int parsed = Integer.parseInt(value);
                if (parsed <= max && parsed >= min) {
                    return parsed;
                } else {JOptionPane.showMessageDialog(null,"Please enter a number between " + min + " and " + max + ".","Error",JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,"Please enter a valid number.","Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}