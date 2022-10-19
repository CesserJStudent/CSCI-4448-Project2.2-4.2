public class AdventurerFactory {
    public Adventurer newAdv(String advType, String name) {
        if (advType == null) {
            return null;
        }
        if (advType.equalsIgnoreCase("Brawler")) {
            return new Brawler(name);
        }
        else if (advType.equalsIgnoreCase("Sneaker")) {
            return new Sneaker(name);
        }
        else if (advType.equalsIgnoreCase("Runner")) {
            return new Runner(name);
        }
        else if (advType.equalsIgnoreCase("Thief")) {
            return new Thief(name);
        }
        return null;
    }
}
