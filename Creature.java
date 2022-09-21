import java.util.HashMap;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Random;

abstract class Creature {
    protected GameBoard board;
    protected ArrayList<Integer[]> validRooms;
    protected Integer[] location;
    protected String name;

    /**
     * @param upperbound
     * @return A random integer between 0 and (upperbound - 1), inclusive
    */
    static public int getRandInt(int upperbound) {
        Random randInt = new Random();
        return randInt.nextInt(upperbound);
    }

    /**
     * @param level
     * @return An ArrayList containing all valid rooms a creature can inhabit on the specified level
    */
    public ArrayList<Integer[]> getValidRooms(int level) {
        ArrayList<Integer[]> rooms = new ArrayList<Integer[]>();
        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 3; k++) {
                Integer[] currRoom = {level, j, k};
                rooms.add(currRoom);
            }
        }
        return rooms;
    }

    /**
     * @return The sum of two 1-6 dice rolls
    */
    public int fight() {
        // getRandInt is called twice to simulate two dice rolls
        return ((getRandInt(6) + 1) + (getRandInt(6) + 1));
    }

    /**
     * Moves a creature from its current room to another valid room. Valid rooms vary by creature.
     * Orbiter - any outer room on their current level
     * Seeker - an adjacent room containing an adventurer
     * Blinker - any room on any level except for 0-1-1
    */
    public void move() {
        int validRoomIndex = getRandInt(validRooms.size());
        if (Arrays.equals(location, validRooms.get(validRoomIndex))) {
            move(); // if the previous location is equal to the new location at validRoomIndex, call the function again. (it wouldn't go anywhere otherwise)

        }
        else {
            board.getRoomAt(location).removeCreature(this); // removes the creature from the room it's about to leave
            location = validRooms.get(validRoomIndex); // sets location to the new location at validRoomIndex
            board.getRoomAt(location).addCreature(this); //add to new location
        }
    }
}

/**
 * A creature who circles the outer rooms of the level they are on, whether connected or not. They will always start in an outer room on any of the four levels.
*/
class Orbiter extends Creature {
    Orbiter(GameBoard gb, Integer[] loc) {
        board = gb;
        validRooms = getValidRooms(loc[0]);     // sets validRooms to the ArrayList gatValidRooms returns. this varies by creature and starting level
        location = loc;  // sets the starting location to a random valid room
        name = "OB"; //set name
    }

    public ArrayList<Integer[]> getValidRooms(int level) {
        ArrayList<Integer[]> rooms = new ArrayList<Integer[]>();
        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 3; k++) {
                if (!(j == 1 && k == 1)) {  // this condition is only true in center rooms, which orbiters cannot reach
                    Integer[] currRoom = {level, j, k};
                    rooms.add(currRoom);
                }
            }
        }
        return rooms;
    }
}

class Seeker extends Creature {
    Seeker(GameBoard gb, Integer[] loc){
        board = gb;
        validRooms = getValidRooms(loc[0]);     // sets validRooms to the ArrayList gatValidRooms returns. this varies by creature and starting level
        location = loc;  // sets the starting location to a random valid room
        name = "SK"; //set name
    }

    /**
     * Helper function for Seeker.move(). Checks all adjacent rooms for adventurers.
     * @return An ArrayList containing all possible rooms a Seeker can currently move to. If the ArrayList was empty returns null instead
    */
    private ArrayList<Integer[]> getPossibleRooms() {
        ArrayList<Integer[]> possibleRooms = new ArrayList<Integer[]>();
        HashMap<String, Integer[]> adjRooms = board.getAdjacentRooms(location);  // gets all adjacent rooms using a HashMap
        for (String i : adjRooms.keySet()) {                                     // traverses the adjRooms HashMap. i is set to the key each loop
            if (adjRooms.get(i) != null && i != "Above" && i != "Below") {       // checks if the value associated with the key isn't null, as well as making sure the room isn't on another level
                Boolean adventurerPresent = board.getRoomAt(adjRooms.get(i)).hasAdventurer(); // checks for adventurers in the current adjacent room
                if (adventurerPresent) {
                    possibleRooms.add(adjRooms.get(i)); // adds the adjacent room to possibleRooms if an adventurer was found
                }
            }
        }
        if (possibleRooms.isEmpty()) {
            return null;
        }
        else {
            return possibleRooms;
        }
    }

    public void move(){
        ArrayList<Integer[]> possibleRooms = getPossibleRooms();
        if (possibleRooms != null) {
            board.getRoomAt(location).removeCreature(this);    // removes the creature from the room it's about to leave
            int validRoomIndex = getRandInt(possibleRooms.size());
            location = possibleRooms.get(validRoomIndex);           // updates the creature's location and adds it to the room associated with location
            board.getRoomAt(location).addCreature(this);
        }
    }
}

class Blinker extends Creature {
    Blinker(GameBoard gb, Integer[] loc){
        board = gb;
        validRooms = getAllRooms(4);
        location = loc; // sets the starting location to a random room from startingRooms
        name = "BK"; //set name
    }

    /**
     * Adds all possible rooms the creature can inhabit to their validRooms attribute
     * @param numLevels - The number of levels inside the facility
    */
    private ArrayList<Integer[]> getAllRooms(int numLevels) {
        ArrayList<Integer[]> allRooms = new ArrayList<Integer[]>();
        for (int i = 1; i <= numLevels; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    Integer[] currRoom = {i, j, k};
                    allRooms.add(currRoom);
                }
            }
        }
        return allRooms;
    }
}