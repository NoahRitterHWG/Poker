import java.util.ArrayList;

public class Middle {
    public ArrayList<Card> middleCards;
    int uncovertCarts;
    int gamePott;
  
    Middle(){
    this.uncovertCarts = 0;
    middleCards = new ArrayList<>();
    this.gamePott=0;
    }

    public void takeCards(Deck deck){   //Karten fÃ¼r die Mitte werden ausgesucht
        for(int i=0; i<5; i++){
            middleCards.add(deck.getCards().get(0));
            deck.getCards().remove(0);
            System.out.println("Middle:" + middleCards.get(i).toString()); // Test
            
        }
    }
    public ArrayList<Card> getMiddleCards(){
        return middleCards;
    }
}