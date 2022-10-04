import java.util.HashMap;
import java.util.Random;
// import com.sun.org.apache.xpath.internal.operations.Bool;
public class Search {
    String name;
    int roll = 0;

    /**
     * @param upperbound
     * @return A random integer between 0 and (upperbound - 1), inclusive
     */
    static public int getRandInt(int upperbound) {
        Random randInt = new Random();
        return randInt.nextInt(upperbound);
    }
    
    public Boolean checkForTrap(Room room, Adventurer adv) {
        Treasure roomTreasure = room.treasure;
        if (roomTreasure.name == "Trap") {
            roomTreasure.treasureAction(adv);
            adv.board.unFoundTreasures.remove(roomTreasure);
            room.treasure = null;
            return true;
        }
        return false;
    }

    public HashMap<String, Boolean> loot(Adventurer adv, Room room) {
        String searchItem = "";
        Boolean searchSuccess = false;
        HashMap<String, Boolean> searchResult = new HashMap<String, Boolean>();
        if (room.treasure != null) {
            Treasure curTres = room.treasure;
            if(!checkForTrap(room, adv)) {
                roll = ((getRandInt(6) + 1) + (getRandInt(6) + 1));
                if (roll >= 10) {
                    adv.ownedTreasures.add(curTres);
                    adv.board.unFoundTreasures.remove(curTres);
                    room.treasure = null;
                    searchItem = "Treasure";
                    searchSuccess = true;
                    if(curTres.name != "Portal") {
                        curTres.treasureAction(adv);
                    }
                }
            }
            else { // the adventurer triggered a trap
                searchItem = "Trap";
                searchSuccess = true;
            }
        }
        searchResult.put(searchItem, searchSuccess);
        return searchResult;
    }
}


class Careful extends Search {
    Careful() {
        name = "Careful";
        roll = 0;
    }

    @Override
    public HashMap<String, Boolean> loot(Adventurer adv, Room room) {
        String searchItem = "";
        Boolean searchSuccess = false;
        HashMap<String, Boolean> searchResult = new HashMap<String, Boolean>();
        if (room.treasure != null) {
            Treasure curTres = room.treasure;
            if(!checkForTrap(room, adv)) {
                roll = ((getRandInt(6) + 1) + (getRandInt(6) + 1));
                if (roll >= 7) {
                    adv.ownedTreasures.add(curTres);
                    adv.board.unFoundTreasures.remove(curTres);
                    room.treasure = null;
                    searchItem = "Treasure";
                    searchSuccess = true;
                    if(curTres.name != "Portal") {
                        curTres.treasureAction(adv);
                    }
                }
            }
            else { // the adventurer triggered a trap
                searchItem = "Trap";
                searchSuccess = true;
            }
        }
        searchResult.put(searchItem, searchSuccess);
        return searchResult;
    }
}

class Quick extends Search {
    Quick() {
        name = "Careful";
    }

    @Override
    public HashMap<String, Boolean> loot(Adventurer adv, Room room) {
        String searchItem = "";
        Boolean searchSuccess = false;
        HashMap<String, Boolean> searchResult = new HashMap<String, Boolean>();
        if (Math.random() > 0.33) { // 33% chance of skipping search
            if (room.treasure != null) {
                Treasure curTres = room.treasure;
                if(!checkForTrap(room, adv)) {
                    roll = ((getRandInt(6) + 1) + (getRandInt(6) + 1));
                    if (roll >=9) {
                        adv.ownedTreasures.add(curTres);
                        adv.board.unFoundTreasures.remove(curTres);
                        room.treasure = null;
                        searchItem = "Treasure";
                        searchSuccess = true;
                        if(curTres.name != "Portal") {
                            curTres.treasureAction(adv);
                        }
                    }

                }
                else { // the adventurer triggered a trap
                    searchItem = "Trap";
                    searchSuccess = true;
                }
            }
        }
        searchResult.put(searchItem, searchSuccess);
        return searchResult;
    }
}

class Careless extends Search {
    Careless() {
        name = "Careless";
    }
}
