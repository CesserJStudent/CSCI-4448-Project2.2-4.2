import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.HashMap;
import java.util.ArrayList;
public class Room {
    ArrayList<Adventurer> adventurersPresent = new ArrayList<Adventurer>(); //list of adventurers in room
    ArrayList<Creature> creaturesPresent = new ArrayList<Creature>(); //list of creatures in room

    Boolean hasTreasure = true; //if room has treasure

    HashMap<String, Integer[]> adjacent = new HashMap<String, Integer[]>(); //hash map holds direction elements
    Room(Integer[] up, Integer[] down, Integer[] right, Integer[] left, Integer[] above, Integer[] below) {
        adjacent.put("Up", up);
        adjacent.put("Down", down);
        adjacent.put("Right", right);
        adjacent.put("Left", left);
        adjacent.put("Above", above);
        adjacent.put("Below", below);
    }

    public void addAdventurer(Adventurer adv) {
        adventurersPresent.add(adv);
    } //adds adventurer to room

    public void addCreature(Creature cre) {
        creaturesPresent.add(cre);
    } //adds creature to room

    public void removeAdventurer(Adventurer adv) {
        adventurersPresent.remove(adv); //removes adventurer from room
    }

    public void removeCreature(Creature cre) {
        creaturesPresent.remove(cre); //removes creature from room
    }

    public boolean checkForAdventurer() { //checks if room has adventurer
        if (adventurersPresent.isEmpty()){
            return false;
        }
        else {
            return true;
        }
    }

    public boolean checkForCreature() { //checks if room has creature
        if (creaturesPresent.isEmpty()){
            return false;
        }
        else {
            return true;
        }
    }
}
