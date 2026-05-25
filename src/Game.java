import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Game {

    int numberOfPlayers;
    int activePlayerIndex = 0;
    public ArrayList<Player> players;
    ArrayList<Player> playersInRound;

    Game() {
        players = new ArrayList<Player>();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void startGame() {
        Integer numberOfPlayersInteger = askForValidInt("Wie viele Spieler sollen teilnehmen? (2-10)", 2, 10);
        if (numberOfPlayersInteger == null) {
            System.exit(0);
        }
        numberOfPlayers = numberOfPlayersInteger;
        String uebersichtPlayers = "Die Spieler: ";
        for (int i = 0; i < numberOfPlayers; i++) {
            String playerName = JOptionPane.showInputDialog(null,"Spieler " + (i + 1) + ", wie ist dein Name?");
            if (playerName == null) {
                players.clear();
                this.startGame();
                return;
            }
            Player player = new Player(playerName, i);
            players.add(player);
            uebersichtPlayers += player.name;
            if (i != numberOfPlayers - 1) {
                uebersichtPlayers += ", ";
            }
        }
        int antwort = JOptionPane.showConfirmDialog(null,uebersichtPlayers,"Kontrolle",JOptionPane.OK_CANCEL_OPTION);
        if (antwort == JOptionPane.CANCEL_OPTION) {
            players.clear();
            startGame();
        } else if (antwort == JOptionPane.OK_OPTION) {
            startRound();
        }
    }

    public void startRound() {
        playersInRound = new ArrayList<>(players);
        Deck deck = new Deck();
        deck.shuffledeck();
        Middle middle = new Middle();
        middle.takeCards(deck);
        for (int i = 0; i < numberOfPlayers; i++) {
            players.get(i).takeCards(deck);
        }
        System.out.println(determinedWinner(middle).name);
    }

    public Player determinedWinner(Middle middle) {
        Player winner = playersInRound.get(0);

        winner.evaluate(middle.getMiddleCards()); // evaluate best hand of first player

        for (int i = 1; i < playersInRound.size(); i++) {
            Player playerToEvaluate = playersInRound.get(i);
            playerToEvaluate.evaluate(middle.getMiddleCards());

            int winnerHandValue = winner.evaluate(middle.getMiddleCards()).getHandValue();
            int currentHandValue = playerToEvaluate.evaluate(middle.getMiddleCards()).getHandValue();

            if (currentHandValue > winnerHandValue) {
                winner = playerToEvaluate;
            } else if (currentHandValue == winnerHandValue) {
                int[] winnerTiebreak = winner.getTiebreakValues();
                int[] playerToEvaluateTiebreak = playerToEvaluate.getTiebreakValues();
                if (playerToEvaluate.winsTiebreak(winnerTiebreak, playerToEvaluateTiebreak)) {
                    winner = playerToEvaluate;
                }
            }
        }
        return winner;
    }

    public Integer askForValidInt(String message, int min, int max) {
        while (true) {
            String value = JOptionPane.showInputDialog(null, message);
            if (value == null) {
                return null;
            }
            try {
                int parsed = Integer.parseInt(value);
                if (parsed <= max && parsed >= min) {
                    return parsed;
                } else {JOptionPane.showMessageDialog(null,"Ungültige Eingabe. Bitte geben Sie eine Zahl zwischen " + min + " und " + max + " ein.","Fehler",JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,"Ungültige Eingabe. Bitte geben Sie eine Zahl ein.","Fehler",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}