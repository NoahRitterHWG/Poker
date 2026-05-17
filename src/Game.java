import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Game {
    
int numberOfPlayers;
int activePlayerIndex = 0;
ArrayList<Player> players;
Game(){
    players = new ArrayList<Player>();
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
Deck deck = new Deck();
deck.shuffledeck();
Middle middle = new Middle();
middle.takeCards(deck);
for(int i=0; i<numberOfPlayers; i++){
Player p = players.get(i);
p.takeCards(deck);
}
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
public int checkHand(Player p, Middle m){   //deck.cards.get(0).value   Einer- und Zehnerstelle für Wert der kleinsten Zahl, soweiter, 10000000000er Stelle für Wert der Hand
    int points = 0;
    Card c1 = p.getHandCard(0);
    Card c2 = p.getHandCard(1);
    Card c3 = m.getUncovertCards(2);
    Card c4 = m.getUncovertCards(3);
    Card c5 = m.getUncovertCards(4);
    Card c6 = m.getUncovertCards(5);
    Card c7 = m.getUncovertCards(6);
    ArrayList<Card> hand = new ArrayList<Card>();
    hand.add(c1);
    hand.add(c2);
    hand.add(c3);
    hand.add(c4);
    hand.add(c5);
    hand.add(c6);
    hand.add(c7);
    int gleicheH = 0;
    int gleicheC = 0;
    int gleicheD = 0;
    int gleicheS = 0;
    int two = 0;
    int three = 0;
    int four = 0;
    int five = 0;
    int six = 0;
    int seven = 0;
    int eight = 0;
    int nine = 0;
    int ten = 0;
    int J = 0;
    int Q = 0;
    int K = 0;
    int A = 0;
    for(int i=0; i<7; i++){
        CardValues c = hand.get(i).value;
    
        if(hand.get(i).type == CardType.CLUBS){
            gleicheC ++;
        }
        else if(hand.get(i).type == CardType.HEARTS){
            gleicheH ++;
        }
        else if(hand.get(i).type == CardType.DAIMONDS){
            gleicheD ++;
        }
        else if(hand.get(i).type == CardType.SPADES){
            gleicheS ++;
        }
        if(c == CardValues.TWO){
            two++;
        }
        else if(c == CardValues.THREE){
            three++;
        }
        else if(c == CardValues.FOUR){
            four++;
        }
        else if(c == CardValues.FIVE){
            five++;
        }
        else if(c == CardValues.SIX){
            six++;
        }
        else if(c == CardValues.SEVEN){
            seven++;
        }
        else if(c == CardValues.EIGHT){
            eight++;
        }
        else if(c == CardValues.NINE){
            nine++;
        }
        else if(c == CardValues.TEN){
            ten++;
        }
        else if(c == CardValues.J){
            J++;
        }
        else if(c == CardValues.Q){
            Q++;
        }
        else if(c == CardValues.K){
            K++;
        }
        else if(c == CardValues.A){
            A++;
        }
        
    }
    if(gleicheC>4 || gleicheH>4 || gleicheD>4 || gleicheS>4){

    }
   
    return points;
}
}