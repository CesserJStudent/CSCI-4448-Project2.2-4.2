import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

abstract class Treasure {
    //Abstract method for treasure actions
    public abstract void treasureAction(Adventurer adv);

    public String name;

    public void addTreasure(Adventurer adv) {
        //add treasure to adventurer
        adv.treasures.replace(name, true);

    }
    /**
     * @param upperbound
     * @return A random integer between 0 and (upperbound - 1), inclusive
     */
    static public int getRandInt(int upperbound) {
        Random randInt = new Random();
        return randInt.nextInt(upperbound);
    }
}

class Sword extends Treasure {

    Sword() {
        name = "Sword";
    }
    //Sword class
    public void treasureAction(Adventurer adv) {
        //Sword action
        adv.armed = true;
        adv.treasures.replace(name, true);
    }
}

class Gem extends Treasure {

    Gem() {
        name = "Gem";
    }
    //Gem class
    public void treasureAction(Adventurer adv) {
        //gem action
        adv.cursed = true;
        adv.treasures.replace(name, true);
    }
}

class Armor extends Treasure {

    Armor() {
        name = "Armor";
    }
    public void treasureAction(Adventurer adv) {
        //Armor action
        adv.armor = true;
    }
}


class Portal extends Treasure {

    Portal() {
        name = "Portal";
    }
    //Portal class
    public void treasureAction(Adventurer adv) {
        //Portal allows to move to any random room
        ArrayList<Integer[]> allRooms = new ArrayList<Integer[]>();
        for (int i = 1; i <= 4; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    Integer[] currRoom = {i, j, k};
                    allRooms.add(currRoom);
                }
            }
        }

        int validRoomIndex = getRandInt(allRooms.size());
        if (Arrays.equals(adv.location, allRooms.get(validRoomIndex))) {
            treasureAction(adv); // if the previous location is equal to the new location at validRoomIndex, call the function again. (it wouldn't go anywhere otherwise)

        }
        else {
            adv.board.getRoomAt(adv.location).removeAdventurer(adv); // removes the creature from the room it's about to leave
            adv.location = allRooms.get(validRoomIndex); // sets location to the new location at validRoomIndex
            adv.board.getRoomAt(adv.location).addAdventurer(adv); //add to new location
        }
    }
}

class Trap extends Treasure {

    Trap() {
        name = "Trap";
    }
    //Sword class
    public void treasureAction(Adventurer adv) {
        //Trap action
        if (adv.search.name == "Careful") {
            if(getRandInt(1) == 0) {
                ;
            }
            else {
                adv.health -= 1;
            }
        }

        else {
            adv.health -= 1;
        };
    }
}

class Potion extends Treasure {

    Potion() {
        name = "Potion";
    }
    //Potion class
    public void treasureAction(Adventurer adv) {
        //Potions give an extra health
        adv.health += 1;
        adv.treasures.replace(name, true);
    }
}