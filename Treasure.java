import java.util.Arrays;
import java.util.Random;

abstract class Treasure {
    public String name;

    /**
     * @param upperbound
     * @return A random integer between 0 and (upperbound - 1), inclusive
     */
    static public int getRandInt(int upperbound) {
        Random randInt = new Random();
        return randInt.nextInt(upperbound);
    }

    public void treasureAction(Adventurer adv) {
        //Sword action
        adv.treasures.replace(name, true);
        adv.mostRecentTreasure = name;
    }
}

class Sword extends Treasure {
    Sword() {
        name = "Sword";
    }
}

class Gem extends Treasure {
    Gem() {
        name = "Gem";
    }
}

class Armor extends Treasure {
    Armor() {
        name = "Armor";
    }
}

class Portal extends Treasure {

    Portal() {
        name = "Portal";
    }
    //Portal class
    public void treasureAction(Adventurer adv) {
        //Portal allows to move to any random room
        adv.treasures.replace(name, true);
        adv.mostRecentTreasure = name;
        // don't want portals to go to the start and end the game prematurely, so the first parameter is always between 1-4 inclusive
        Integer[] newRoom = {getRandInt(4) + 1, getRandInt(3), getRandInt(3)};

        if (Arrays.equals(adv.location, newRoom)) {
            treasureAction(adv); // if the previous location is equal to the new location newRoom, call the function again. (it wouldn't go anywhere otherwise)
        }
        else {
            adv.board.getRoomAt(adv.location).removeAdventurer(adv); // removes the adventurer from the room it's about to leave
            adv.location = newRoom; // sets location to the location at newRoom
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
        adv.treasures.replace(name, true);
        adv.mostRecentTreasure = name;
        if (adv.search.name == "Careful") {
            if(getRandInt(2) == 0) { // 50% chance of being damaged by the trap. equivalent to 50% chance of avoiding it
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
        adv.treasures.replace(name, true);
        adv.mostRecentTreasure = name;
        adv.health += 1;
    }
}