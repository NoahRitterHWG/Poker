
public class Player {
    int bet;
    int playerBalance = 10000;
    Hand hand;
    String name;
    Player(String name) {
        this.hand = new Hand();
        this.name = name;
    }

    public void hit(Card card) {
        hand.pullCard(card);
    }
    public void bet(int bet) {
        playerBalance -= bet;
        this.bet = bet;
    

}
}
