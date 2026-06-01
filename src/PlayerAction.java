public enum PlayerAction {
    FOLD("Fold"),
    CALL("Check/Call"),
    RAISE("Raise");

    private final String label;

    PlayerAction(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}