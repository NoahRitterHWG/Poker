import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.JOptionPane;

/**
 * Controls the main game flow of a Texas Hold'em poker game.
 * Manages rounds, betting, side pots, and winner determination.
 * <p>
 * {@code startGame}, {@code startRound}, and {@code betRound} were developed
 * jointly by Nouri Ayadhi and Noah Ritter. All other methods written
 * by Nouri Ayadhi.
 * </p>
 *
 * @author Nouri Ayadhi, Noah Ritter
 * @version 1.0
 */
public class Game {
    /** The graphical interface for this game.*/
    GameGUI gui;
    /** The current bet amount all players must match.*/
    int currentTargetBet;
    /** The total number of players in the game. */
    int numberOfPlayers;
    /** The index of the currently active player.*/
    int activePlayerIndex;
    /** The index of the player with the small blind.*/
    int smallBlindIndex;
    /** The index of the player with the big blind. */
    int bigBlindIndex = 1;
    /** Whether the current round ended early (e.g. all but one player folded).*/
    boolean roundEndedEarly = false;
    /** All players in the game. */
    public ArrayList<Player> players;
    /** Players still active in the current round. */
    ArrayList<Player> playersInRound;
    /** Players who chose to stop playing after the round. */
    ArrayList<Player> stoppedPlaying;
    /** Players to be removed from the current round after a betting phase. */
    ArrayList<Player> removeFromRound;
    /** The community cards and pot for the current round. */
    Middle middle;
    /**
     * Creates a new game and initializes the GUI and player list.
     */
    Game() {
        gui = new GameGUI(this);
        players = new ArrayList<Player>();

    }
    
