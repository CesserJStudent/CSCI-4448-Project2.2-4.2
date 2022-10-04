import java.util.Random;

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
        int adv_roll = ((getRandInt(6) + 1) + (getRandInt(6) + 1));
        int cre_roll = ((getRandInt(6) + 1) + (getRandInt(6) + 1));
        if (adv.armed == true) {
            adv_roll += 1;
        }

        if (adv.cursed == true) {
            cre_roll += 1;
        }

        if (adv.armor == true) {
            cre_roll -= 1;
        }
        adv_roll += fightBonus;
        return (adv_roll > cre_roll);
    }
}

class Stealth extends Combat {
    Stealth() {
        name = "Careful";
        fightBonus = 0;
    }
    @Override
    public Boolean fight(Adventurer adv, Creature cre) {
        if(getRandInt(2) == 0) {
            return null;
        }

        else {
            return super.fight(adv, cre);
        }

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
        name = "Trained";
        fightBonus = 2;
    }
}
