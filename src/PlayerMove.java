/**
 * A single move made by a player, consisting of an action and an
 * optional raise amount.
 *
 * @author Nouri Ayadhi
 * @version 1.0
 */
public class PlayerMove {
    /** The action the player chose (Fold, Call, or Raise). */
    private PlayerAction action;
     /** The total bet amount if the action is a raise, otherwise 0. */
    private int raiseAmount;

    /**
     * Creates a new player move with the given action and raise amount.
     *
     * @param action      the chosen {@link PlayerAction}
     * @param raiseAmount the total bet amount for a raise, or 0 for other actions
     */
    public PlayerMove(PlayerAction action, int raiseAmount) {
        this.action = action;
        this.raiseAmount = raiseAmount;
    }

    /**
     * Returns the action of this move.
     *
     * @return the {@link PlayerAction}
     */
    public PlayerAction getAction() {
        return action;
    }

    /**
     * Returns the raise amount of this move.
     *
     * @return the raise amount, or 0 if the action is not a raise
     */
    public int getRaiseAmount() {
        return raiseAmount;
    }
}
