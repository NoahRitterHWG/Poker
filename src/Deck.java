import java.util.ArrayList;
import java.util.Random;

/**
 * Represents a full deck of 52 playing cards.
 * The deck is built automatically when created and can be shuffled before use.
 *
 * @author Nouri Ayadhi, Noah Ritter
 * @version 1.0
 */
public class Deck {

    public ArrayList<Card> cards;
    Deck(){

    cards = new ArrayList<Card>();
    /**
     * Creates a new deck containing all 52 cards (4 suits × 13 values).
     */
    for (CardType type : CardType.values()){
        for(CardValues value : CardValues.values()){
            Card card = new Card(type, value); 
            cards.add(card);  

            }

        }
    }
    
    /**
     * Returns the list of cards currently in the deck.
     *
     * @return the deck's card list
     */
    public ArrayList<Card> getCards() {
        return cards;
    }
    
    /**
     * Shuffles the deck randomly 
     */
    public void shuffledeck(){
    Random random = new Random();
    for (int i = 0; i < cards.size(); i++){
        int j = random.nextInt(cards.size());
        Card currentCard = cards.get(i);
        Card randomCard = cards.get(j);
        cards.set(i, randomCard);
        cards.set(j, currentCard);
        }
    }
}

        
