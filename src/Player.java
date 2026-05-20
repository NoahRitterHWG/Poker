import java.util.ArrayList;
import java.util.Collections;

public class Player {
    ArrayList<Card> availableCards;
    ArrayList<Card> possebleHand;
    PokerHand bestHand;
    int playerBalance = 10000;  //Geld des Spielers
    private ArrayList<Card> handCards;
    int id;
    String name;
    Player(String name, int id) {   //Konstruktor
        this.name = name;
        handCards = new ArrayList<Card>();
        this.id = id;
    }
    public void takeCards(Deck deck){    //lässt die Spieler zwei zufällige Karten nehmen
        for(int i=0; i<2; i++){
        handCards.add(deck.getCards().get(i));
        deck.getCards().remove(i);
        }
    }
    public Card getHandCard(int index){
        Card card = handCards.get(index);
        return card;
    }

    public PokerHand evaluate(ArrayList<Card> comunityCards){              
   availableCards = new ArrayList<Card>();
   availableCards.addAll(comunityCards);
   availableCards.addAll(handCards);
   bestHand = PokerHand.HIGHCARD;                                
   
   for (int i=0; i <= 6; i++){
      for (int j = i+1; j <= 6; j++){

         possebleHand = new ArrayList<Card>(availableCards);
         possebleHand.remove(j);
         possebleHand.remove(i);
         
         if(isStraightFlush()){
            bestHand = PokerHand.STRAIGHT_FLUSH;
         }
         else if (isFourOfAKind() && PokerHand.FOUR_OF_A_KIND.getHandValue() > bestHand.getHandValue()){
            bestHand = PokerHand.FOUR_OF_A_KIND;
         }
         else if (isFullHouse() && PokerHand.FULL_HOUSE.getHandValue() > bestHand.getHandValue()){
            bestHand = PokerHand.FULL_HOUSE;
         }
         else if (isFlush() && PokerHand.FLUSH.getHandValue() > bestHand.getHandValue()){
            bestHand = PokerHand.FLUSH;
         }
         else if (isStraight() && PokerHand.STRAIGHT.getHandValue() > bestHand.getHandValue()){
            bestHand = PokerHand.STRAIGHT;
         }
         else if (isThreeOfAKind() && PokerHand.THREE_OF_A_KIND.getHandValue() > bestHand.getHandValue()){
            bestHand = PokerHand.THREE_OF_A_KIND;
         }
         else if (isTwoPair() && PokerHand.TWO_PAIR.getHandValue() > bestHand.getHandValue()){
            bestHand = PokerHand.TWO_PAIR;
         }
         else if (isOnePair() && PokerHand.ONE_PAIR.getHandValue() > bestHand.getHandValue()){
            bestHand = PokerHand.ONE_PAIR;
         }
      }
   }
   return bestHand;
 }

 public int[] getTiebrakValues(){

   if (bestHand ==  PokerHand.STRAIGHT_FLUSH){
      if (possebleHand.get(4).getValue() == 14 && possebleHand.get(3).getValue() == 5) {
         return new int[] { 5 };
      } else {
         return new int[] { possebleHand.get(4).getValue() };
      }
   }
   else if (bestHand ==  PokerHand.FOUR_OF_A_KIND){
      for (int i = 0; i < possebleHand.size(); i++) {
         int count = 0;
         for (int j = 0; j < possebleHand.size(); j++) {
            if (possebleHand.get(j).getValue() == possebleHand.get(i).getValue()) {
               count += 1;
            }
         }
         if (count == 4) {
            return new int[]{possebleHand.get(i).getValue()};
         }
      }
   }
   else if (bestHand ==  PokerHand.FULL_HOUSE){
      for (int i = 0; i < possebleHand.size(); i++) {
         int count = 0;
         for (int j = 0; j < possebleHand.size(); j++) {
            if (possebleHand.get(j).getValue() == possebleHand.get(i).getValue()) {
               count += 1;
            }
         }
         if (count == 3) {
            for (int k = 0; k < possebleHand.size(); k++) {
               count = 0;
               for (int l = 0; l < possebleHand.size(); l++) {
                  if (possebleHand.get(k).getValue() == possebleHand.get(l).getValue()) {
                     count += 1;
                  }
               }
               if (count == 2) {
                  return new int[]{possebleHand.get(i).getValue(), possebleHand.get(k).getValue()};
               }
            }
         }
      }
   }
   else if (bestHand ==  PokerHand.FLUSH){
      return new int[] {possebleHand.get(4).getValue()};
   }
   else if (bestHand ==  PokerHand.STRAIGHT){
      if (possebleHand.get(4).getValue() == 14 && possebleHand.get(3).getValue() == 5) {
         return new int[] { 5 };
      } else {
         return new int[] { possebleHand.get(4).getValue() };
      }
   }
   else if (bestHand ==  PokerHand.THREE_OF_A_KIND){
      for (int i = 0; i < possebleHand.size(); i++) {
         int count = 0;
         for (int j = 0; j < possebleHand.size(); j++) {
            if (possebleHand.get(j).getValue() == possebleHand.get(i).getValue()) {
               count += 1;
            }
         }
         if (count == 3) {
            return new int[] { possebleHand.get(i).getValue() };
         }
      }
   }
   else if (bestHand ==  PokerHand.TWO_PAIR){
      for (int i = 0; i < possebleHand.size(); i++) {
         int count = 0;
         for (int j = 0; j < possebleHand.size(); j++) {
            if (possebleHand.get(j).getValue() == possebleHand.get(i).getValue()) {
               count += 1;
            }
         }
         if (count == 2) {
            for (int k = 0; k < possebleHand.size(); k++) {
               count = 0;
               for (int l = 0; l < possebleHand.size(); l++) {
                  if (possebleHand.get(k).getValue() == possebleHand.get(l).getValue()) {
                     count += 1;
                  }
               }
               if (count == 2 && possebleHand.get(i).getValue() != possebleHand.get(k).getValue()) {
                  for (int m = 0; m < possebleHand.size(); m++){
                     if (possebleHand.get(m).getValue() != possebleHand.get(i).getValue() && possebleHand.get(m).getValue() != possebleHand.get(k).getValue())
                        return new int[] { possebleHand.get(i).getValue(), possebleHand.get(k).getValue(), possebleHand.get(m).getValue()};
                  }
               }
            }
         }
      }
   }
   else if (bestHand ==  PokerHand.ONE_PAIR){
      for (int i = 0; i < possebleHand.size(); i++) {
         int count = 0;
         for (int j = 0; j < possebleHand.size(); j++) {
            if (possebleHand.get(j).getValue() == possebleHand.get(i).getValue()) {
               count += 1;
            }
         }
         if (count == 2) {
            return new int[] { possebleHand.get(i).getValue() };
         }
      }
   }
   else if (bestHand ==  PokerHand.HIGHCARD){
      Collections.sort(possebleHand, (b, a) -> b.getValue() - a.getValue());
      return new int[] {
         possebleHand.get(0).getValue(),
         possebleHand.get(1).getValue(),
         possebleHand.get(2).getValue(),
         possebleHand.get(3).getValue(),
         possebleHand.get(4).getValue()
      };
   }
   return null;
}

