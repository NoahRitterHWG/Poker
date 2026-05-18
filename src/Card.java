public class Card {
    CardType type;
    CardValues value;
    Card(CardType type, CardValues value){
        this.type = type;
        this.value = value;
    }
    
    public int getValue() {
        return this.value.getCardValue();
    }
    public String toString(){
        if(value == CardValues.A || value == CardValues.J || value == CardValues.Q || value == CardValues.K){
            return value + "-" + type.getCardTypeChar();
        }
        else{    
            return value.getCardValue() + "-" + type.getCardTypeChar();
            }
        }

}