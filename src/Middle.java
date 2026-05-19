import java.util.ArrayList;

public class Middle {
    public ArrayList<Card> middleCards;
    int uncovertCarts = 0;

    Middle(){
    middleCards = new ArrayList<>();
    }

    public void takeCards(Deck deck){
        for(int i=0; i<5; i++){
            middleCards.add(deck.getCards().get(i));
            deck.getCards().remove(i);
        }
    }
    public ArrayList<Card> getMiddleCards(){
        return middleCards;
    }
}