public class App {
public static void main(String[] args) {

    Game game = new Game();

    new Thread(() -> {
        game.startGame();
    }).start();
    }
}