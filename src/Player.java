public class Player {
    int playerBalance = 10000;  //Geld des Spielers
    private Card[] handCards;
    int id;
    String name;
    Player(String name, int id) {   //Konstruktor
        this.name = name;
        handCards = new Card[2];
        this.id = id;
    }
    public void takeCards(Deck deck){    //lässt die Spieler zwei zufällige Karten nehmen
        for(int i=0; i<2; i++){
        handCards[i] = deck.getCards().get(i);
        deck.getCards().remove(i);
        }
    }
    public Card getHandCard(int index){
        Card card = handCards[index];
        return card;
    }
}
