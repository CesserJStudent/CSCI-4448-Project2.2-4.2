import java.util.HashMap;
import java.util.Random;
// import com.sun.org.apache.xpath.internal.operations.Bool;
//Search now includes a strategy pattern for looting
abstract class Search {
    String name;
    int skillCheck = 0;

    /**
     * @param upperbound
     * @return A random integer between 0 and (upperbound - 1), inclusive
     */
    static public int getRandInt(int upperbound) {
        Random randInt = new Random();
        return randInt.nextInt(upperbound);
    }
    
    private Boolean checkForTrap(Room room, Adventurer adv) { //check if there is a trap in the room and if there is, trigger it
        Treasure roomTreasure = room.treasure;
        if (roomTreasure.name == "Trap") {
            roomTreasure.treasureAction(adv);
            adv.board.unFoundTreasures.remove(roomTreasure);
            room.treasure = null;
            return true;
        }
        return false;
    }

    private Boolean checkForTreasure(Adventurer adv, Treasure tre) { //check if the treasure in the room is already owned by the adventurer
        for (int i = 0; i <adv.treasures.size(); i++) {
            if (adv.treasures.get(tre.name) == true) {
                return true;
            }
        }
        return false;
    }

    public HashMap<String, Boolean> loot(Adventurer adv, Room room) {
        String searchItem = "";
        Boolean searchSuccess = false;
        HashMap<String, Boolean> searchResult = new HashMap<String, Boolean>();
        if (room.treasure != null) { //check if there is a treasure in the room
            Treasure curTres = room.treasure;
            if(checkForTrap(room, adv)) { // the adventurer triggered a trap
                searchItem = "Trap";
                searchSuccess = true;
            }
            else {
                if(!checkForTreasure(adv, room.treasure)) { //check if the treasure is already owned by the adventurer
                    int roll = ((getRandInt(6) + 1) + (getRandInt(6) + 1));
                    if (roll >= skillCheck) {
                        room.treasure = null;
                        searchItem = "Treasure";
                        searchSuccess = true;
                        adv.board.unFoundTreasures.remove(curTres); //remove the treasure from the list of unowned treasures
                        curTres.treasureAction(adv);  //perform the action of the treasure, this also adds the treasure to the adventurer
                    }
                }
            }
        }
        searchResult.put(searchItem, searchSuccess);
        return searchResult;
    }
}


class Careful extends Search {
    Careful() {
        name = "Careful";
        skillCheck = 4;
    }
}

class Quick extends Search {
    Quick() {
        name = "Quick";
        skillCheck = 6;
    }
}

class Careless extends Search {
    Careless() {
        name = "Careless";
        skillCheck = 7;
    }
}
