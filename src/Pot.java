import java.util.ArrayList;

public class Pot {

    int amount;

    ArrayList<Player> eligiblePlayers;

    public Pot(int amount,
            ArrayList<Player> eligiblePlayers) {

        this.amount = amount;
        this.eligiblePlayers = eligiblePlayers;
    }
}