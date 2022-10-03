// import com.sun.org.apache.xpath.internal.operations.Bool;
public class Search {
    String name;
    int roll = 0;

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

    public Boolean loot(Adventurer adv, Room room) {
        Boolean treasureFound = false;
        if (room.treasure != null) {
            Treasure curTres = room.treasure;
            if(!checkForTrap(room, adv)) {
                roll = ((adv.getRandInt(6) + 1) + (adv.getRandInt(6) + 1));
                if (roll >= 10) {
                    adv.ownedTreasures.add(curTres);
                    adv.board.unFoundTreasures.remove(curTres);
                    room.treasure = null;
                    treasureFound = true;
                    if(curTres.name != "Portal") {
                        curTres.treasureAction(adv);
                    }
                }
            }
        }
        return treasureFound;
    }
}


class Careful extends Search {
    Careful() {
        name = "Careful";
        roll = 0;
    }

    @Override
    public Boolean loot(Adventurer adv, Room room) {
        Boolean treasureFound = false;
        if (room.treasure != null) {
            Treasure curTres = room.treasure;
            if(!checkForTrap(room, adv)) {
                roll = ((adv.getRandInt(6) + 1) + (adv.getRandInt(6) + 1));
                if (roll >= 7) {
                    adv.ownedTreasures.add(curTres);
                    adv.board.unFoundTreasures.remove(curTres);
                    room.treasure = null;
                    treasureFound = true;
                    if(curTres.name != "Portal") {
                        curTres.treasureAction(adv);
                    }
                }
            }
        }
        return treasureFound;
    }
}

class Quick extends Search {
    Quick() {
        name = "Careful";
    }

    @Override
    public Boolean loot(Adventurer adv, Room room) {
        Boolean treasureFound = false;
        if (Math.random() > 0.33) { // 33% chance of skipping search
            if (room.treasure != null) {
                Treasure curTres = room.treasure;
                if(!checkForTrap(room, adv)) {
                    roll = ((adv.getRandInt(6) + 1) + (adv.getRandInt(6) + 1));
                    if (roll >=9) {
                        adv.ownedTreasures.add(curTres);
                        adv.board.unFoundTreasures.remove(curTres);
                        room.treasure = null;
                        treasureFound = true;
                        if(curTres.name != "Portal") {
                            curTres.treasureAction(adv);
                        }
                    }

                }
            }
        }
        return treasureFound;
    }
}

class Careless extends Search {
    Careless() {
        name = "Careless";
    }
}
