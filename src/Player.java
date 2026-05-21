import java.util.ArrayList;
import java.util.Collections;

public class Player {
    ArrayList<Card> availableCards;
    int[] lastTieBreakValue = {0,0,0,0,0};
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
        handCards.add(deck.getCards().get(0));
        deck.getCards().remove(0);
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
   possebleHand = new ArrayList<Card>();                  
   
   for (int i=0; i < 6; i++){
      for (int j = i+1; j < 7; j++){
         possebleHand.removeAll(possebleHand);
         possebleHand.addAll(availableCards);
         if(j<i){
            possebleHand.remove(i);
            possebleHand.remove(j);
         } else{
         possebleHand.remove(j);
         possebleHand.remove(i);
         }
         
         if(isStraightFlush() && PokerHand.STRAIGHT_FLUSH.getHandValue() > bestHand.getHandValue() || isStraightFlush() && PokerHand.STRAIGHT_FLUSH.getHandValue() == bestHand.getHandValue() && winsTieBreak(lastTieBreakValue, getTiebrakValues())){
            bestHand = PokerHand.STRAIGHT_FLUSH;
            lastTieBreakValue = getTiebrakValues();
         }
         else if (isFourOfAKind() && PokerHand.FOUR_OF_A_KIND.getHandValue() > bestHand.getHandValue() || isFourOfAKind() && PokerHand.FOUR_OF_A_KIND.getHandValue() == bestHand.getHandValue() && winsTieBreak(lastTieBreakValue, getTiebrakValues())){
            bestHand = PokerHand.FOUR_OF_A_KIND;
            lastTieBreakValue = getTiebrakValues();
         }
         else if (isFullHouse() && PokerHand.FULL_HOUSE.getHandValue() > bestHand.getHandValue() || isFullHouse() && PokerHand.FULL_HOUSE.getHandValue() == bestHand.getHandValue() && winsTieBreak(lastTieBreakValue, getTiebrakValues())){
            bestHand = PokerHand.FULL_HOUSE;
            lastTieBreakValue = getTiebrakValues();
         }
         else if (isFlush() && PokerHand.FLUSH.getHandValue() > bestHand.getHandValue() || isFlush() && PokerHand.FLUSH.getHandValue() == bestHand.getHandValue() && winsTieBreak(lastTieBreakValue, getTiebrakValues())){
            bestHand = PokerHand.FLUSH;
            lastTieBreakValue = getTiebrakValues();
         }
         else if (isStraight() && PokerHand.STRAIGHT.getHandValue() > bestHand.getHandValue() || isStraight() && PokerHand.STRAIGHT.getHandValue() == bestHand.getHandValue() && winsTieBreak(lastTieBreakValue, getTiebrakValues())){
            bestHand = PokerHand.STRAIGHT;
            lastTieBreakValue = getTiebrakValues();
         }
         else if (isThreeOfAKind() && PokerHand.THREE_OF_A_KIND.getHandValue() > bestHand.getHandValue() || isThreeOfAKind() && PokerHand.THREE_OF_A_KIND.getHandValue() == bestHand.getHandValue() && winsTieBreak(lastTieBreakValue, getTiebrakValues())){
            bestHand = PokerHand.THREE_OF_A_KIND;
            lastTieBreakValue = getTiebrakValues();
         }
         else if (isTwoPair() && PokerHand.TWO_PAIR.getHandValue() > bestHand.getHandValue() || isTwoPair() && PokerHand.TWO_PAIR.getHandValue() == bestHand.getHandValue() && winsTieBreak(lastTieBreakValue, getTiebrakValues())){
            bestHand = PokerHand.TWO_PAIR;
            lastTieBreakValue = getTiebrakValues();
         }
         else if (isOnePair() && PokerHand.ONE_PAIR.getHandValue() > bestHand.getHandValue() || isOnePair() && PokerHand.ONE_PAIR.getHandValue() == bestHand.getHandValue() && winsTieBreak(lastTieBreakValue, getTiebrakValues())){
            bestHand = PokerHand.ONE_PAIR;
            lastTieBreakValue = getTiebrakValues();
         }
         else if (winsTieBreak(lastTieBreakValue, getTiebrakValues())){
            lastTieBreakValue = getTiebrakValues();
         }
      }
   }
   return bestHand;
 }

 public boolean winsTieBreak(int[] lastValue,int[] potNewBest){
   for(int i=0; i < lastValue.length; i++){
      if (lastValue[i] < potNewBest[i]){
         return true;
      }
   }
   return false;
}

 public int[] getTiebrakValues(){

   if (bestHand ==  PokerHand.STRAIGHT_FLUSH){
      System.out.println("1funktioniert");
      if (possebleHand.get(4).getValue() == 14 && possebleHand.get(3).getValue() == 5) {
         return new int[] { 5 };
      } else {
         return new int[] { possebleHand.get(4).getValue() };
      }
   }
   else if (bestHand ==  PokerHand.FOUR_OF_A_KIND){
      System.out.println("2funktioniert");
      for (int i = 0; i < 5; i++) {
         int count = 0;
         for (int j = 0; j < 5; j++) {
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
      System.out.println("3funktioniert");
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
      System.out.println("4funktioniert");
      return new int[] {possebleHand.get(4).getValue()};
   }
   else if (bestHand ==  PokerHand.STRAIGHT){
      System.out.println("5funktioniert");
      if (possebleHand.get(4).getValue() == 14 && possebleHand.get(3).getValue() == 5) {
         return new int[] { 5 };
      } else {
         return new int[] { possebleHand.get(4).getValue() };
      }
   }
   else if (bestHand ==  PokerHand.THREE_OF_A_KIND){
      System.out.println("6funktioniert");
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
      System.out.println("7funktioniert");
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
      System.out.println("Falsche Schleife");
   }
   else if (bestHand ==  PokerHand.ONE_PAIR){
      System.out.println("8funktioniert");
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
      System.out.println("Falsche Schleife");
   }
   else if (bestHand ==  PokerHand.HIGHCARD){
      System.out.println("9funktioniert");
      Collections.sort(possebleHand, (b, a) -> b.getValue() - a.getValue());
      return new int[] {
         possebleHand.get(0).getValue(),
         possebleHand.get(1).getValue(),
         possebleHand.get(2).getValue(),
         possebleHand.get(3).getValue(),
         possebleHand.get(4).getValue()
      };
   }
   System.out.println("BlaaabLaaa");
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
   if (possebleHand.size() != 5){
      System.out.println(possebleHand.size());
      return false;
   }
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