 public boolean isStraightFlush(){
   return(isStraight()  && isFlush());
 }

 public boolean isFourOfAKind(){
   return hasNOfAKind(4);
 }

 public boolean isFullHouse() {
    for (int i = 0; i < possebleHand.size(); i++) {
       int count = 0;
       for (int j = 0; j < possebleHand.size(); j++) {
          if (possebleHand.get(j).getValue() == possebleHand.get(i).getValue()) {
             count += 1;
          }
       }
       if (count == 3) {
          for (int k = 0; k < possebleHand.size(); k++) {
             count = 0;
             for (int l = 0; l < possebleHand.size(); l++) {
                if (possebleHand.get(k).getValue() == possebleHand.get(l).getValue()) {
                   count += 1;
                }
             }
             if (count == 2) {
                return true;
             }
          }
       }
    }
    return false;
 }

 public boolean isFlush(){
   CardType cardtype = possebleHand.get(0).type; 
   for (int i = 0; i < possebleHand.size(); i++){
      if (cardtype != possebleHand.get(i).type){
         return false;
      }
   }
   return true;
 }

 public boolean isStraight(){
   Collections.sort(possebleHand, (a, b) -> a.getValue() - b.getValue());
   for (int i = 0; i < possebleHand.size()-1; i++){
      if (possebleHand.get(i).getValue() == possebleHand.get(i+1).getValue()){
         return false;
      }
      
   }
   return ((possebleHand.get(4).getValue() - possebleHand.get(0).getValue() == 4 )|| (possebleHand.get(4).getValue() == 14 && possebleHand.get(3).getValue() == 5));
 }

 public boolean isThreeOfAKind() {
   return hasNOfAKind(3);
 }

 public boolean isTwoPair() {
    for (int i = 0; i < possebleHand.size(); i++) {
       int count = 0;
       for (int j = 0; j < possebleHand.size(); j++) {
          if (possebleHand.get(j).getValue() == possebleHand.get(i).getValue()) {
             count += 1;
          }
       }
       if (count == 2) {
          for (int k = 0; k < possebleHand.size(); k++) {
             count = 0;
             for (int l = 0; l < possebleHand.size(); l++) {
                if (possebleHand.get(k).getValue() == possebleHand.get(l).getValue()) {
                   count += 1;
                }
             }
             if (count == 2 && possebleHand.get(i).getValue() != possebleHand.get(k).getValue()) {
                return true;
             }
          }
       }
    }
    return false;
 }
 
 public boolean isOnePair() {
   return hasNOfAKind(2);
 }

 public boolean hasNOfAKind(int n){
   for (int i = 0; i < possebleHand.size(); i++) {
      int count = 0;
      for (int j = 0; j < possebleHand.size(); j++) {
         if (possebleHand.get(j).getValue() == possebleHand.get(i).getValue()) {
            count += 1;
         }
      }
      if (count == n) {
         return true;
      }
   }
   return false;
    
 }
}
