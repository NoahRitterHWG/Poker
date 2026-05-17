public class Middle {
    private Card[] middleCards;
    int uncovertCarts = 0;

    public void takeCards(Deck deck){
        for(int i=0; i<5; i++){
            middleCards[i] = deck.getCards().get(i);
            deck.getCards().remove(i);
        }
    }
    public Card getUncovertCards(int index){ //gibt, wenn alle Karten aufgedeckt sind, die Karte zurück
        Card card = null;
        if(uncovertCarts==5){
            card = middleCards[index];
        }
        return card;
    }
}