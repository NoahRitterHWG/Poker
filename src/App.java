/**
 * Entry point of the Texas Hold'em poker game.
 *
 * <p>
 * Requires Java 8 or later.
 * </p>
 *
 * @author Nouri Ayadhi
 * @version 1.0
 */
public class App {
    /**
     * Starts the application by creating a {@link Game} instance and running it on
     * a new thread.
     */
    public static void main(String[] args) {

    Game game = new Game();

    new Thread(() -> {game.startGame();}).start();
    }
}