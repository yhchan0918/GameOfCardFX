package TCP1201;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.*;

public class Game {
    private ArrayList<Player> players;
    private int nameMaxLen; //For CLI purpose only, nice output
    private int phaseInt;

    public Game(String p1Name,String p2Name,String p3Name) {
        ArrayList<String> playersName = new ArrayList<>();
        playersName.add(p1Name);
        playersName.add(p2Name);
        playersName.add(p3Name);
        players = new ArrayList<Player>();
        nameMaxLen = 0;
        for (String playerName : playersName) {
            players.add(new Player(playerName));
            nameMaxLen = Math.max(playerName.length(), nameMaxLen);
        }
        phaseInt = 3;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getNameMaxLen() {
        return nameMaxLen;
    }

    public void dealCards() {
        Stack<Card> cards = Card.newShuffleCards();
        //temporary store cards for players
        Stack<Card>[] playersCards =  new Stack[players.size()];
        for (int i = 0; i < players.size(); i++)
            playersCards[i] = new Stack<Card>();

        //deal cards
        for(int i = 0; !cards.isEmpty(); i++)
            playersCards[i % players.size()].push(cards.pop());

        //player divide its own cards
        for(int i = 0; i < players.size(); i++)
            players.get(i).setCards(playersCards[i]);
    }

    public void showAvailableCard(){
        System.out.println("Available Cards:");
        int maxLen  = getNameMaxLen() + 1;

        for (Player p : getPlayers()) {
            int spaceLen = maxLen - p.getName().length();
            System.out.print(p.getName() + " ".repeat(spaceLen) + ":");
            System.out.print(PrintCard.avlCards(p.getCards()));
            System.out.println();
        }
    }


    public void removeWeakest() {
        ArrayList<Player> weakPlayers = new ArrayList<>();
        int lowestScore = Integer.MAX_VALUE;

        for (Player p : players) {
            if (p.getScore() <= lowestScore) {
                if (p.getScore() < lowestScore)
                    weakPlayers.clear();
                lowestScore = p.getScore();
                weakPlayers.add(p);
            }
        }

        //check if more than 1 worst player, if more than 1, proceed to else
        if (weakPlayers.size() == 1)
            players.remove(weakPlayers.get(0));
        else
        if (phaseInt == 3) //unlucky draw only for 3rd phase
            unluckyDraw(weakPlayers);
    }

    private void unluckyDraw(ArrayList<Player> weakPlayers) {
        System.out.println("Unlucky Draw Session");
        System.out.println("One of the weakest player will be remove");

        Random r = new Random();
        Player unluckyPlayer =  weakPlayers.get(r.nextInt(weakPlayers.size()));
        System.out.println("The unlucky player is " + unluckyPlayer.getName());

        players.remove(unluckyPlayer);
    }

    public void showNextPhase() {
        phaseInt--;

        System.out.print("****** ");
        System.out.print(players.get(0).getName() + " and " + players.get(1).getName());
        System.out.print(" proceed to 2-Player phase ");
        System.out.println("******\n\n");

        System.out.println("******************");
        System.out.println(" 2-Player Phase  *");
        System.out.println("******************");
    }

    public void showWinner() {
        System.out.print("****** ");
        System.out.print(players.get(0).getName() + " ");

        if(players.size() == 2)
            System.out.print("and " + players.get(1).getName() + " are ");
        else
            System.out.print("is ");

        System.out.print("the WINNER");

        if(players.size() == 2)
            System.out.print("S");

        System.out.println("! ******");
    }


}
