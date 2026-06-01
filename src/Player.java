import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JOptionPane;

public class Player {

   ArrayList<Card> bestPossibleHand;
   ArrayList<Card> availableCards;
   ArrayList<Card> possibleHand;
   ArrayList<Card> handCards;

   int[] lastTiebreakValue = { 0, 0, 0, 0, 0 };
   PokerHand bestHand;
   int playerBalance = 10000;
   int id;
   int roundBet;
   String name;
   boolean isBigBlind = false;
   boolean isLastRaiser = false;
   boolean hasChecked = false;


   Player(String name, int id) {
      this.name = name;
      this.id = id;
      handCards = new ArrayList<Card>();
      this.roundBet = 0;
   }

   public void takeCards(Deck deck) { // adds 2 cards from the deck to the players hand and removes them from the deck
      for (int i = 0; i < 2; i++) {
         handCards.add(deck.getCards().remove(0));
      }
   }

   public PokerHand evaluate(ArrayList<Card> comunityCards) { // checks what the best pokerhand is that the player can have with his current cards
      availableCards = new ArrayList<Card>(); // combines the community cards and the cards of the player
      availableCards.addAll(comunityCards);
      availableCards.addAll(handCards);
      bestHand = PokerHand.HIGHCARD;
      bestPossibleHand = new ArrayList<Card>();
      lastTiebreakValue = new int[] { 0, 0, 0, 0, 0 };

      for (int i = 0; i < 7; i++) { // removes every combination of 2 cards from the 7 possible cards to get every combination of 5 cards (21)
         for (int j = i + 1; j < 7; j++) {

            possibleHand = new ArrayList<Card>();
            possibleHand.addAll(availableCards);
            possibleHand.remove(j);
            possibleHand.remove(i);

            ArrayList<Card> workingHand = new ArrayList<Card>(possibleHand); // creates a temporary Arraylist to work with, so that possibleHand stays intact
            Collections.sort(workingHand, (a, b) -> b.getValue() - a.getValue()); // sorts the Hand from highest card to lowest. needed for isStraight and for getTiebreakValuesForHand

            PokerHand currentHandType = determineHandType(workingHand); // checks the Handtype of the current hand

            int[] currentTiebreakeValues = getTiebreakValuesForHand(currentHandType, workingHand); // gets the tiebreake Values of the current hand

            if (currentHandType.getHandValue() > bestHand.getHandValue()
                  || (currentHandType.getHandValue() == bestHand.getHandValue()
                        && winsTiebreak(lastTiebreakValue, currentTiebreakeValues))) { // if the current hand is better than the previous best it becomes the new best
               bestHand = currentHandType;
               bestPossibleHand = new ArrayList<Card>(workingHand);
               lastTiebreakValue = currentTiebreakeValues;
            }
         }
      }
      return bestHand; // after all combinations are checked, the best combination is returned
   }

   public boolean winsTiebreak(int[] lastValue, int[] potentialNewBest) { // checks if the new value beats the old
      if (potentialNewBest == null)
         return false;
      if (lastValue == null)
         return true;
      for (int i = 0; i < 5; i++) { // compares the integers at every index of the arrays
         if (potentialNewBest[i] > lastValue[i])
            return true;
         if (potentialNewBest[i] < lastValue[i])
            return false;
      }
      return false;
   }

   public int[] getTiebreakValues() { // returns the tiebreakevalue of the best hand of the player, needed for determine winner in game
      return lastTiebreakValue;
   }

   private PokerHand determineHandType(ArrayList<Card> workingHand) { // determines what kind of pokerhand the current hand is

      boolean flush = isFlush(workingHand);
      boolean straight = isStraight(workingHand);

      if (straight && flush)
         return PokerHand.STRAIGHT_FLUSH;
      if (hasNOfAKind(workingHand, 4))
         return PokerHand.FOUR_OF_A_KIND;
      if (isFullHouse(workingHand))
         return PokerHand.FULL_HOUSE;
      if (flush)
         return PokerHand.FLUSH;
      if (straight)
         return PokerHand.STRAIGHT;
      if (hasNOfAKind(workingHand, 3))
         return PokerHand.THREE_OF_A_KIND;
      if (isTwoPair(workingHand))
         return PokerHand.TWO_PAIR;
      if (hasNOfAKind(workingHand, 2))
         return PokerHand.ONE_PAIR;
      return PokerHand.HIGHCARD;
   }

