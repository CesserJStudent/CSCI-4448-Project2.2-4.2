//decorator class, this is using the decorator pattern
public abstract class Celebrate implements Decorate {
    protected Combat decoratedCombat; //combat to decorate

    public Celebrate(Combat decoratedCombat) {
        this.decoratedCombat = decoratedCombat;
    }

    public Boolean fight(Adventurer adv, Creature cre) {
        return decoratedCombat.fight(adv, cre);
    } //combat action
}

//concrete shout class
 class Shout extends Celebrate {
     public Shout(Combat decoratedCombat)
     {
         super(decoratedCombat);
     }
    public String action() {
        return("shout");
    } //return shout
}

//concrete dance class
class Dance extends Celebrate {
    public Dance(Combat decoratedCombat)
    {
        super(decoratedCombat);
    }
    public String action() {
        return("dance");
    } //return dance
}

//concrete Jump class
class Jump extends Celebrate {
    public Jump(Combat decoratedCombat)
    {
        super(decoratedCombat);
    }
    public String action() {
        return("jump");
    } //return jump
}

//concrete Spin class
class Spin extends Celebrate {
    public Spin(Combat decoratedCombat)
    {
        super(decoratedCombat);
    }

    public String action() {
        return("spin");
    } //return spin
}