public class AdventurerFactory {
    public Adventurer newAdv(String advType, String name) { //AdventurerFactory
        if (advType == null) { // if the type is null, return null
            return null;
        }
        if (advType.equalsIgnoreCase("Brawler")) { // if brawler create new Brawler
            return new Brawler(name);
        }
        else if (advType.equalsIgnoreCase("Sneaker")) { // if sneaker create new Sneaker
            return new Sneaker(name);
        }
        else if (advType.equalsIgnoreCase("Runner")) { // if runner create new Runner
            return new Runner(name);
        }
        else if (advType.equalsIgnoreCase("Thief")) { // if thief create new Thief
            return new Thief(name);
        }
        return null;
    }
}
