public class AdventurerFactory {
    public Adventurer newAdv(String advType, GameBoard board, String name) {
        if (advType == null) {
            return null;
        }
        if (advType.equalsIgnoreCase("Brawler")) {
            return new Brawler(board, name);
        }
        else if (advType.equalsIgnoreCase("Sneaker")) {
            return new Sneaker(board, name);
        }
        else if (advType.equalsIgnoreCase("Runner")) {
            return new Runner(board, name);
        }
        else if (advType.equalsIgnoreCase("Thief")) {
            return new Thief(board, name);
        }
        return null;
    }
}
