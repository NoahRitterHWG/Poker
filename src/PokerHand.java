/**
 * All possible poker hand types, ordered from weakest (0) to strongest (8).
 * The value is used to compare hands during winner determination.
 *
 * @author Nouri Ayadhi
 * @version 1.0
 */
public enum PokerHand {

    HIGHCARD(0, "High Card"),
    ONE_PAIR(1, "One Pair"),
    TWO_PAIR(2, "Two Pair"),
    THREE_OF_A_KIND(3, "Three of a Kind"),
    STRAIGHT(4, "Straight"),
    FLUSH(5, "Flush"),
    FULL_HOUSE(6, "Full House"),
    FOUR_OF_A_KIND(7, "Four of a Kind"),
    STRAIGHT_FLUSH(8, "Straight Flush");

    private final int handValue;
    private final String displayName;

    /**
     * Creates a {@code PokerHand} with the given value and display name.
     *
     * @param handValue   the numeric strength of this hand (higher is better)
     * @param displayName the name shown in the UI
     */
    PokerHand(int handValue, String displayName) {
        this.handValue = handValue;
        this.displayName = displayName;
    }

    /**
     * Returns the strength value of this hand.
     *
     * @return the hand value
     */
    public int getHandValue() {
        return handValue;
    }

    /**
     * Returns the display name of this hand.
     *
     * @return the hand name as a string
     */
    @Override
    public String toString() {
        return displayName;
    }
}