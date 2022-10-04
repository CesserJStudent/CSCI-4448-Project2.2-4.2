public abstract class Celebrate extends Combat {
    Combat combatBehavior;
    public abstract void celebrate();
}

class Shout extends Celebrate {
    public Shout(Combat combat) {
        this.combatBehavior = combat;
    }

    public void celebrate() {
        // needs to be implemented
    }

    public Boolean fight(Adventurer adv, Creature cre) { // fight implementation may need to be changed
        // beeds to be implemented
        return null;
    }
}

class Dance extends Celebrate {
    public Dance(Combat combat) {
        this.combatBehavior = combat;
    }

    public void celebrate() {
        // needs to be implemented
    }

    public Boolean fight(Adventurer adv, Creature cre) {
        // beeds to be implemented
        return null;
    }
}

class Jump extends Celebrate {
    public Jump(Combat combat) {
        this.combatBehavior = combat;
    }

    public void celebrate() {
        // needs to be implemented
    }

    public Boolean fight(Adventurer adv, Creature cre) {
        // beeds to be implemented
        return null;
    }
}

class Spin extends Celebrate {
    public Spin(Combat combat) {
        this.combatBehavior = combat;
    }

    public void celebrate() {
        // needs to be implemented
    }

    public Boolean fight(Adventurer adv, Creature cre) {
        // beeds to be implemented
        return null;
    }
}