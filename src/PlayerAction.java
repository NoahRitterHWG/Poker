/**
 * The possible actions a player can take during a betting round.
 *
 * @author Nouri Ayadhi
 * @version 1.0
 */
public enum PlayerAction {
    FOLD("Fold"),
    CALL("Check/Call"),
    RAISE("Raise");

    private final String label;
    
    /**
     * Constructs a {@code PlayerAction} with the given display label.
     *
     * @param label the label shown in the UI
     */
    PlayerAction(String label) {
        this.label = label;
    }
    
    /**
     * Returns the display label of this action.
     *
     * @return the action label as a string
     */
    @Override
    public String toString() {
        return label;
    }
}