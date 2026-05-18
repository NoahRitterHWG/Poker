public enum PokerHand {
    HIGHCARD(0),
    ONE_PAIR(1),
    TWO_PAIR(2),
    THREE_OF_A_KIND(3),
    STRAIGHT(4),
    FLUSH(5),
    FULL_HOUSE(6),
    FOUR_OF_A_KIND(7),
    STRAIGHT_FLUSH(8);



    private final int HandValue;

    PokerHand(int HandValue){
        this.HandValue = HandValue;
    }
    
    public int getHandValue(){
        return this.HandValue;
    }


}

