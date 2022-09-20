import java.util.HashMap;
import java.util.ArrayList;
public class Room {
    ArrayList<Adventurer> adventurersPresent = new ArrayList<Adventurer>();
    ArrayList<Creature> creaturesPresent = new ArrayList<Creature>();

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
    }

    public void addCreature(Creature cre) {
        creaturesPresent.add(cre);
    }

    public void removeAdventurer(Adventurer adv) {
        // needs to be implemented
    }

    public void removeCreature(Creature cre) {
        // needs to be implemented
    }

    public boolean checkForAdventurer() {
        if (adventurersPresent.isEmpty()){
            return false;
        }
        else {
            return true;
        }
    }

    private boolean checkForCreature() {
        if (creaturesPresent.isEmpty()){
            return false;
        }
        else {
            return true;
        }
    }

    public boolean combatCheck() {
        return checkForAdventurer() && checkForCreature();
    }
}