    /**
     * Starts the game by asking for the number of players and their names,
     * then launches the first round.
     *
     * @author Nouri Ayadhi, Noah Ritter
     */
    public void startGame() {
        Integer numberOfPlayersInteger = askForValidInt("Wie viele Spieler sollen teilnehmen? (2-6)", 2, 6);
        if (numberOfPlayersInteger == null) {
            System.exit(0);
        }
        numberOfPlayers = numberOfPlayersInteger;
        String uebersichtPlayers = "The Players: ";
        for (int i = 0; i < numberOfPlayers; i++) {
            String playerName;
            while (true) {
                playerName = JOptionPane.showInputDialog(null,"Player " + (i + 1) + ", what's your name?");

                if (playerName == null) {
                    players.clear();
                    this.startGame();
                    return;
                }
                playerName = playerName.trim();
                if (playerName.isBlank()) {
                    JOptionPane.showMessageDialog(null,"Name cannot be empty!","Invalid Input",JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                
                if (playerName.length() > 8) {
                    JOptionPane.showMessageDialog(null,"Name must be 8 characters or less!","Invalid Input",JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                break; 
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

    /**
     * Sets up and runs a full round of the game.
     * Deals cards, gives out blinds, runs all four betting phases, and triggers the showdown.
     * Ends early if all but one player folds.
     *
     * @author Nouri Ayadhi, Noah Ritter
     */
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
            p.playerBalanceAtStartOfRound = p.playerBalance;
            p.takeCards(deck);
            System.out.println(p.name+" has "+p.handCards.get(0).toString()+" and "+p.handCards.get(1).toString()); //Test
        }
        players.get(smallBlindIndex).playerBalance -= 100;
        players.get(smallBlindIndex).roundBet = 100;
        players.get(smallBlindIndex).totalContribution += 100;
    
        
        System.out.println(players.get(smallBlindIndex).name);// Test
  
        middle.gamePott += 100;

        players.get(bigBlindIndex).playerBalance -= 200;
        players.get(bigBlindIndex).roundBet = 200;
        players.get(bigBlindIndex).totalContribution += 200;
        middle.gamePott += 200;
        players.get(bigBlindIndex).isBigBlind = true;
        
        System.out.println(players.get(bigBlindIndex).name);// Test
        
        roundEndedEarly = false;
        betRound();    
        if (roundEndedEarly) {
            askForContinuation();
            return;
        }
        
        //test
        System.out.println("1");// Test
        gui.updateMiddleCards(middle, 3);
        betRound();
        if (roundEndedEarly) {
            askForContinuation();
            return;
        }
        System.out.println("2");// Test
        gui.updateMiddleCards(middle, 4);
        betRound();
        if (roundEndedEarly) {
            askForContinuation();
            return;
        }
        System.out.println("3");// Test
        gui.updateMiddleCards(middle, 5);
        betRound();
        if (roundEndedEarly) {
            askForContinuation();
            return;
        }
        System.out.println("4");// Test
        showdown();
        askForContinuation();
    }

    /**
     * Runs the showdown at the end of a round.
     * Evaluates all remaining players' hands, determines the winner(s)
     * per side pot, distributes chips, and shows the results.
     */
    public void showdown() {

        for (Player player : playersInRound) {
            player.evaluate(middle.getMiddleCards());
        }

        gui.showShowdown(playersInRound,new ArrayList<>(),false);
        gui.setContinueButtonText("Reveal Winner");
        gui.enterShowdownMode();
        gui.waitForContinue();

        ArrayList<SidePot> pots = createSidePots();
        ArrayList<Player> winners = new ArrayList<>();

        for (SidePot pot : pots) {

            Player winner = determinedWinner(middle,pot.eligiblePlayers);

            winners.add(winner);

            winner.playerBalance += pot.amount;
            winner.totalRoundWinnings += pot.amount;
        }

        gui.showShowdown(playersInRound,winners,true);

        gui.setContinueButtonText("Continue");

        gui.waitForContinue();

        gui.exitShowdownMode();
        gui.showPlayerView();

        for (Player p : players) {

            if (p.totalRoundWinnings != 0) {

                int profit = p.playerBalance- p.playerBalanceAtStartOfRound;

                if (profit > 0) {
                    JOptionPane.showMessageDialog(null,p.name + " wins $" + profit);
                } else {
                    JOptionPane.showMessageDialog(null,p.name + " regains $"+ (p.totalContribution + profit));
                }
            }
        }
    }

    /**
     * Runs a single betting phase. Asks each active player for their move
     * in order until all players have acted. Ends the round early if only
     * one player remains.
     *
     * @author Nouri Ayadhi, Noah Ritter
     */
    public void betRound(){
        main: while(true){
          
            removeFromRound = new ArrayList<>();
            for(Player p:playersInRound){
                if(playersInRound.size()-1 <= removeFromRound.size()){
                    playersInRound.removeAll(removeFromRound);
                    Player winner = playersInRound.get(0);
                    winner.playerBalance += middle.gamePott;
                    winner.totalRoundWinnings += middle.gamePott;
                    roundEndedEarly = true;
                    return;
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
    
    /**
     * Handles folding, calling, and raising including all-in situations.
     *
     * @param player the player who is acting
     * @param move   the move the player made
     */
    public void handleAction(Player player, PlayerMove move) {

        switch (move.getAction()) {

            case FOLD:
                removeFromRound.add(player);
                break;

            case CALL:
                int amountNeeded = currentTargetBet - player.roundBet;

                if (amountNeeded >= player.playerBalance) {
                    amountNeeded = player.playerBalance;
                    player.isAllIn = true;
                }

                player.playerBalance -= amountNeeded;
                player.roundBet += amountNeeded;
                player.totalContribution += amountNeeded;
                middle.gamePott += amountNeeded;
                player.hasChecked = true;

                break;

            case RAISE:

                int newTotalBet = move.getRaiseAmount();

                int amountToAdd = newTotalBet - player.roundBet;
                if (amountToAdd>=player.playerBalance){
                    amountToAdd = player.playerBalance;
                    player.isAllIn = true;
                }

                player.playerBalance -= amountToAdd;
                player.roundBet += amountToAdd;
                player.totalContribution += amountToAdd;
                middle.gamePott += amountToAdd;

                currentTargetBet = newTotalBet;
                newLastRaiser(player);
                noOneHasChecked();
                gui.updatePlayersPanel();

                break;
        }
    }

    /**
     * Updates the GUI for the given player and waits for their move input.
     *
     * @param player the player whose turn it is
     * @return the {@link PlayerMove} chosen by the player
     */
    private PlayerMove askPlayerMove(Player player) {

        System.out.println("Waiting for move from " + player.name);//test

        activePlayerIndex = players.indexOf(player);

        gui.updatePlayerLabel(player);
        gui.updateActivePlayerCards(player);
        gui.updateInfo(player);

        return gui.waitForMove();
    }

    /**
     * Determines the winner from a list of candidates by comparing hand types
     * and tiebreak values.
     *
     * @param middle     the community cards used for evaluation
     * @param candidates the players competing for this pot
     * @return the winning {@link Player}
     */
    public Player determinedWinner(Middle middle, ArrayList<Player> candidates) {
        Player winner = candidates.get(0);

        winner.evaluate(middle.getMiddleCards()); // evaluate best hand of first player

        for (int i = 1; i < candidates.size(); i++) {
            Player playerToEvaluate = candidates.get(i);
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

    /**
     * Asks each player if they want to continue after a round ends.
     * Removes players who quit and starts a new round, or ends the game
     * if fewer than 2 players remain.
     */
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

    /**
     * Resets all players' state for a new round.
     */
    public void resetplayers(){
        for (Player p:players){
            p.resetplayer();
        }
    }
    
    /**
     * Sets the given player as the last raiser and clears that flag for all others.
     *
     * @param player the player who just raised
     */
    public void newLastRaiser(Player player){
      for (Player p:players){
        p.isLastRaiser = false;
      }
      player.isLastRaiser = true;
    }
    
    /**
     * Resets the checked state for all players.
     */
    public void noOneHasChecked(){
        for (Player p:players){
            p.hasChecked = false;
        }
    }
    
    /**
     * Prompts the user for an integer input within a given range.
     * Repeats until a valid value is entered or the dialog is cancelled.
     *
     * @param message the prompt shown to the user
     * @param min     the minimum accepted value
     * @param max     the maximum accepted value
     * @return the entered integer, or {@code null} if the user cancelled
     */
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

    /**
     * Creates side pots based on each player's total contribution to the pot.
     * Used when one or more players are all-in with different amounts.
     *
     * @return a list of {@link SidePot} objects, each with an amount and eligible
     *         players
     */
    public ArrayList<SidePot> createSidePots() {

    ArrayList<SidePot> pots = new ArrayList<>();
    ArrayList<Integer> levels = new ArrayList<>();

    for (Player p : players) {
        if (p.totalContribution > 0 &&!levels.contains(p.totalContribution)) {
            levels.add(p.totalContribution);
        }
    }

    Collections.sort(levels);

    int previousLevel = 0;

    for (int level : levels) {

        ArrayList<Player> eligiblePlayers = new ArrayList<>();
        int contoributers = 0;

        for (Player p : players) {
            if (p.totalContribution >= level) {
                contoributers++;
                if (playersInRound.contains(p)){
                    eligiblePlayers.add(p);
                    
                }
            }
        }

        int potAmount =
            (level - previousLevel) * contoributers;

        pots.add(new SidePot(potAmount,new ArrayList<>(eligiblePlayers)));
        previousLevel = level;
        System.out.print("Eligible Players for $" +potAmount+" pot: ");
        for (Player test: eligiblePlayers){
            System.out.print(test.name);
        }
        
        
    }
    return pots;
}
}