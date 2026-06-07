import java.util.ArrayList;

/**
 * Manages the five community cards and the pot for a round.
 *
 * @author Noah Ritter
 * @version 1.0
 */
public class Middle {
    /** The five community cards on the table. */
    public ArrayList<Card> middleCards;
    /** Current amount of chips in the pot. */
    int gamePott;
  
    Middle(){
    middleCards = new ArrayList<>();
    this.gamePott=0;
    }
    
    /**
     * Draws the first five cards from the deck and places them on the table.
     *
     * @param deck the deck to draw from
     */
    public void takeCards(Deck deck){   
        for(int i=0; i<5; i++){
            middleCards.add(deck.getCards().get(0));
            deck.getCards().remove(0);
            System.out.println("Middle:" + middleCards.get(i).toString()); // Test
            
        }
    }
    
    /**
     * Returns the community cards on the table.
     *
     * @return list of community cards
     */
    public ArrayList<Card> getMiddleCards(){
        return middleCards;
    }
}