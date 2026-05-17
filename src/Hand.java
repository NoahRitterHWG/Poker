import java.util.ArrayList;

public class Hand {
 ArrayList<Card> handCards;

 Hand(){
    handCards = new ArrayList<Card>();
 }
 
 public void pullCard(Card card){
    handCards.add(card);
 }
 public int getTotalHandValue(){
    int aceCount = 0;
    int totalValue = 0;
    for (int i = 0; i < handCards.size(); i++){
        Card cardInHand = handCards.get(i);
        totalValue += cardInHand.getValue();
        if (cardInHand.value == CardValues.A){
         aceCount += 1;
        }  
    }
   while (totalValue > 21 && aceCount > 0) {
      totalValue -= 10;
      aceCount -= 1;
   }
    return totalValue;
 }
 public boolean isBust(){
    return getTotalHandValue() > 21;
 }
}
   