   private int[] getTiebreakValuesForHand(PokerHand hand, ArrayList<Card> workingHand) { // gives out an int[] with all the needed Tiebrake values. a Hand needs less that 5values, the rest is set to 0
      int[] tiebreaker = new int[5];
      if (hand == PokerHand.STRAIGHT_FLUSH || hand == PokerHand.STRAIGHT) {
         if (workingHand.get(0).getValue() == 14 && workingHand.get(1).getValue() == 5) { // special case where the aceis a 1
            tiebreaker[0] = 5;
         } else {
            tiebreaker[0] = workingHand.get(0).getValue(); // sets the first index of the array to the highest card inthe straight
         }
      }

      else if (hand == PokerHand.FOUR_OF_A_KIND) {
         int quad = getRankOfN(workingHand, 4); // sets the value of the cardvalue that exist 4 times to an integer
         int remainingCard = 0;
         for (Card card : workingHand) { // goes through all cards of the hand
            if (card.getValue() != quad)
               remainingCard = card.getValue();
         }
         tiebreaker[0] = quad;
         tiebreaker[1] = remainingCard;
      }

      else if (hand == PokerHand.FULL_HOUSE) {
         int triple = getRankOfN(workingHand, 3); // sets the value of the cardvalue that exist 3 times to an integer
         int pair = 0;
         for (Card card : workingHand) {
            if (card.getValue() != triple) // cards that are not part of the triple must be part of the double
               pair = card.getValue();
         }
         tiebreaker[0] = triple;
         tiebreaker[1] = pair;
      }

      else if (hand == PokerHand.FLUSH || hand == PokerHand.HIGHCARD) { // the tiebreaker values in case of flush andhighcard are the same : all cards are needed
         for (int i = 0; i < 5; i++)
            tiebreaker[i] = workingHand.get(i).getValue();
      }

      else if (hand == PokerHand.THREE_OF_A_KIND) {
         int triple = getRankOfN(workingHand, 3);
         tiebreaker[0] = triple;
         int index = 1;
         for (Card card : workingHand) {
            if (card.getValue() != triple) // values of cards that are not part of the triple get added to the array (first the higher card than the lower)
               tiebreaker[index++] = card.getValue();
         }
      }

      else if (hand == PokerHand.TWO_PAIR) {
         int highPair = -1;
         int lowPair = -1;
         int remainingCard = -1;
         for (int i = 0; i < 4; i++) {
            if (workingHand.get(i).getValue() == workingHand.get(i + 1).getValue()) { // highpair is set to the firstpair that is encountered,lowpair is set to the second
               if (highPair == -1)
                  highPair = workingHand.get(i).getValue();
               else
                  lowPair = workingHand.get(i).getValue();
               i++;
            }
         }
         for (Card card : workingHand) { //
            if (card.getValue() != highPair && card.getValue() != lowPair)
               remainingCard = card.getValue();
         }
         tiebreaker[0] = highPair;
         tiebreaker[1] = lowPair;
         tiebreaker[2] = remainingCard;
      }

      else if (hand == PokerHand.ONE_PAIR) {
         int pair = getRankOfN(workingHand, 2); // same logic as three of a kind
         tiebreaker[0] = pair;
         int index = 1;
         for (Card card : workingHand) {
            if (card.getValue() != pair)
               tiebreaker[index++] = card.getValue();
         }
      }

      return tiebreaker; // returns the array
   }

   private boolean isFlush(ArrayList<Card> workingHand) { // if any card has a different CardType than the rest it returns false, otherwise true
      CardType type = workingHand.get(0).type;
      for (Card card : workingHand) {
         if (card.type != type)
            return false;
      }
      return true;
   }

   private boolean isStraight(ArrayList<Card> workingHand) {
      if (workingHand.get(0).getValue() == 14 && workingHand.get(1).getValue() == 5
            && workingHand.get(2).getValue() == 4 && workingHand.get(3).getValue() == 3
            && workingHand.get(4).getValue() == 2) { // special case where the ace is a 1
         return true;
      }
      for (int i = 0; i < 4; i++) { // if any card is not one smaller than the previous, retun false
         if (workingHand.get(i).getValue() - workingHand.get(i + 1).getValue() != 1)
            return false;
      }
      return true;
   }

   private boolean isFullHouse(ArrayList<Card> workingHand) { // if the hand contains a triple and a pair that is not part of the triple it returns true
      int triple = getRankOfN(workingHand, 3);
      if (triple == -1)
         return false;
      for (Card card : workingHand) {
         if (card.getValue() != triple && hasRankCount(workingHand, card.getValue(), 2))
            return true;
      }
      return false;
   }

   private boolean isTwoPair(ArrayList<Card> workingHand) { // checks if there are 2 pairs by comparing a card at an index i and the next card
      int NumberOFpairs = 0;
      for (int i = 0; i < 4; i++) {
         if (workingHand.get(i).getValue() == workingHand.get(i + 1).getValue()) {
            NumberOFpairs++;
            i++;
         }
      }
      return NumberOFpairs == 2;
   }

   private boolean hasNOfAKind(ArrayList<Card> workingHand, int n) { // needed for game
      return getRankOfN(workingHand, n) != -1;
   }

   private int getRankOfN(ArrayList<Card> workingHand, int n) { // checks which CardValue exist n times in the hand
      for (Card card : workingHand) {
         if (hasRankCount(workingHand, card.getValue(), n))
            return card.getValue();
      }
      return -1;
   }

   private boolean hasRankCount(ArrayList<Card> workingHand, int value, int target) { // checks if the CardValue exist as often as needed
      int count = 0;
      for (Card card : workingHand) {
         if (card.getValue() == value)
            count++;
      }
      return count == target;
   }

   public void resetplayer(){
      if (bestPossibleHand != null){
         bestPossibleHand.clear();
      }
      if (availableCards != null){
         availableCards.clear();
      }
      if (possibleHand != null){
         possibleHand.clear();
      }
      isBigBlind = false;
      handCards.clear();
      lastTiebreakValue = new int[] { 0, 0, 0, 0, 0 };
      roundBet = 0;

   }

  

   public boolean hasStoppedPlaying(){
      if (playerBalance < 1){
         JOptionPane.showMessageDialog(null, "You've ran out of money!");
         return true;
      }
      int continuePlaying = JOptionPane.showConfirmDialog(null,this.name +", do you want to continue playing?");
      return continuePlaying != JOptionPane.YES_OPTION;
   }
}