import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;

public class Simulation {
public static int NUM_PLAYERS = 120;
public static int NUM_SIMULATIONS = 1000;
public static int NUM_SEATS = 8; //seats per table for an individual game
public static int ROUNDS = 7; //rounds in the first stage
public static int[] POINT_DISTRIBUTION = {10,8,6,5,3,2,1,1};

private static ArrayList<Integer> topScores; //
private static int total; //total of adding all the scores. Update this as the program runs to save on speed

    public static void main(String[] args) {
        try {
            NUM_SIMULATIONS = Integer.parseInt(JOptionPane.showInputDialog("How many simulations do you want to run?"));
            NUM_PLAYERS = Integer.parseInt(JOptionPane.showInputDialog("How many players do you want to test with?"));
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "You didn't write a number. Exiting.");
            return;
        }
        
        
        
        topScores = new ArrayList<Integer>(NUM_SIMULATIONS);
        for (int i = 0; i < NUM_SIMULATIONS; i++) {
            //run simulations
            Integer[] players = new Integer[NUM_PLAYERS];
            Arrays.fill(players, 0); //Integer array starts as NULL, we want it to start as 0
            runSimulation(players);
            Arrays.sort(players);
            //We only care about the cutoff point, 
            //so we'll only consider the "last place" person who passes to the second round
            total += players[NUM_PLAYERS - 32];
            topScores.add(players[NUM_PLAYERS - 32]);
        }
        String message = "Average Score: " + getAverage() + " Median Score: " + getMedian();
        JOptionPane.showMessageDialog(null, message);
    }
    
    /**
     * Runs one simulation with NUM_PLAYERS players
     * @param players
     */
    private static void runSimulation(Integer[] players) {
        List<Integer> intList = Arrays.asList(players);
        for (int i =0; i < ROUNDS; i++) {
            Collections.shuffle(intList); //while not realistic, we will take this random ordering of players and correlate their
                                           //position to the game to their score. This is random as all players have equal skill.
            for (int j =0; j < intList.size(); j += NUM_SEATS) {
                oneGame(intList, j);
            }
        }
        intList.toArray(players);
    }
    
    /**
     * Runs one instance of a game with NUM_SEATS players
     * @param players the players who will be in this game
     */
    private static void oneGame(List<Integer> players, int startIndex) {
        for (int i = 0; i < NUM_SEATS; i++) {
            players.set(i, players.get(i) + POINT_DISTRIBUTION[i]);
        }
    }
    
    /**
     * Pre: At least one simulation has run
     * @return the average of the scores calculated so far
     */
    private static int getAverage() {
        return total / topScores.size();
    }
    
    /**
     * Pre: topScores not empty
     * @returnthe median of the topScore array
     */
    private static int getMedian() {
        
        return topScores.get(topScores.size() / 2);
    }
    
}