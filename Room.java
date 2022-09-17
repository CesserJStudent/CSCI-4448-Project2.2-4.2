import java.util.HashMap;
public class Room {
    HashMap<String, Integer[]> adjacent = new HashMap<String, Integer[]>(); //hash map holds direction elements
    Room(Integer[] up, Integer[] down, Integer[] right, Integer[] left, Integer[] above, Integer[] below) {
        adjacent.put("Up", up);
        adjacent.put("Down", down);
        adjacent.put("Right", right);
        adjacent.put("Left", left);
        adjacent.put("Above", above);
        adjacent.put("Below", below);
    }
}
