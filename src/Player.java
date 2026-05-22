import java.util.ArrayList;
import java.util.Collections;

public class Player {
    
    int[] lastTieBreakValue = {0,0,0,0,0};
    
    
    int playerBalance = 10000;  //Geld des Spielers
   ArrayList<Card> handCards;
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
        System.out.println("PlayerKArten:"+ handCards.size());
        }
    }
    public Card getHandCard(int index){
        Card card = handCards.get(index);
        return card;
    }

   public PokerHand evaluate(ArrayList<Card> comunityCards, ArrayList<Card> handCards){   
   ArrayList<Card> availableCards;           
   availableCards = new ArrayList<Card>();
   PokerHand bestHand;
   availableCards.addAll(comunityCards);
   availableCards.addAll(handCards);
   ArrayList<Card> possebleHand;
   bestHand = PokerHand.HIGHCARD;              
   possebleHand = new ArrayList<Card>();                  
   
   for (int i=0; i < 6; i++){
      for (int j = i+1; j < 7; j++){
         possebleHand.clear();
         possebleHand.addAll(availableCards);
         System.out.println("possCards:"+possebleHand.size());
         if(j<i){
            possebleHand.remove(i);
            possebleHand.remove(j);
         } else{
         possebleHand.remove(j);
         possebleHand.remove(i);
         }
         
         if(isStraightFlush(possebleHand) && PokerHand.STRAIGHT_FLUSH.getHandValue() > bestHand.getHandValue() || isStraightFlush(possebleHand) && PokerHand.STRAIGHT_FLUSH.getHandValue() == bestHand.getHandValue() && winsTieBreak(lastTieBreakValue, getTiebrakValues(possebleHand,bestHand))){
            bestHand = PokerHand.STRAIGHT_FLUSH;
            lastTieBreakValue = getTiebrakValues(possebleHand,bestHand);
         }
         else if (isFourOfAKind(possebleHand) && PokerHand.FOUR_OF_A_KIND.getHandValue() > bestHand.getHandValue() || isFourOfAKind(possebleHand) && PokerHand.FOUR_OF_A_KIND.getHandValue() == bestHand.getHandValue() && winsTieBreak(lastTieBreakValue, getTiebrakValues(possebleHand,bestHand))){
            bestHand = PokerHand.FOUR_OF_A_KIND;
            lastTieBreakValue = getTiebrakValues(possebleHand,bestHand);
         }
         else if (isFullHouse(possebleHand) && PokerHand.FULL_HOUSE.getHandValue() > bestHand.getHandValue() || isFullHouse(possebleHand) && PokerHand.FULL_HOUSE.getHandValue() == bestHand.getHandValue() && winsTieBreak(lastTieBreakValue, getTiebrakValues(possebleHand,bestHand))){
            bestHand = PokerHand.FULL_HOUSE;
            lastTieBreakValue = getTiebrakValues(possebleHand,bestHand);
         }
         else if (isFlush(possebleHand) && PokerHand.FLUSH.getHandValue() > bestHand.getHandValue() || isFlush(possebleHand) && PokerHand.FLUSH.getHandValue() == bestHand.getHandValue() && winsTieBreak(lastTieBreakValue, getTiebrakValues(possebleHand,bestHand))){
            bestHand = PokerHand.FLUSH;
            lastTieBreakValue = getTiebrakValues(possebleHand,bestHand);
         }
         else if (isStraight(possebleHand) && PokerHand.STRAIGHT.getHandValue() > bestHand.getHandValue() || isStraight(possebleHand) && PokerHand.STRAIGHT.getHandValue() == bestHand.getHandValue() && winsTieBreak(lastTieBreakValue, getTiebrakValues(possebleHand,bestHand))){
            bestHand = PokerHand.STRAIGHT;
            lastTieBreakValue = getTiebrakValues(possebleHand,bestHand);
         }
         else if (isThreeOfAKind(possebleHand) && PokerHand.THREE_OF_A_KIND.getHandValue() > bestHand.getHandValue() || isThreeOfAKind(possebleHand) && PokerHand.THREE_OF_A_KIND.getHandValue() == bestHand.getHandValue() && winsTieBreak(lastTieBreakValue, getTiebrakValues(possebleHand,bestHand))){
            bestHand = PokerHand.THREE_OF_A_KIND;
            lastTieBreakValue = getTiebrakValues(possebleHand,bestHand);
         }
         else if (isTwoPair(possebleHand) && PokerHand.TWO_PAIR.getHandValue() > bestHand.getHandValue() || isTwoPair(possebleHand) && PokerHand.TWO_PAIR.getHandValue() == bestHand.getHandValue() && winsTieBreak(lastTieBreakValue, getTiebrakValues(possebleHand,bestHand))){
            bestHand = PokerHand.TWO_PAIR;
            lastTieBreakValue = getTiebrakValues(possebleHand,bestHand);
         }
         else if (isOnePair(possebleHand) && PokerHand.ONE_PAIR.getHandValue() > bestHand.getHandValue() || isOnePair(possebleHand) && PokerHand.ONE_PAIR.getHandValue() == bestHand.getHandValue() && winsTieBreak(lastTieBreakValue, getTiebrakValues(possebleHand,bestHand))){
            bestHand = PokerHand.ONE_PAIR;
            lastTieBreakValue = getTiebrakValues(possebleHand,bestHand);
         }
         else if (winsTieBreak(lastTieBreakValue, getTiebrakValues(possebleHand,bestHand))){
            lastTieBreakValue = getTiebrakValues(possebleHand,bestHand);
         }
      }
   }
   return bestHand;
 }

 public boolean winsTieBreak(int[] lastValue,int[] potNewBest){
   for(int i=0; i < lastValue.length; i++){
      if (lastValue[i] < potNewBest[i]){
         return true;
      } else if (lastValue[i]>potNewBest[i]){
         return false;
      }
   }
   return false;
}

 public int[] getTiebrakValues(ArrayList<Card> possebleHand, PokerHand bestHand){

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
      for (int i = 0; i < 4; i++) {
         int count = 0;
         for (int j = i+1; j < 5; j++) {
            if (possebleHand.get(j).getValue() == possebleHand.get(i).getValue()) {
               count += 1;
            }
         }
         if (count == 1) {
            for (int k = 0; k < 4; k++) {
               count = 0;
               for (int l = i+1; l < 5; l++) {
                  if (possebleHand.get(k).getValue() == possebleHand.get(l).getValue()) {
                     count += 1;
                  }
               }
               if (count == 1 && possebleHand.get(i).getValue() != possebleHand.get(k).getValue()) {
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
      for (int i = 0; i < 4; i++) {
         int count = 0;
         for (int j = i+1; j < 5; j++) {
            if (possebleHand.get(j).getValue() == possebleHand.get(i).getValue()) {
               count += 1;
            }
         }
         if (count <= 1) {
            return new int[] { possebleHand.get(i).getValue() };
         }
         System.out.println("Falsche Schleife"+ count);
      }
      
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

 public boolean isStraightFlush(ArrayList<Card> possebleHand){
   return(isStraight(possebleHand)  && isFlush(possebleHand));
 }

 public boolean isFourOfAKind(ArrayList<Card> possebleHand){
   return hasNOfAKind(4, possebleHand);
 }

 public boolean isFullHouse(ArrayList<Card> possebleHand) {
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

 public boolean isFlush(ArrayList<Card> possebleHand){
   CardType cardtype = possebleHand.get(0).type; 
   for (int i = 0; i < possebleHand.size(); i++){
      if (cardtype != possebleHand.get(i).type){
         return false;
      }
   }
   return true;
 }

 public boolean isStraight(ArrayList<Card> possebleHand){
   Collections.sort(possebleHand, (a, b) -> a.getValue() - b.getValue());
   for (int i = 0; i < possebleHand.size()-1; i++){
      if (possebleHand.get(i).getValue() == possebleHand.get(i+1).getValue()){
         return false;
      }
      
   }
   return ((possebleHand.get(4).getValue() - possebleHand.get(0).getValue() == 4 )|| (possebleHand.get(4).getValue() == 14 && possebleHand.get(3).getValue() == 5));
 }

 public boolean isThreeOfAKind(ArrayList<Card> possebleHand) {
   return hasNOfAKind(3, possebleHand);
 }

 public boolean isTwoPair(ArrayList<Card> possebleHand) {
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
 
 public boolean isOnePair(ArrayList<Card> possebleHand) {
   return hasNOfAKind(2,possebleHand);
 }

 public boolean hasNOfAKind(int n,ArrayList<Card> possebleHand){
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
