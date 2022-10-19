import java.util.Random;

//Combat now uses a strategy pattern
public class Combat {
    String name;
    int fightBonus;

    /**
     * @param upperbound
     * @return A random integer between 0 and (upperbound - 1), inclusive
     */
    static public int getRandInt(int upperbound) {
        Random randInt = new Random();
        return randInt.nextInt(upperbound);
    }
    public Boolean fight(Adventurer adv, Creature cre) {
        int adv_roll = ((getRandInt(6) + 1) + (getRandInt(6) + 1)); // adv_roll
        int cre_roll = ((getRandInt(6) + 1) + (getRandInt(6) + 1)); // cre_roll
        if (adv.treasures.get("Sword") == true) {
            adv_roll += 1; //add one to adv_roll if armed
        }

        if (adv.treasures.get("Gem") == true) {
            cre_roll += 1; //add one to cre_roll if cursed
        }

        if (adv.treasures.get("Armor") == true) {
            cre_roll -= 1; //suvbtract one from cre_roll if armor
        }
        adv_roll += fightBonus;
        return (adv_roll > cre_roll); //return true if adv_roll is greater than cre_roll
    }
}

class Stealth extends Combat {
    Stealth() {
        name = "Stealth";
        fightBonus = 0;
    }
}

class Untrained extends Combat {
    Untrained() {
        name = "Untrained";
        fightBonus = 0;
    }
}

class Trained extends Combat {
    Trained() {
        name = "Trained";
        fightBonus = 1;
    }
}

class Expert extends Combat {
    Expert() {
        name = "Expert";
        fightBonus = 2;
    }
}


