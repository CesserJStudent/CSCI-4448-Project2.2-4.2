import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;

abstract class Adventurer {
    // ENCAPSULATION: Adventurer's attributes are only accessible within subclasses and their package
    protected GameBoard board;
    protected Integer[] location;
    protected int health = 3;
    protected int treasure = 0;
    protected Boolean armed = false;
    protected Boolean cursed = false;
    protected Boolean armor = false;
    protected String name;
    protected ArrayList <Treasure> ownedTreasures = new ArrayList <Treasure>();
    protected Combat combat = null;
    protected Search search = null;


    HashMap<String, Boolean> treasures = new HashMap<String, Boolean>() {{
        put("Sword", false);
        put("Gem", false);
        put("Armor", false);
        put("Potion", false);
    }};


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
        board.getRoomAt(location).removeAdventurer(this);                   // removes the adventurer from the previous room before updating location
        location = adjRooms.get(chosenDirection);                                // updates location
        board.getRoomAt(location).addAdventurer(this);
        // adds the adventurer to the room associated with location
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
        combat = new Expert();
        search = new Careless();
    }
}

/**
 * An adventurer who has a 50% chance not to fight creatures found in their room (implemented in GameRunner adventurerAction())
*/
class Sneaker extends Adventurer {
    Sneaker(GameBoard gb, Integer[] loc) {
        board = gb;
        name = "S";
        location = loc;
        combat = new Stealth();
        search = new Quick();
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
        combat = new Untrained();
        search = new Quick();
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
        combat = new Trained();
        search = new Careful();
    }
}