import java.util.ArrayList;

public class SidePot {

    int amount;
    ArrayList<Player> eligiblePlayers;

    SidePot(int amount, ArrayList<Player> eligiblePlayers) {
        this.amount = amount;
        this.eligiblePlayers = eligiblePlayers;
    }
}