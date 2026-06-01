import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Game {
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
        players = new ArrayList<Player>();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void startGame() {
        Integer numberOfPlayersInteger = askForValidInt("Wie viele Spieler sollen teilnehmen? (2-10)", 2, 10);
        if (numberOfPlayersInteger == null) {
            System.exit(0);
        }
        numberOfPlayers = numberOfPlayersInteger;
        String uebersichtPlayers = "Die Spieler: ";
        for (int i = 0; i < numberOfPlayers; i++) {
            String playerName = JOptionPane.showInputDialog(null,"Spieler " + (i + 1) + ", wie ist dein Name?");
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
        int antwort = JOptionPane.showConfirmDialog(null,uebersichtPlayers,"Kontrolle",JOptionPane.OK_CANCEL_OPTION);
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
        for (int i = 0; i < numberOfPlayers; i++) {
            players.get(i).takeCards(deck);
        }
        players.get(smallBlindIndex).playerBalance -= 100;
        players.get(smallBlindIndex).roundBet = 100;
        middle.gamePott += 100;

        players.get(bigBlindIndex).playerBalance -= 200;
        players.get(bigBlindIndex).roundBet = 200;
        middle.gamePott += 200;
        players.get(bigBlindIndex).isBigBlind = true;

        betRound();
        middle.revealCard(3);
        betRound();
        middle.revealCard(1);
        betRound();
        middle.revealCard(1);
        betRound();
        showdown();
        askForContinuation();
    }

    public void showdown(){
        Player p = determinedWinner(middle);
        for (Player player:playersInRound){
            JOptionPane.showConfirmDialog(null, player.name + "has a "+ player.bestHand);
        }
        JOptionPane.showConfirmDialog(null, p.name + "wins $"+middle.gamePott);
        p.playerBalance += middle.gamePott;
    }

    public void betRound(){
        main: while(true){
            removeFromRound = new ArrayList<>();
            for(Player p:playersInRound){
                if (p.isBigBlind){
                    PlayerMove move = askPlayerMove(p);
                    handleAction(p, move);
                    p.isBigBlind = false;
                }
                else if (!p.isLastRaiser && !p.hasChecked){
                    PlayerMove move = askPlayerMove(p);
                    handleAction(p, move);
                }
          
                else{
                    break main;
                }
            }
            playersInRound.removeAll(removeFromRound);
        }
        for (Player p : playersInRound){
            p.roundBet = 0;
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

                break;
        }
    }

    private PlayerMove askPlayerMove(Player player) {

        String[] options = { "Fold", "Call", "Raise" };

        int choice = JOptionPane.showOptionDialog(
                null,
                player.name + "'s turn",
                "Action",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[1]);

        switch (choice) {

            case 0:
                return new PlayerMove(PlayerAction.FOLD, 0);

            case 1:
                return new PlayerMove(PlayerAction.CALL, 0);

            case 2:

                int raiseTo = askForValidInt(
                        "Raise to how much?",
                        currentTargetBet + 1,
                        player.playerBalance + player.roundBet);

                return new PlayerMove(PlayerAction.RAISE, raiseTo);

            default:
                return new PlayerMove(PlayerAction.FOLD, 0);
        }
    }
    

    public Player determinedWinner(Middle middle) {
        Player winner = playersInRound.get(0);

        winner.evaluate(middle.getMiddleCards()); // evaluate best hand of first player

        for (int i = 1; i < playersInRound.size(); i++) {
            Player playerToEvaluate = playersInRound.get(i);
            playerToEvaluate.evaluate(middle.getMiddleCards());

            int winnerHandValue = winner.evaluate(middle.getMiddleCards()).getHandValue();
            int currentHandValue = playerToEvaluate.evaluate(middle.getMiddleCards()).getHandValue();

            if (currentHandValue > winnerHandValue) {
                winner = playerToEvaluate;
            } else if (currentHandValue == winnerHandValue) {
                int[] winnerTiebreak = winner.getTiebreakValues();
                int[] playerToEvaluateTiebreak = playerToEvaluate.getTiebreakValues();
                if (playerToEvaluate.winsTiebreak(winnerTiebreak, playerToEvaluateTiebreak)) {
                    winner = playerToEvaluate;
                }
            }
        }
        return winner;
    }

    public void askForContinuation(){
        stoppedPlaying = new ArrayList<>();
        for (Player p:players){
            if(p.hasStoppedPlaying()){
                stoppedPlaying.add(p);
                JOptionPane.showConfirmDialog(null, "Thanks for playing");
            }
        }
        players.removeAll(stoppedPlaying);
        if(players.size() >= 2){
            numberOfPlayers = players.size();
            startRound();
        }
        else{
            JOptionPane.showConfirmDialog(null, "Game Over");
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
                } else {JOptionPane.showMessageDialog(null,"Ungültige Eingabe. Bitte geben Sie eine Zahl zwischen " + min + " und " + max + " ein.","Fehler",JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,"Ungültige Eingabe. Bitte geben Sie eine Zahl ein.","Fehler",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}