/**
 * The four suits of the card deck.
 *
 * @author Nouri Ayadhi, Noah Ritter
 * @version 1.0
 */
public enum CardType {

    CLUBS('C'), HEARTS('H'), DAIMONDS('D'), SPADES('S');
    private final char CardTypeChar;

    CardType (char CardTypeChar) {
        this.CardTypeChar = CardTypeChar;
    }

    /**
     * Returns the character representing this suit (for example {@code 'C'} for Clubs).
     *
     * @return the suit character
     */
    public char getCardTypeChar(){
        return this.CardTypeChar;
    }
}