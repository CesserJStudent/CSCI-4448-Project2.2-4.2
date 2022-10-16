public class CreatureFactory {
    public Creature newCreature(String creatureType, GameBoard board, Integer[] location) {
        if (creatureType == null) {
            return null;
        }
        if (creatureType.equalsIgnoreCase("Orbiter")) {
            return new Orbiter(board, location);
        }
        else if (creatureType.equalsIgnoreCase("Seeker")) {
            return new Seeker(board, location);
        }
        else if (creatureType.equalsIgnoreCase("Blinker")) {
            return new Blinker(board, location);
        }
        return null;
    }
}
