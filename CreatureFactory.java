public class CreatureFactory {
    public Creature newCreature(String creatureType, Integer[] location) { //CreatureFactory
        if (creatureType == null) { // if null, return null
            return null;
        }
        if (creatureType.equalsIgnoreCase("Orbiter")) { //if orbiter, return Orbiter
            return new Orbiter(location);
        }
        else if (creatureType.equalsIgnoreCase("Seeker")) { //if seeker, return Seeker
            return new Seeker(location);
        }
        else if (creatureType.equalsIgnoreCase("Blinker")) { //if blinker, return Blinker
            return new Blinker(location);
        }
        return null; //if none of the above, return null
    }
}
