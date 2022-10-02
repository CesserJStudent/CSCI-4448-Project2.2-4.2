import com.sun.org.apache.xpath.internal.operations.Bool;


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

    public void loot(Adventurer adv, Room room) {
        if (room.treasure != null) {
            Treasure curTres = room.treasure;
            if(!checkForTrap(room, adv)) {
                roll = ((adv.getRandInt(6) + 1) + (adv.getRandInt(6) + 1));
                if (roll >= 10) {
                    adv.ownedTreasures.add(curTres);
                    adv.board.unFoundTreasures.remove(curTres);
                    room.treasure = null;
                    if(curTres.name != "Portal") {
                        curTres.treasureAction(adv);
                    }

                }

            }
        }
    }
}


class Careful extends Search {
    Careful() {
        name = "Careful";
        roll = 0;
    }

    @Override
    public void loot(Adventurer adv, Room room) {
        if (room.treasure != null) {
            Treasure curTres = room.treasure;
            if(!checkForTrap(room, adv)) {
                roll = ((adv.getRandInt(6) + 1) + (adv.getRandInt(6) + 1));
                if (roll >= 7) {
                    adv.ownedTreasures.add(curTres);
                    adv.board.unFoundTreasures.remove(curTres);
                    room.treasure = null;
                    if(curTres.name != "Portal") {
                        curTres.treasureAction(adv);
                    }
                }

            }
        }
    }
}

class Quick extends Search {
    Quick() {
        name = "Careful";
    }

    @Override
    public void loot(Adventurer adv, Room room) {
        if (Math.random() > 0.33) { // 33% chance of skipping search
            if (room.treasure != null) {
                Treasure curTres = room.treasure;
                if(!checkForTrap(room, adv)) {
                    roll = ((adv.getRandInt(6) + 1) + (adv.getRandInt(6) + 1));
                    if (roll >=9) {
                        adv.ownedTreasures.add(curTres);
                        adv.board.unFoundTreasures.remove(curTres);
                        room.treasure = null;
                        if(curTres.name != "Portal") {
                            curTres.treasureAction(adv);
                        }
                    }

                }
            }
        }
    }
}

class Careless extends Search {
    Careless() {
        name = "Careless";
    }
}
