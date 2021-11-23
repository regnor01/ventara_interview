
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
public static int CUTOFFPLACE = 32; //this is the last place that advances to the next round

private static ArrayList<Integer> topScores; //
private static int total; //total of adding all the scores. Update this as the program runs to save on speed

    public static void main(String[] args) {
        int changePoints = -1;
        String errorMessage = "Invalid Input. Please try again";
        try {
            
            int skip = JOptionPane.showConfirmDialog(null, "Run with defaults?", ""
                + "Point Distribution", JOptionPane.YES_NO_OPTION);
            if (skip == JOptionPane.CLOSED_OPTION) {
                System.exit(0);
            }
            if (skip == 1) {
                
                NUM_SIMULATIONS = getInputFromUser("How many simulations do you want to run?");
                
                NUM_PLAYERS = getInputFromUser("How many players do you want to test with?");
                boolean invalid = true;
                while (invalid) {
                    NUM_SEATS = getInputFromUser("How many players per game?");
                    if (NUM_SEATS > NUM_PLAYERS) {
                        errorMessage = "Cannot have more seats than players per game.";
                        JOptionPane.showMessageDialog(null, errorMessage);
                    }
                    else {
                        invalid = false;
                    }
                }
                invalid = true;
                ROUNDS = getInputFromUser("How many rounds per simulation?");
                while (invalid) {
                    CUTOFFPLACE = getInputFromUser("How many players can advance to the second stage?");
                    if (CUTOFFPLACE > NUM_PLAYERS) {
                        errorMessage = "Cannot have more players advancing than players playing.";
                        JOptionPane.showMessageDialog(null, errorMessage);
                    }
                    else {
                        invalid = false;
                    }
                }
                
                
                changePoints = JOptionPane.showConfirmDialog(null, "Do you want to change the point distribution from the default?", ""
                    + "Point Distribution", JOptionPane.YES_NO_OPTION);
            }
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, errorMessage);
            return;
        }
        
        POINT_DISTRIBUTION = Arrays.copyOf(POINT_DISTRIBUTION, NUM_SEATS); //Assuming zero for places after 8 unless contradicted
        if (changePoints == 0) {
            changePointDistribution();
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
            total += players[NUM_PLAYERS - CUTOFFPLACE];
            topScores.add(players[NUM_PLAYERS - CUTOFFPLACE]);
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
                                            //If the number of players is not evenly divisible by number of seats, num_players % num_seats
                                            //players will earn 0 points per round.
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
    
    /**
     * Changes point distribution by user input
     */
    private static void changePointDistribution() {
        for (int i = 0; i < NUM_SEATS; i++) {
            int hasCompleted = 0;
            while (hasCompleted == 0) {
                try {
                    String response = JOptionPane.showInputDialog("Please enter "
                        + "the number of points for "+ changePlaceToWord(i + 1) + " place.");
                    if (response == null) {
                        System.exit(0);
                    }
                    POINT_DISTRIBUTION[i] = Integer.parseInt(response);
                    hasCompleted = 1;
                }
                catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Could not parse number. Please try again.");
                }
            }
        }
    }
    
    /**
     * Takes in a number and returns it as adjective
     * @param place 
     * @return 1st is place == 1, 2nd if place == 2, get the idea
     */
    private static String changePlaceToWord(int place) {
        int leastSignificant = place % 10;
        if ((place / 10) % 10 == 1) {
            return place + "th"; //handles 11-19 
        }
        switch(leastSignificant) {
            case 1:
                return place + "st";
            case 2:
                 return place + "nd";
            case 3:
                return place + "rd";
            default:
                return place + "th";
        }
    }
    
    /**
     * Grabs a certain number from the user. Number can't be negative
     * @param question the string displayed to the user
     * @return the number to user entered
     */
    private static int getInputFromUser(String question) {
        
        int result = -1;
        boolean inputWorked = false;
        while (!inputWorked) {
            try {
                String status = JOptionPane.showInputDialog(question);
                if (status == null) {
                    System.exit(0);
                }
                result = Integer.parseInt(status);
                if (result < 0) {
                    JOptionPane.showMessageDialog(null, "Please enter only positive numbers for this value.");
                }
                else {
                    inputWorked = true;
                }
            }
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please try again.");
            }
        }
        return result;
    }
    
}
