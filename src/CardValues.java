public enum CardValues {
    A(14),
    TWO(2), 
    THREE(3),
    FOUR(4),
    FIVE(5), 
    SIX(6), 
    SEVEN(7), 
    EIGHT(8), 
    NINE(9), 
    TEN(10), 
    J(11), 
    Q(12), 
    K(13);

    private final int CardValue;

    CardValues(int CardValue){
        this.CardValue = CardValue;
    }
    
    public int getCardValue(){
        return this.CardValue;
    }


}
