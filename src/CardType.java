public enum CardType {

    CLUBS('C'), HEARTS('H'), DAIMONDS('D'), SPADES('S');
    private final char CardTypeChar;

    CardType (char CardTypeChar) {
        this.CardTypeChar = CardTypeChar;
    }

    public char getCardTypeChar(){
        return this.CardTypeChar;
    }
}