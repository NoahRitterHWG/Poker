/**
 * A single playing card with a suit and a value.
 *
 * @author Nouri Ayadhi
 * @version 1.0
 */
public class Card {

    CardType type;
    CardValues value;

    /**
     * Constructs a new card with the given suit and value.
     *
     * @param type  the suit of the card
     * @param value the value of the card
     */
    Card(CardType type, CardValues value){
        this.type = type;
        this.value = value;
    }
    
    /**
     * Returns the value of this card.
     *
     * @return the card's value as an {@code int}
     */
    public int getValue() {
        return this.value.getCardValue();
    }
    
    /**
     * Returns a string representation of this card 
     * 
     * @return a string in the format {@code "<value>-<suit>"}
     */
    public String toString(){
        if(value == CardValues.A || value == CardValues.J || value == CardValues.Q || value == CardValues.K){
            return value + "-" + type.getCardTypeChar();
        }
        else{    
            return (value.getCardValue()+2) + "-" + type.getCardTypeChar();
            }
        }

}