import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JOptionPane;

/**
 * Represents a poker player with a hand, balance, and betting state.
 * Also handles hand evaluation to find the best possible 5-card hand
 * from the player's 2 cards and the 5 community cards.
 *
 * @author Nouri Ayadhi
 * @version 1.0
 */
public class Player {
   /** The best 5-card hand found during evaluation. */
   ArrayList<Card> bestPossibleHand;
   /** All 7 available cards (2 player cards + 5 community cards). */
   ArrayList<Card> availableCards;
   /** A single 5-card combination being evaluated. */
   ArrayList<Card> possibleHand;
   /** The 2  cards dealt to this player. */
   ArrayList<Card> handCards;

    /** Tiebreak values of the current best hand. */
   int[] lastTiebreakValue = { 0, 0, 0, 0, 0 };
   /** The best poker hand type found so far. */
   PokerHand bestHand;
   /** The player's current chip balance. */
   int playerBalance = 10000;
   /** The player's balance at the start of the current round. */
   int playerBalanceAtStartOfRound = playerBalance;
   /** Total chips won in the current round. */
   int totalRoundWinnings=0;
   /** Amount bet in the current betting phase. */
   int roundBet;
   /** Total chips contributed to the pot this round. */
   int totalContribution = 0;
   /** The players choosen name */
   String name;
   /** Whether this player is the big blind this round. */
   boolean isBigBlind = false;
   /** Whether this player was the last to raise. */
   boolean isLastRaiser = false;
   /** Whether this player has already checked or called. */
   boolean hasChecked = false;
   /** Whether this player is all-in. */
   boolean isAllIn= false;

   /**
     * Creates a new player with the given name
     *
     * @param name the player's display name
     */
   Player(String name, int id) {
      this.name = name;
      handCards = new ArrayList<Card>();
      this.roundBet = 0;
   }

   /**
    * Draws 2 cards from the deck into the player's hand.
    *
    * @param deck the deck to draw from
    */
   public void takeCards(Deck deck) { // adds 2 cards from the deck to the players hand and removes them from the deck
      for (int i = 0; i < 2; i++) {
         handCards.add(deck.getCards().remove(0));
      }
   }

   /**
    * Finds the best 5-card poker hand from the player's cards and the
    * community cards by checking all 21 possible combinations.
    * Each combination is sorted highest to lowest before evaluation.
    *
    * @param comunityCards the 5 community cards on the table
    * @return the best {@link PokerHand} this player can make
    */
   public PokerHand evaluate(ArrayList<Card> comunityCards) { 
      availableCards = new ArrayList<Card>(); 
      availableCards.addAll(comunityCards);
      availableCards.addAll(handCards);
      bestHand = PokerHand.HIGHCARD;
      bestPossibleHand = new ArrayList<Card>();
      lastTiebreakValue = new int[] { 0, 0, 0, 0, 0 };

      for (int i = 0; i < 7; i++) { 
         for (int j = i + 1; j < 7; j++) {

            possibleHand = new ArrayList<Card>();
            possibleHand.addAll(availableCards);
            possibleHand.remove(j);
            possibleHand.remove(i);

            ArrayList<Card> workingHand = new ArrayList<Card>(possibleHand); 
            Collections.sort(workingHand, (a, b) -> b.getValue() - a.getValue()); 

            PokerHand currentHandType = determineHandType(workingHand); 

            int[] currentTiebreakeValues = getTiebreakValuesForHand(currentHandType, workingHand); 

            if (currentHandType.getHandValue() > bestHand.getHandValue()|| (currentHandType.getHandValue() == bestHand.getHandValue()&& winsTiebreak(lastTiebreakValue, currentTiebreakeValues))) { 
               bestHand = currentHandType;
               bestPossibleHand = new ArrayList<Card>(workingHand);
               lastTiebreakValue = currentTiebreakeValues;
            }
         }
      }
      return bestHand; 
   }

