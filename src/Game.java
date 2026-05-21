import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Game {
    
int numberOfPlayers;
int activePlayerIndex = 0;
public ArrayList<Player> players;
ArrayList<Player> playersInRound;

Game(){
    players = new ArrayList<Player>();
}

public ArrayList<Player> getPlayers(){
    return players;
} 

public void startGame(){    //startet das Spiel, indem es alle wichtigen Informationen einholt
    Integer numberOfPlayersInteger = askForValidInt("Wie viele Spieler sollen teilnehmen? (2-10)", 2, 10);
    if(numberOfPlayersInteger == null){     //beendet das Programm im Falle eines Abbruches
        System.exit(0);
    }
    numberOfPlayers = numberOfPlayersInteger;   //der Fall numberOfPlayersInteger==null ist weg also kann der Wert in die eigentliche int
    String uebersichtPlayers = "Die Spieler: ";
    for (int i = 0; i < numberOfPlayers; i++){
        String playerName = JOptionPane.showInputDialog(    //Fragekasten mit Frage wie heißt du für jeden Spieler
                null,
                "Spieler "+(i+1)+", wie ist dein Name?"
            );
            if(playerName == null){
                players.clear();         //Im Falle eines Abbruchs wird das Programm neugestartet und die Liste wird gelöscht
                this.startGame();
                
            }
        Player player = new Player(playerName, i); //erstellt neues Objekt Spieler mit dem eingegebenen Namen
        players.add(player);
        Player p = (players.get(i));
        uebersichtPlayers = uebersichtPlayers + p.name;
        if(i != numberOfPlayers - 1){
            uebersichtPlayers = uebersichtPlayers + ", ";
        }
    }
    int antwort = JOptionPane.showConfirmDialog(
        null,
        uebersichtPlayers,
        "Kontrolle",
        JOptionPane.OK_CANCEL_OPTION
    );
    if(antwort == JOptionPane.CANCEL_OPTION){
        players.clear();
        startGame();
        
    }
    else if (antwort == JOptionPane.OK_OPTION){
        startRound();
    }
}
public void startRound(){
playersInRound = players;
Deck deck = new Deck();
deck.shuffledeck();
Middle middle = new Middle();
middle.takeCards(deck);
for(int i=0; i<numberOfPlayers; i++){
players.get(i).takeCards(deck);
System.out.println(determinedWinner(middle));
}
// gui Fenster öffnet sich (Spieler an der Reihe, Geld, Geboten, Geboten von anderen, )
// Spieler kommt an die Reihe (EInsatz + Kartensehen)
// Nächte Karte wird aufgedeckt
// Am Ende Sieger anzeigen

}
public Player determinedWinner(Middle middle){
    
    Player Winner = playersInRound.get(0);
    for(int i = 1; i < playersInRound.size(); i++){
        if(Winner.evaluate(middle.getMiddleCards()).getHandValue() < playersInRound.get(i).evaluate(middle.getMiddleCards()).getHandValue()){
            Winner = playersInRound.get(i);
        }
        else if(Winner.evaluate(middle.getMiddleCards()).getHandValue() == playersInRound.get(i).evaluate(middle.getMiddleCards()).getHandValue() && players.get(i).winsTieBreak(Winner.getTiebrakValues(), playersInRound.get(i).getTiebrakValues())){
            
        }
    }
    return Winner;
}

public Integer askForValidInt(String message, int min, int max){    // Gibt eine gültige Zahl in Form eines Integers oder im Falle des Abbruchs null zurück.
    while(true){ 
        String value = JOptionPane.showInputDialog(null, message);
            if(value == null){
                return null;
            }
            try {
        if (Integer.parseInt(value) <= max && Integer.parseInt(value) >= min){
            return Integer.parseInt(value);
        }
        else {
            JOptionPane.showMessageDialog(
                null,
                "Ungültige Eingabe. Bitte geben Sie eine Zahl zwischen " + min + " und " + max + " ein.",
                "Fehler",
                JOptionPane.ERROR_MESSAGE
            );
        }
        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(
                null,
                "Ungültige Eingabe. Bitte geben Sie eine Zahl ein.",
                "Fehler",
                JOptionPane.ERROR_MESSAGE);
     }
    
}
}
}