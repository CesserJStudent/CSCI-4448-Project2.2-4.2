public class Combat {
    String name;
    int fightBonus;

    public Boolean fight(Adventurer adv, Creature cre) {
        int adv_roll = ((adv.getRandInt(6) + 1) + (adv.getRandInt(6) + 1));
        int cre_roll = ((cre.getRandInt(6) + 1) + (cre.getRandInt(6) + 1));
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
        if(adv.getRandInt(2) == 0) {
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