   /**
    * Checks if a new set of tiebreak values beats the current best,
    * by comparing each index one by one.
    *
    * @param lastValue        the current best tiebreak values
    * @param potentialNewBest the new tiebreak values to compare
    * @return {@code true} if the new values win the tiebreak
    */
   public boolean winsTiebreak(int[] lastValue, int[] potentialNewBest) {
      if (potentialNewBest == null)
         return false;
      if (lastValue == null)
         return true;
      for (int i = 0; i < 5; i++) { 
         if (potentialNewBest[i] > lastValue[i])
            return true;
         if (potentialNewBest[i] < lastValue[i])
            return false;
      }
      return false;
   }
   
   /**
    * Returns the tiebreak values of the player's current best hand.
    *
    * @return an array of tiebreak values
    */
   public int[] getTiebreakValues() { 
      return lastTiebreakValue;
   }
   
   /**
    * Determines the type of poker hand for a given 5-card hand.
    *
    * @param workingHand the 5-card hand to evaluate
    * @return the {@link PokerHand} type of the given hand
    */
   private PokerHand determineHandType(ArrayList<Card> workingHand) { 
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

   /**
    * Returns the tiebreak values for a given hand type.
    * Unused slots in the Array are set to 0.
    * Note: for a straight or straight flush, the Ace can count as 1
    * in a straight (A-2-3-4-5), in which case 5 is used as the tiebreak value.
    *
    * @param hand        the type of the hand
    * @param workingHand the 5-card hand, sorted highest to lowest
    * @return an array of up to 5 tiebreak values
    */
   private int[] getTiebreakValuesForHand(PokerHand hand, ArrayList<Card> workingHand) { 
      int[] tiebreaker = new int[5];
      if (hand == PokerHand.STRAIGHT_FLUSH || hand == PokerHand.STRAIGHT) {
         if (workingHand.get(0).getValue() == 14 && workingHand.get(1).getValue() == 5) { 
            tiebreaker[0] = 5;
         } else {
            tiebreaker[0] = workingHand.get(0).getValue(); 
         }
      }

      else if (hand == PokerHand.FOUR_OF_A_KIND) {
         int quad = getRankOfN(workingHand, 4); 
         int remainingCard = 0;
         for (Card card : workingHand) { 
            if (card.getValue() != quad)
               remainingCard = card.getValue();
         }
         tiebreaker[0] = quad;
         tiebreaker[1] = remainingCard;
      }

      else if (hand == PokerHand.FULL_HOUSE) {
         int triple = getRankOfN(workingHand, 3); 
         int pair = 0;
         for (Card card : workingHand) {
            if (card.getValue() != triple) 
               pair = card.getValue();
         }
         tiebreaker[0] = triple;
         tiebreaker[1] = pair;
      }

      else if (hand == PokerHand.FLUSH || hand == PokerHand.HIGHCARD) {
         for (int i = 0; i < 5; i++)
            tiebreaker[i] = workingHand.get(i).getValue();
      }

      else if (hand == PokerHand.THREE_OF_A_KIND) {
         int triple = getRankOfN(workingHand, 3);
         tiebreaker[0] = triple;
         int index = 1;
         for (Card card : workingHand) {
            if (card.getValue() != triple) {
               tiebreaker[index++] = card.getValue();
           }
         }
      }

      else if (hand == PokerHand.TWO_PAIR) {
         int highPair = -1;
         int lowPair = -1;
         int remainingCard = -1;
         for (int i = 0; i < 4; i++) {
            if (workingHand.get(i).getValue() == workingHand.get(i + 1).getValue()) { 
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
         int pair = getRankOfN(workingHand, 2);
         tiebreaker[0] = pair;
         int index = 1;
         for (Card card : workingHand) {
            if (card.getValue() != pair)
               tiebreaker[index++] = card.getValue();
         }
      }

      return tiebreaker;
   }

   /**
    * Checks if all 5 cards in the hand have the same suit.
    *
    * @param workingHand the 5-card hand to check
    * @return {@code true} if all cards have the same suit
    */
   private boolean isFlush(ArrayList<Card> workingHand) { 
      CardType type = workingHand.get(0).type;
      for (Card card : workingHand) {
         if (card.type != type)
            return false;
      }
      return true;
   }

   /**
    * Checks if the 5 cards form a straight.
    * Also handles the special case where the Ace counts as 1 (A-2-3-4-5).
    *
    * @param workingHand 
    * @return {@code true} if the hand is a straight
    */
   private boolean isStraight(ArrayList<Card> workingHand) {
      if (workingHand.get(0).getValue() == 14 && workingHand.get(1).getValue() == 5
            && workingHand.get(2).getValue() == 4 && workingHand.get(3).getValue() == 3
            && workingHand.get(4).getValue() == 2) { 
         return true;
      }
      for (int i = 0; i < 4; i++) { 
         if (workingHand.get(i).getValue() - workingHand.get(i + 1).getValue() != 1)
            return false;
      }
      return true;
   }

   /**
    * Checks if the hand contains a three-of-a-kind and a pair of a different
    * value.
    *
    * @param workingHand the 5-card hand to check
    * @return {@code true} if the hand is a full house
    */
   private boolean isFullHouse(ArrayList<Card> workingHand) {
      int triple = getRankOfN(workingHand, 3);
      if (triple == -1)
         return false;
      for (Card card : workingHand) {
         if (card.getValue() != triple && hasRankCount(workingHand, card.getValue(), 2))
            return true;
      }
      return false;
   }

   /**
    * Checks if the hand contains exactly two pairs.
    *
    * @param workingHand the 5-card hand, sorted highest to lowest
    * @return {@code true} if the hand contains two pairs
    */
   private boolean isTwoPair(ArrayList<Card> workingHand) { 
      int NumberOFpairs = 0;
      for (int i = 0; i < 4; i++) {
         if (workingHand.get(i).getValue() == workingHand.get(i + 1).getValue()) {
            NumberOFpairs++;
            i++;
         }
      }
      return NumberOFpairs == 2;
   }
   
   /**
    * Checks if the hand contains at least one group of {@code n} cards with the
    * same value.
    *
    * @param workingHand the 5-card hand to check
    * @param n           the required count
    * @return {@code true} if such a group exists
    */
   private boolean hasNOfAKind(ArrayList<Card> workingHand, int n) { 
      return getRankOfN(workingHand, n) != -1;
   }
   
   /**
    * Returns the card value that appears exactly {@code n} times in the hand,
    * or {@code -1} if no such value exists.
    *
    * @param workingHand the 5-card hand to check
    * @param n           the required count
    * @return the matching card value, or {@code -1} if not found
    */
   private int getRankOfN(ArrayList<Card> workingHand, int n) {
      for (Card card : workingHand) {
         if (hasRankCount(workingHand, card.getValue(), n))
            return card.getValue();
      }
      return -1;
   }
   
   /**
    * Checks if a specific card value appears exactly {@code target} times in the
    * hand.
    *
    * @param workingHand the 5-card hand to check
    * @param value       the card value to count
    * @param target      the expected count
    * @return {@code true} if the value appears exactly {@code target} times
    */
   private boolean hasRankCount(ArrayList<Card> workingHand, int value, int target) { // checks if the CardValue exist as often as needed
      int count = 0;
      for (Card card : workingHand) {
         if (card.getValue() == value)
            count++;
      }
      return count == target;
   }
   
   /**
    * Resets the player's state for a new round.
    */
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
      hasChecked = false;
      totalContribution = 0;
      isAllIn = false;

   }
   
   /**
    * Asks the player if they want to continue playing.
    * Returns {@code true} if the player has run out of money or chooses to stop.
    *
    * @return {@code true} if the player has stopped playing
    */
   public boolean hasStoppedPlaying(){
      if (playerBalance < 1){
         JOptionPane.showMessageDialog(null, this.name+" ,you've ran out of money!");
         return true;
      }
      int continuePlaying = JOptionPane.showConfirmDialog(null,this.name +", do you want to continue playing?");
      return continuePlaying != JOptionPane.YES_OPTION;
   }
}