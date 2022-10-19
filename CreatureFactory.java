public class CreatureFactory {
    public Creature newCreature(String creatureType, Integer[] location) {
        if (creatureType == null) {
            return null;
        }
        if (creatureType.equalsIgnoreCase("Orbiter")) {
            return new Orbiter(location);
        }
        else if (creatureType.equalsIgnoreCase("Seeker")) {
            return new Seeker(location);
        }
        else if (creatureType.equalsIgnoreCase("Blinker")) {
            return new Blinker(location);
        }
        return null;
    }
}
