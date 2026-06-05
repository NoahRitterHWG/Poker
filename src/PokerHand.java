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

    PokerHand(int handValue, String displayName) {
        this.handValue = handValue;
        this.displayName = displayName;
    }

    public int getHandValue() {
        return handValue;
    }

    @Override
    public String toString() {
        return displayName;
    }
}