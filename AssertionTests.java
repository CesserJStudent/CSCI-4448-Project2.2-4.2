import org.junit.jupiter.api.DisplayName;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;

@DisplayName("18 assertions across 7 tests for Project 4.2")
public class AssertionTests {

    @Test
    @DisplayName("Two GameBoard objects should be the same instance given by getBoard()")
    public void singletonBoardTest() { // contains 1 assert statement
        GameBoard board1 = GameBoard.getBoard();
        GameBoard board2 = GameBoard.getBoard();
        assertEquals(board1, board2);
    }

    @Test
    @DisplayName("Two Tracker objects should be the same instance given by getTracker()")
    public void singletonTrackerTest() { // contains 1 assert statement
        File directory = new File("Logger-Tracker output");
        directory.mkdirs();
        Tracker tracker1 = Tracker.getTracker();
        Tracker tracker2 = Tracker.getTracker();
        assertEquals(tracker1, tracker2);
    }

    @Test
    @DisplayName("Two Logger objects should be the same instance given by getInstance(), even if the turn parameter is different")
    public void singletonLoggerTest() { // contains 1 assert statement
        File directory = new File("Logger-Tracker output");
        directory.mkdirs();
        Logger logger1 = Logger.getLogger(1);
        Logger logger2 = Logger.getLogger(2);
        assertEquals(logger1, logger2);
    }

    @Test
    @DisplayName("Center room of 1-1-1 should have the correct adjacent rooms")
    public void adjacentRoomsTest() { // contains 6 assert statements
        Integer[] center = {1, 1, 1};
        HashMap<String, Integer[]> actualAdjRooms = new HashMap<String, Integer[]>() {{
            put("up", new Integer[] {1, 0, 1});
            put("down", new Integer[] {1, 2, 1});
            put("right", new Integer[] {1, 1, 2});
            put("left", new Integer[] {1, 1, 0});
            put("above", new Integer[] {2, 1, 1});
            put("below", new Integer[] {0, 1, 1});
        }};
        HashMap<String, Integer[]> adjRooms = GameBoard.getBoard().getAdjacentRooms(center);
        for (String i : adjRooms.keySet()) {
            assertArrayEquals(actualAdjRooms.get(i), adjRooms.get(i));
        }
    }

    @Test
    @DisplayName("Adventurers created with a factory should be equivalent to Adventurers created without one")
    public void adventurerFactoryTest() { // contains 3 assert statements
        Adventurer brawler = new Brawler("coolguy");
        AdventurerFactory advFactory = new AdventurerFactory();
        Adventurer factoryBrawler = advFactory.newAdv("Brawler", "coolguy");
        
        assertEquals(brawler.name, factoryBrawler.name);
        assertEquals(brawler.search.name, factoryBrawler.search.name);
        assertEquals(brawler.combat.name, factoryBrawler.combat.name);
    }

    @Test
    @DisplayName("Creatures created with a factory should be equivalent to Creatures created without one")
    public void creatureFactoryTest() { // contains 2 assert statements
        Integer[] startRoom = {1, 1, 2};
        Creature orbiter = new Orbiter(startRoom);
        CreatureFactory creFactory = new CreatureFactory();
        Creature factoryOrbiter = creFactory.newCreature("Orbiter", new Integer[] {1, 1, 2});
        
        assertEquals(orbiter.name, factoryOrbiter.name);
        assertArrayEquals(orbiter.location, factoryOrbiter.location);
    }

    @Test
    @DisplayName("A Subject should be able to correctly register and remove Observers")
    public void subjectMethodsTest() { // contains 4 assert statements
        GameRunner subject = new GameRunner();

        File directory = new File("Logger-Tracker output");
        directory.mkdirs();
        Tracker observerOne = Tracker.getTracker();
        Logger observerTwo = Logger.getLogger(1);

        subject.registerObserver(observerOne);
        assertTrue(subject.observers.contains(observerOne));

        subject.registerObserver(observerTwo);
        assertTrue(subject.observers.contains(observerTwo));

        subject.removeObserver(observerTwo);
        assertFalse(subject.observers.contains(observerTwo));

        subject.removeObserver(observerOne);
        assertTrue(subject.observers.isEmpty());
    }
}