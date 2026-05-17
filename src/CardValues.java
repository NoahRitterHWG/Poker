public enum CardValues {
    A(12),
    TWO(0), 
    THREE(1),
    FOUR(2),
    FIVE(3), 
    SIX(4), 
    SEVEN(5), 
    EIGHT(6), 
    NINE(7), 
    TEN(8), 
    J(9), 
    Q(10), 
    K(11);

    private final int CardValue;

    CardValues(int CardValue){
        this.CardValue = CardValue;
    }
    
    public int getCardValue(){
        return this.CardValue;
    }


}
