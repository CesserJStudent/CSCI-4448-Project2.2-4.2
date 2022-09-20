import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;

abstract class Adventurer {
    GameBoard board;
    Integer[] location = {0, 1, 1};
    int health = 3;
    int treasure = 0;

    /**
     * @param upperbound
     * @return A random integer between 0 and (upperbound - 1), inclusive
    */
    static public int getRandInt(int upperbound) {
        Random randInt = new Random();
        return randInt.nextInt(upperbound);
    }

    /**
     * Helper function for move(), 
     * @param  adj - A HashMap containing all adjacent directions as keys, and an integer array as values i.e ("Up", {1, 0, 1})
     * @return An ArrayList of strings containing all possible travel directions
    */
    static public ArrayList<String> getValidDirections(HashMap<String, Integer[]> adj) {
        ArrayList<String> validDirections = new ArrayList<String>();
        for (String i : adj.keySet()) {     // traverses the adj HashMap. i is set to the key each loop
            if (adj.get(i) != null) {       // checks if the value associated with the key isn't null
                validDirections.add(i);     // adds the key i to validDirections. all possible keys: "Up", "Down", "Right", "Left", "Above", "Below"
            }
        }
        return validDirections;
    }

    /**
     * Updates the current location to a new location by choosing a random direction to travel
    */
    public void move() {
        HashMap<String, Integer[]> adjRooms = board.getAdjacentRooms(location);  // gets all adjacent rooms using a HashMap
        ArrayList<String> validDirections = getValidDirections(adjRooms);        // creates an ArrayList of strings containing all valid directions that can be traveled all possible values are ("Up", "Down", "Right", "Left", "Above", "Below")
        int randInt = getRandInt(validDirections.size());                        // chooses a random index to get a direction
        String chosenDirection = validDirections.get(randInt);
        board.getCurrentRoom(location).removeAdventurer(this);                   // removes the adventurer from the previous room before updating location
        for (int i = 0; i < 3; i++){
            location[i] = adjRooms.get(chosenDirection)[i];                      // updates location
        }
        board.getCurrentRoom(location).addAdventurer(this);                      // adds the adventurer to the room associated with location
    }

    /**
     * @return The sum of two 1-6 dice rolls
    */
    public int fight() {
        // getRandInt is called twice to simulate two dice rolls
        return ((getRandInt(6) + 1) + (getRandInt(6) + 1));
    }

    /**
     * Rolls two dice (1-6). If their sum is greater than 10, add treasure to the current adventurer
    */
    public void loot() {
        int lootCheck = (getRandInt(6) + 1) + (getRandInt(6) + 1);
        if (lootCheck >= 10) {
            treasure++;
        }
    }

}

/**
 * An adventurer who gets +2 to each dice roll when fighting
*/
class Brawler extends Adventurer {
    Brawler(GameBoard gb) {
        board = gb;
    }
    public int fight() {
        // adds an extra +2 to each dice roll since this is a Brawler
        return ((getRandInt(6) + 3) + (getRandInt(6) + 3));
    };
}

/**
 * An adventurer who has a 50% chance not to fight creatures found in their room (implement this somewhere else)
*/
class Sneaker extends Adventurer {
    Sneaker(GameBoard gb) {
        board = gb;
    }
}

/**
 * An adventurer who gets to perform two actions per turn (implement this somewhere else)
*/
class Runner extends Adventurer {
    Runner(GameBoard gb) {
        board = gb;
    }
}

/**
 * An adventurer who gets +1 to finding treasure and +1 to fighting
*/
class Thief extends Adventurer {
    Thief(GameBoard gb) {
        board = gb;
    }

    public int fight() {
        // adds +1 to the sum of the two dice rolls
        return ((getRandInt(6) + 1) + (getRandInt(6) + 1)) + 1;
    }

    public void loot() {
        // adds +1 to the sum of the two rolls
        int lootCheck = (getRandInt(6) + 1) + (getRandInt(6) + 1) + 1;
        if (lootCheck >= 10) {
            treasure++;
        }
    }
}