public class PlayerMove {

    private PlayerAction action;
    private int raiseAmount;

    public PlayerMove(PlayerAction action, int raiseAmount) {
        this.action = action;
        this.raiseAmount = raiseAmount;
    }

    public PlayerAction getAction() {
        return action;
    }

    public int getRaiseAmount() {
        return raiseAmount;
    }
}
