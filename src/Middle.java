import java.util.ArrayList;

public class Middle {
    public ArrayList<Card> middleCards;
    int uncovertCarts = 0;

    Middle(){
    middleCards = new ArrayList<>();
    }

    public void takeCards(Deck deck){
        for(int i=0; i<5; i++){
            middleCards.add(deck.getCards().get(0));
            deck.getCards().remove(0);
            System.out.println("Middle:"+middleCards.size());
        }
    }
    public ArrayList<Card> getMiddleCards(){
        return middleCards;
    }
}