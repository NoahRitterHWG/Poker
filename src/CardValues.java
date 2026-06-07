/**
 * All possible card values in a standard deck.
 * Each value has a number used to compare cards during the game.
 * The Ace has the highest value (12), but can also count as 1 in a straight.
 *
 * @author Nouri Ayadhi
 * @version 1.0
 */
public enum CardValues {
    A(12),
    TWO(0), 
    THREE(1),
    FOUR(2),
    FIVE(3), 
    SIX(4), 
    SEVEN(5), 
    EIGHT(6), 
    NINE(7), 
    TEN(8), 
    J(9), 
    Q(10), 
    K(11);

    private final int CardValue;

    CardValues(int CardValue){
        this.CardValue = CardValue;
    }
   
    /**
     * Returns the value of this card.
     *
     * @return the card's value
     */ 
    public int getCardValue(){
        return this.CardValue;
    }


}
