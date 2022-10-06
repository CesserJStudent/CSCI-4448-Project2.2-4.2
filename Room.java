// import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.HashMap;
import java.util.ArrayList;
public class Room {
    ArrayList<Adventurer> adventurersPresent = new ArrayList<Adventurer>(); //list of adventurers in room
    ArrayList<Creature> creaturesPresent = new ArrayList<Creature>(); //list of creatures in room
    HashMap<String, Integer[]> adjacent = new HashMap<String, Integer[]>(); //hash map holds direction elements
    Treasure treasure = null; //treasure in room if any

    Room(Integer[] up, Integer[] down, Integer[] right, Integer[] left, Integer[] above, Integer[] below) {
        adjacent.put("Up", up);
        adjacent.put("Down", down);
        adjacent.put("Right", right);
        adjacent.put("Left", left);
        adjacent.put("Above", above);
        adjacent.put("Below", below);
    }

    /**
     * Adds an adventurer to a room
     * @param adv The Adventurer object to be added.
    */
    public void addAdventurer(Adventurer adv) {
        adventurersPresent.add(adv);
    }

    /**
     * Adds a creature to a room
     * @param cre The Creature object to be added.
    */
    public void addCreature(Creature cre) {
        creaturesPresent.add(cre);
    }

    /**
     * Removes an adventurer from a room
     * @param adv The Adventurer object to be removed.
    */
    public void removeAdventurer(Adventurer adv) {
        adventurersPresent.remove(adv);
    }

    /**
     * Removes a creature from a room
     * @param cre The Creature object to be removed.
    */
    public void removeCreature(Creature cre) {
        creaturesPresent.remove(cre);
    }

    /**
     * Checks if a room has an adventurer
     * @return A boolean that's true if the room has an adventurer, otherwise false.
    */
    public boolean hasAdventurer() {
        if (adventurersPresent.isEmpty()){
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Checks if a room has a creature
     * @return A boolean that's true if the room has a creature, otherwise false.
    */
    public boolean hasCreature() {
        if (creaturesPresent.isEmpty()){
            return false;
        }
        else {
            return true;
        }
    }
}
