import java.util.HashMap;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Random;

abstract class Creature {
    GameBoard board;
    int level;
    ArrayList<Integer[]> validRooms;
    Integer[] location;

    /**
     * @param upperbound
     * @return A random integer between 0 and (upperbound - 1), inclusive
    */
    static public int getRandInt(int upperbound) {
        Random randInt = new Random();
        return randInt.nextInt(upperbound);
    }

    public ArrayList<Integer[]> getValidRooms(int numLevels, int currentLevel) {
        ArrayList<Integer[]> rooms = new ArrayList<Integer[]>();
        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 3; k++) {
                Integer[] currRoom = {currentLevel, j, k};
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

    public abstract void move();
}

class Orbiter extends Creature {
    Orbiter(GameBoard gb) {
        board = gb;
        level = getRandInt(4) + 1;
        validRooms = getValidRooms(4, level);     // sets validRooms to the ArrayList gatValidRooms returns. this varies by creature and starting level
        location = validRooms.get(getRandInt(validRooms.size()));  // sets the starting location to a random valid room
        board.getCurrentRoom(location).addCreature(this);
    }

    public ArrayList<Integer[]> getValidRooms(int numLevels, int level) {
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

    // since movement doesn't require rooms to be connected, simply pick another valid room and move there
    public void move() {
        int validRoomIndex = getRandInt(validRooms.size());
        if (Arrays.equals(location, validRooms.get(validRoomIndex))) {
            move(); // if the previous location is equal to the new location at validRoomIndex, call the function again
        }
        else {
            board.getCurrentRoom(location).removeCreature(this);
            location = validRooms.get(validRoomIndex); // sets location to the new location at validRoomIndex
            board.getCurrentRoom(location).addCreature(this);
        }
    }
}

class Seeker extends Creature {
    Seeker(GameBoard gb){
        board = gb;
        level = getRandInt(4) + 1;
        validRooms = getValidRooms(4, level);     // sets validRooms to the ArrayList gatValidRooms returns. this varies by creature and starting level
        location = validRooms.get(getRandInt(validRooms.size()));  // sets the starting location to a random valid room
        board.getCurrentRoom(location).addCreature(this);
    }

    // helper function for move
    private ArrayList<Integer[]> getPossibleRooms() {
        ArrayList<Integer[]> possibleRooms = new ArrayList<Integer[]>();
        HashMap<String, Integer[]> adjRooms = board.getAdjacentRooms(location);  // gets all adjacent rooms using a HashMap
        for (String i : adjRooms.keySet()) {     // traverses the adjRooms HashMap. i is set to the key each loop
            if (adjRooms.get(i) != null && i != "Above" && i != "Below") {       // checks if the value associated with the key isn't null, as well as making sure the room isn't on another level
                Boolean adventurerPresent = board.getCurrentRoom(adjRooms.get(i)).checkForAdventurer();
                if (adventurerPresent) {
                    possibleRooms.add(adjRooms.get(i));
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
            board.getCurrentRoom(location).removeCreature(this);    // removes the creature from the room it's about to leave
            int validRoomIndex = getRandInt(possibleRooms.size());
            location = possibleRooms.get(validRoomIndex);           // updates the creature's location and adds it to the room associated with location
            board.getCurrentRoom(location).addCreature(this);
        }
    }
}

class Blinker extends Creature {
    Blinker(GameBoard gb){
        board = gb;
        level = 4;
        validRooms = getAllRooms(4);
        ArrayList<Integer[]> startingRooms = getValidRooms(4, level); // initializes startingRooms using getValidRooms. this contains only rooms on level 4
        location = startingRooms.get(getRandInt(startingRooms.size()));         // sets the starting location to a random room from startingRooms
        board.getCurrentRoom(location).addCreature(this);
        startingRooms = null;   // sets startingRooms to null, since it was only needed for the initialization of Blinker
    }

    /**
     * Adds all possible rooms the creature can inhabit to their validRooms attribute
     * @param numLevels - The number of levels inside the facility
    */
    public ArrayList<Integer[]> getAllRooms(int numLevels) {
        ArrayList<Integer[]> allRooms = new ArrayList<Integer[]>();
        for (int i = 1; i < numLevels; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    Integer[] currRoom = {i, j, k};
                    allRooms.add(currRoom);
                }
            }
        }
        return allRooms;
    }

    public void move(){
        int validRoomIndex = getRandInt(validRooms.size());
        if (Arrays.equals(location, validRooms.get(validRoomIndex))) {
            move(); // if the previous location is equal to the new location at validRoomIndex, call the function again. (it wouldn't go anywhere otherwise)
        }
        else {
            board.getCurrentRoom(location).removeCreature(this);
            location = validRooms.get(validRoomIndex); // sets location to the new location at validRoomIndex
            board.getCurrentRoom(location).addCreature(this);
        }
    }
}