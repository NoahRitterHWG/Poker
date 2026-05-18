import java.util.ArrayList;
import java.util.Random;

public class Deck {
    ArrayList<Card> cards;
    Deck(){
    cards = new ArrayList<Card>();

    for (CardType type : CardType.values()){
        for(CardValues value : CardValues.values()){
            Card card = new Card(type, value); 
            cards.add(card);  

            }

        }
    }
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
    public Card deal(){
        Card dealtCard = cards.get(cards.size()-1);
        cards.remove(dealtCard);
        return dealtCard;
    }
}

        
