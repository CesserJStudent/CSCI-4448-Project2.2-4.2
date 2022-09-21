import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;

abstract class Adventurer {
    // ENCAPSULATION: Adventurer's attributes are only accessible within subclasses and their package
    protected GameBoard board;
    protected Integer[] location;
    protected int health = 3;
    protected int treasure = 0;
    protected String name;
    protected int turns = 1;

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
        location = adjRooms.get(chosenDirection);                                // updates location
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
     * Rolls two dice (1-6). If their sum is greater than 10 AND the current room has treasure, add treasure to the current adventurer
     * @param curRoom - A room object, needed to check whether treasure exists there or not
    */
    public int loot(Room curRoom) {
        if(curRoom.hasTreasure == true) {
            int lootCheck = (getRandInt(6) + 1) + (getRandInt(6) + 1);
            if (lootCheck >= 10) {
                treasure++;
                curRoom.hasTreasure = false;
                return(1);
            }
        }
        return(0);
    }

}

// INHERITANCE: All the subclasses that extend Adventurer
/**
 * An adventurer who gets +2 to each dice roll when fighting
*/
class Brawler extends Adventurer {
    Brawler(GameBoard gb, Integer[] loc) {
        board = gb;
        name = "B";
        location = loc;
    }
    public int fight() {
        // adds an extra +2 to each dice roll since this is a Brawler
        return ((getRandInt(6) + 3) + (getRandInt(6) + 3));
    };
}

/**
 * An adventurer who has a 50% chance not to fight creatures found in their room (implemented in GameRunner adventurerAction())
*/
class Sneaker extends Adventurer {
    Sneaker(GameBoard gb, Integer[] loc) {
        board = gb;
        name = "S";
        location = loc;
    }
}

/**
 * An adventurer who gets to perform two actions per turn
*/
class Runner extends Adventurer {
    Runner(GameBoard gb, Integer[] loc) {
        board = gb;
        name = "R";
        location = loc;
        turns = 2;
    }
}

/**
 * An adventurer who gets +1 to finding treasure and +1 to fighting
*/
class Thief extends Adventurer {
    Thief(GameBoard gb, Integer[] loc) {
        board = gb;
        name = "T";
        location = loc;
    }

    public int fight() {
        // adds +1 to the sum of the two dice rolls
        return ((getRandInt(6) + 1) + (getRandInt(6) + 1)) + 1;
    }

    public int loot(Room curRoom) {
        // adds +1 to the sum of the two rolls
        if(curRoom.hasTreasure == true) {
            int lootCheck = (getRandInt(6) + 1) + (getRandInt(6) + 1) + 1;
            if (lootCheck >= 10) {
                treasure++;
                curRoom.hasTreasure = false;
                return (1);
            }
        }
        return(0);
    }
}