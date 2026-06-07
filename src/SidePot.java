import java.util.ArrayList;

/**
  A side pot created when one or more players are all-in.
 * Each side pot has its own amount and a list of players eligible to win it.
 *
 * @author Nouri Ayadhi
 * @version 1.0
 */
public class SidePot {

    /** The total chip amount in this side pot. */
    int amount;
    /** The players who are eligible to win this side pot. */
    ArrayList<Player> eligiblePlayers;

    /**
     * Creates a new side pot with the given amount and eligible players.
     *
     * @param amount          the total chips in this pot
     * @param eligiblePlayers the players who can win this pot
     */
    SidePot(int amount, ArrayList<Player> eligiblePlayers) {
        this.amount = amount;
        this.eligiblePlayers = eligiblePlayers;
    }
}