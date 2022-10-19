import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.util.Scanner;

// OBSERVER: By implementing Subject, GameRunner now publishes and sends out events to all registered observers
public class GameRunner implements Subject{
    GameBoard playSpace = GameBoard.getBoard(); // gets the singleton GameBoard that was eagerly instantiated
    Scanner userInput = UserInput.getInput().getScanner(); // gets the singleton UserInput to initialize a Scanner object
    ArrayList<Adventurer> aliveAdventurers = new ArrayList<Adventurer>();   // list of alive adventurers
    ArrayList<Creature> aliveCreatures = new ArrayList<Creature>();         // list of alive creatures
    ArrayList<Observer> observers = new ArrayList<Observer>();              // list of current observers
    HashMap<String, String> event = new HashMap<String, String>();          // list containing strings needed to describe an event.
    int turn = 1; // the current turn

    /** Adds an observer to be notified of events 
     * @param obs The observer to be added
    */
    public void registerObserver(Observer obs) {
        observers.add(obs);
    }

    /** Removes an observer from the list of current observers
     * @param obs The observer to be removed
     */
    public void removeObserver(Observer obs) {
        observers.remove(obs);
    }

    /** Sends an event to all observers via their update() method */
    public void notifyObserver() {
        for (Observer i : observers) {
            i.update(event);
        }
    }
    // helper function for addEvent / Tracker
    private HashMap<String, String> getStatusUpdate(String eventType) {
        String currentTurn = String.valueOf(turn);
        String totalAdv = String.valueOf(aliveAdventurers.size());
        String totalCre = String.valueOf(aliveCreatures.size());

        HashMap<String, String> statusUpdate = new HashMap<String, String>();
        statusUpdate.put("eventType", eventType);
        statusUpdate.put("turn", currentTurn);
        statusUpdate.put("totalAdv", totalAdv);
        statusUpdate.put("totalCre", totalCre);

        // loop through all active adventurers, adding their information to status update
        for (int i = 0; i < aliveAdventurers.size(); i++) {
            Adventurer adv = aliveAdventurers.get(i);
            String advName = adv.name;
            String advLocation = Arrays.toString(adv.location);
            String advHealth = String.valueOf(adv.health);
            String advTreasure = "";
            for (String j : adv.treasures.keySet()) {
                if (adv.treasures.get(j)) { // if the adventurer owns the treasure, add it to advTreasure
                    advTreasure += j + ", ";
                }
            }
            if (advTreasure != "") {
                advTreasure = advTreasure.substring(0, advTreasure.length() - 2); // removes the last ", " from advTreasure if it was populated
            }
            statusUpdate.put("advName", advName); // passes the adventurer's name since names are now variable
            statusUpdate.put(advName + "Location", advLocation);
            statusUpdate.put(advName + "Health", advHealth);
            statusUpdate.put(advName + "Treasure", advTreasure);
        }

        // loop through all active creatures, adding their information to status update
        for (int k = 0; k < aliveCreatures.size(); k++) {
            Creature cre = aliveCreatures.get(k);
            String creName = cre.name;
            String creLocation = Arrays.toString(cre.location);
            // inserts a creature into statusUpdate with a key like "cre1Name" or "cre12Location". there can be a total of 12 creatures at a time
            statusUpdate.put("cre" + (k + 1) + "Name", creName);
            statusUpdate.put("cre" + (k + 1) + "Location", creLocation);
        }
        return statusUpdate;
    }

    /**
     * Populates "event" with all necessary information for each event. Calls notifyObserver() after populating it.
     * @param eventType A description of the event being added
     * @param adv The adventurer involved in the event, if any exists
     * @param cre The creature involved in the event, if any exists
     */
    protected void addEvent(String eventType, Adventurer adv, Creature cre) {
        if (!event.isEmpty()) event.clear();     // clears out the past event, if it exists
        event.put("eventType", eventType);  // adds the type of event with key "eventType"
        
        switch(eventType) {
            case "advMove":                                // adds the adventurer's name and location with keys "advName" and "location" respectively
                event.put("advName", adv.name);
                event.put("location", Arrays.toString(adv.location));
                break;
            case "advWin", "advLoss":                      // adds the adventurer's name and creature's name for both wins and losses. keys are "advName" and "creName"
                event.put("advName", adv.name);
                event.put("creName", cre.name);
                break;
            case "advHurt", "advHeal":                     // adds the adventurer's name and health for both events. keys are "advName" and "advHealth"
                event.put("advName", adv.name);
                event.put("advHealth", String.valueOf(adv.health));
                break;
            case "advDeath", "advCelebrate", "foundTrap":  // these 3 events only require the adventurer's name
                event.put("advName", adv.name);
                break;
            case "foundTreasure":           // adds the adventurer's name and found treasure's name. keys are "advName" and "treasureName"
                event.put("advName", adv.name);
                event.put("treasureName", adv.mostRecentTreasure);
                break;
            case "creMove":                 // creature events are handled the same as adventurer events. equivalent keys are prefixed with "cre" instead of "adv"
                event.put("creName", cre.name);
                event.put("location", Arrays.toString(cre.location));
                break;
            case "creWin", "creLoss":
                event.put("creName", cre.name);
                event.put("advName", adv.name);
                break;
            case "creDeath":
                event.put("creName", cre.name);
                break;
            case "turnEnd":  // used to signal the end of turn. stop condition for logger and contains all the data needed for tracker
                event = getStatusUpdate(eventType);
                break;
            case "gameEnd":  // used to signal the end of the game. stop condition for tracker
                break;
            default:
                event.clear();
                System.out.println("invalid event added");
                return;
        }
        if (!event.isEmpty()) notifyObserver();   // passes this event to all observers, unless an invalid event was added
    }

    /**
     * @param upperbound
     * @return A random integer between 0 and (upperbound - 1), inclusive
     */
    static public int getRandInt(int upperbound) {
        Random randInt = new Random();
        return randInt.nextInt(upperbound);
    }

    static public Integer[] getRandRoom() {
        Integer[] room = {getRandInt(4) + 1, getRandInt(3), getRandInt(3)};
        return room;
    }
    public void placePlayers(){
        //System.out.println("Hello! Welcome to RotLA. This key will help you get to know the characters. B = Brawler, S = Sneaker, R = Runner, T = Thief, OB = Orbiter, SK = Seeker, and BK = Blinker. The game will now begin.");
        Integer[] startI = new Integer[]{0, 1, 1};
        Room startR = playSpace.getRoomAt(startI);
        //Ask to choose adventurer
        System.out.println("Choose an Adventurer: (Brawler, Sneaker, Runner, or Thief)");
        String choosePlayer = userInput.nextLine();
        System.out.println("Name your Adventurer!");
        String chooseName = userInput.nextLine();
        AdventurerFactory advFactory = new AdventurerFactory();
        startR.addAdventurer(advFactory.newAdv(choosePlayer, chooseName));
        //add Creatures to the board
        CreatureFactory creFactory = new CreatureFactory();
        for (int i = 0; i < 4; i++) {
            int x = getRandInt(4) + 1; //get x for orbiter placement
            int y = getRandInt(3); //get y for orbiter placement
            int z = -1;
            if (y == 1) { //if y is 1, z must be 0 or 2 because orbiters cannot be placed in the center room
                int temp = getRandInt(2);
                if (temp == 0) {
                    z = 0;
                }
                else {
                    z = 2;
                }
            }
            else { //otherwise can be any room
                z = getRandInt(3);
            }
            Integer[] curLoc = new Integer[]{x, y, z};
            // IDENTITY: Even though multiple Orbiter objects are added with the same name "ob", each one has its own unique identity to distinguish itself from others.
            Creature ob = creFactory.newCreature("Orbiter", curLoc);
            playSpace.getRoomAt(curLoc).addCreature(ob); // add orbiter to board
            aliveCreatures.add(ob); //add orbiter to aliveCreatures

            curLoc = getRandRoom(); //get random room for seeker placement
            Creature sk = creFactory.newCreature("Seeker", curLoc);
            playSpace.getRoomAt(curLoc).addCreature(sk); // add Seeker to board
            aliveCreatures.add(sk); //add Seeker to aliveCreatures

            y = getRandInt(3); //set y for blinker placement
            z = getRandInt(3); //set z for blinker placement
            curLoc = new Integer[]{4, y, z};
            Creature bk = creFactory.newCreature("Blinker", curLoc);
            playSpace.getRoomAt(curLoc).addCreature(bk); // add Blinker to board
            aliveCreatures.add(bk); //add Blinker to aliveCreatures

        }

        aliveAdventurers.add(startR.adventurersPresent.get(0));

    }

    public Integer[] randAndCheckTreasure() {
        Integer[] room = getRandRoom();
        Room curRoom = playSpace.getRoomAt(room);
        if (curRoom.treasure == null) {
            return room;
        }
        else {
            return randAndCheckTreasure();
        }
    }

    public void placeTreasure() {
        Integer[] curLoc;

        for (int i = 0; i < 4; i++) {
            curLoc = randAndCheckTreasure(); //get random room for Sword placement
            Treasure curTres = playSpace.getRoomAt(curLoc).treasure = new Sword();
            playSpace.unFoundTreasures.add(curTres);

            curLoc = randAndCheckTreasure(); //get random room for Gem placement
            curTres = playSpace.getRoomAt(curLoc).treasure = new Gem();
            playSpace.unFoundTreasures.add(curTres);

            curLoc = randAndCheckTreasure(); //get random room for Armor placement
            curTres = playSpace.getRoomAt(curLoc).treasure = new Armor();
            playSpace.unFoundTreasures.add(curTres);

            curLoc = randAndCheckTreasure(); //get random room for Trap placement
            curTres =playSpace.getRoomAt(curLoc).treasure = new Trap();
            playSpace.unFoundTreasures.add(curTres);

            curLoc = randAndCheckTreasure(); //get random room for Potion placement
            curTres = playSpace.getRoomAt(curLoc).treasure = new Potion();
            playSpace.unFoundTreasures.add(curTres);

            curLoc = randAndCheckTreasure(); //get random room for Portal placement
            curTres = playSpace.getRoomAt(curLoc).treasure = new Portal();
            playSpace.unFoundTreasures.add(curTres);
        }
    }

    protected void checkForAdvDeath(Adventurer adv, Room room) {
        if (adv.health <= 0) { //if adventurer dies, remove from aliveAdventurers and room
            room.removeAdventurer(adv);
            aliveAdventurers.remove(adv);
            addEvent("advDeath", adv, null);
        }
    }

    public String decorateAdv(Adventurer curAdv) { //class to decorate Adventurer after combat
        Shout shout = new Shout(curAdv.combat);
        Jump jump = new Jump(curAdv.combat);
        Spin spin = new Spin(curAdv.combat);
        Dance dance = new Dance(curAdv.combat);
        String fullCel = "";
        for (int i = 0; i < 2; i++) { //add 2 random of each action
            if(getRandInt(3) == 1) {
                fullCel += shout.action();
                fullCel += " ";
            }
            if(getRandInt(3) == 1) {
                fullCel += jump.action();
                fullCel += " ";
            }
            if(getRandInt(3) == 1) {
                fullCel += spin.action();
                fullCel += " ";
            }
            if(getRandInt(3) == 1) {
                fullCel += dance.action();
                if(i == 0) {
                    fullCel += " ";
                }
            }
        }
        if(fullCel.equals("")) {
            fullCel = "No Celebration";
        }

        return fullCel;
    }

    /**
     * @param curAdv The adventurer who is fighting
     * @param curRoom The creature who is fighting
     * @return A Boolean value representing whether the dies
     */
    public void fightCreature(Adventurer curAdv, Room curRoom) {
        for(int z =0; z < curRoom.creaturesPresent.size(); z++) { //get all creatures in the room
            Creature curCre = curRoom.creaturesPresent.get(z);
            Boolean winner = curAdv.combat.fight(curAdv, curCre);
            if (winner != null && winner == true) { //if adventurer wins
                //if adventurer wins eliminate creature
                curRoom.removeCreature(curCre);
                aliveCreatures.remove(curCre);
                addEvent("advWin", curAdv, curCre);
                addEvent("creDeath", null, curCre);
            }
            else if (winner != null) { //if creature wins
                addEvent("advLoss", curAdv, curCre);
                if (curAdv.combat.name == "Stealth") {
                    if (getRandInt(2) == 0) { // 50% chance to take damage. equivalent to 50% chance to avoid damage
                        curAdv.health -=1;
                        addEvent("advHurt", curAdv, null);
                    }
                }
                else {
                    curAdv.health -= 1; //if adventurer loses, reduce health by 1
                    addEvent("advHurt", curAdv, null);
                }
            }
        }
        checkForAdvDeath(curAdv, curRoom);
    }

    /**
     * @param curRoom The room that the adventurer is in
     * @param curCre The creatures who is fighting
     * @return A Boolean value representing whether the adventurer dies
     */
    public void fightAdventurer(Creature curCre, Room curRoom) {
       for(int z =0; z < curRoom.adventurersPresent.size(); z++) { //get all advernuters in the room
            Adventurer curAdv = curRoom.adventurersPresent.get(z);
            Boolean winner = curAdv.combat.fight(curAdv, curCre);
            if (winner != null && winner == true) { //if adventurer wins eliminate creature
                curRoom.removeCreature(curCre);
                aliveCreatures.remove(curCre);
                addEvent("creLoss", curAdv, curCre);
                addEvent("creDeath", null, curCre);
            }
            else if(winner != null) { //if creature wins
                addEvent("creWin", curAdv, curCre);
                if (curAdv.combat.name == "Stealth") {
                    if (getRandInt(2) == 0) { // 50% chance to take damage. equivalent to 50% chance to avoid damage
                        curAdv.health -=1;
                        addEvent("advHurt", curAdv, null);
                    }
                }
                else {
                    curAdv.health -= 1; // adventurer lost, reduce health by 1
                    addEvent("advHurt", curAdv, null);
                }
            }
            checkForAdvDeath(curAdv, curRoom);
        }
    }


    public void creatureAction() { //creature action
        for (int i = 0; i < aliveCreatures.size(); i++) {
            Creature curCre = aliveCreatures.get(i); //get current creature
            Room curRoom = curCre.board.getRoomAt(curCre.location); //get location
            if (!curRoom.hasAdventurer()) { //if needed move
                curCre.move(); // POLYMORPHISM: move() is called with the same name, but performs differently depending on which creature it's applied to
                Room newRoom = curCre.board.getRoomAt(curCre.location);         // stores location of the room moved to. needed to check for Seeker movement since move() won't always move them if their conditions aren't met
                if (!newRoom.equals(curRoom)) {
                    addEvent("creMove", null, curCre); // updates event if "newRoom" and "curRoom" are different rooms. they may be the same if "curCre" is a Seeker.
                }
                if (newRoom.hasAdventurer()) {                                  // checks "newRoom" for adventurers and fights them if found
                    fightAdventurer(curCre, newRoom);
                }
            }
            else {  // else fight
                fightAdventurer(curCre, curRoom); //fight adventurer
            }
        }
    }

    public void searchAndReturn(Adventurer curAdv, Room curRoom) {
        int preSearchHP = curAdv.health;    // needed to check if an adventurer avoided triggering a trap or died from it. set to adventurer health before attempting a search
        HashMap<String, Boolean> searchResult = curAdv.search.loot(curAdv, curRoom); // performs a search and returns the result
        if (searchResult.get("Treasure") != null) {             // enters if the adventurer found a treasure
            addEvent("foundTreasure", curAdv, null);
            if (curAdv.mostRecentTreasure == "Potion") {
                addEvent("advHeal", curAdv, null);
            }
        }
        if (searchResult.get("Trap") != null) {                 // enters if the adventurer found a trap
            addEvent("foundTrap", curAdv, null);
            int postSearchHP = curAdv.health;
            if (preSearchHP > postSearchHP) { // the adventurer didn't avoid the trap and took damage
                addEvent("advHurt", curAdv, null);
                checkForAdvDeath(curAdv, curRoom);
            }
        }
    }

    public void adventurerTurn(Adventurer curAdv, Room curRoom) {
        playSpace.printBoard(); //print board
        printStats(); //print stats
        CommandExecutor executeC = new CommandExecutor();
        System.out.println("Your turn, " + curAdv.name + "!");
        HashMap <String, Boolean> validActions = checkAdvActions(curRoom); //check adventurer actions
        String invalidAction = "That action is currently unavailable! Try again. \n";
        System.out.println("Please enter an available action.");
        String chooseCommand = userInput.nextLine();
        switch (chooseCommand.toLowerCase()) {
            case "move", "<move>":
                executeC.executeCommand(new MoveCommand(curAdv, curRoom, this));
                break;
            case "search", "<search>":
                if (validActions.get("search")) {
                    executeC.executeCommand(new SearchCommand(curAdv, curRoom, this));
                }
                else {
                    System.out.println(invalidAction);
                    adventurerTurn(curAdv, curRoom);
                }
                break;
            case "fight", "<fight>":
                if (validActions.get("fight")) {
                    executeC.executeCommand(new FightCommand(curAdv, curRoom, this));
                }
                else {
                    System.out.println(invalidAction);
                    adventurerTurn(curAdv, curRoom);
                }
                break;
            case "celebrate", "<celebrate>":
                if (validActions.get("celebrate")) {
                    executeC.executeCommand(new CelebrateCommand(curAdv, curRoom, this));
                }
                else {
                    System.out.println(invalidAction);
                    adventurerTurn(curAdv, curRoom);
                }
                break;
            case "quit", "<quit>", "q":
                System.out.println("Thanks for playing!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid command");
                break;
        }


    }
    private HashMap<String, Boolean> checkAdvActions(Room curRoom) {
        HashMap<String, Boolean> availableActions = new HashMap<String, Boolean>() {{
            put("move", true);
            put("quit", true);
            put("fight", false);
            put("search", false);
            put("celebrate", false);
        }};
        System.out.println("<move> and <quit> are always available.");
        if(curRoom.hasCreature()) {
            availableActions.replace("fight", true);
            System.out.println("This room has a creature... <fight> is available.");
        }
        else {
            if (curRoom.treasure != null) {
                availableActions.replace("search", true);
                System.out.println("This room contains a treasure! <search> is available.");
            }
            availableActions.replace("celebrate", true);
            System.out.println("This room has no creatures. <celebrate> is available.");
        }
        return availableActions;

    }
    public void adventurerAction() {
        for (int i = 0; i < aliveAdventurers.size(); i++) { //check for alive adventurers
            Adventurer curAdv = aliveAdventurers.get(i);
            Room curRoom = curAdv.board.getRoomAt(curAdv.location);
            adventurerTurn(curAdv, curRoom);
        }
    }


    public void actionRunner() {
        adventurerAction(); //run adventurer actions
        creatureAction(); //run creature actions
    }

    /**
     * @return A Boolean value representing whether the game is over
     */
    public Boolean gameOver() {
        Integer[] startI = new Integer[]{0, 1, 1};
        Room startR = playSpace.getRoomAt(startI);
        if (aliveAdventurers.size() == 0) { //game over if all adventurers die
            System.out.printf("Defeat... You died! \n");
            return true;
        }
        else if (startR.hasAdventurer()) { //game over if adventurer returns to the starting room
            Boolean treasureCheck = true; // checks if the adventurer gathered all the treasures
            for (Boolean i : aliveAdventurers.get(0).treasures.values()) {
                if (i == false) {   // any treasure that hasn't been found will have a false value
                    treasureCheck = false;
                }
            }
            if (treasureCheck) {    // if all the adventurer owns all the treasures
                System.out.println("Victory! Returned home with all treasures!");
            }
            else {                  // else a treasure was missing
                System.out.println("Defeat... Returned to start without all treasures.");
            }
            return true;
        }
        else if (aliveCreatures.size() == 0) { //game over if creatures are eliminated
            System.out.println("Victory! All Creatures eliminated!");
            return true;
        }
        else {
            return false;
        }
    }

    public void printStats() {
        //print stats for Adventurers
        for(int i = 0; i < aliveAdventurers.size(); i++) {
            Adventurer curAdv = aliveAdventurers.get(i);
            System.out.println("\n" + curAdv.name + " has " + curAdv.health + " health.");
            String gatheredTreasures = "";
            for (String j : curAdv.treasures.keySet()) {
                if (curAdv.treasures.get(j)) { // if the adventurer owns the treasure, add it to gatheredTreasures
                    gatheredTreasures += j + ", ";
                }
            }
            if (gatheredTreasures != "") {
                gatheredTreasures = gatheredTreasures.substring(0, gatheredTreasures.length() - 2); // removes the last ", " from advTreasure if it was populated
                System.out.println(curAdv.name + " has collected the following treasures: " + gatheredTreasures);
            }
        }

        int obC = 0;
        int skC = 0;
        int bkC = 0;
        //update creature count
        for (int i = 0; i < aliveCreatures.size(); i++) {
            Creature curCre = aliveCreatures.get(i);
            if(curCre instanceof Orbiter) {
                obC++;
            }
            else if(curCre instanceof Seeker) {
                skC++;
            }
            else if(curCre instanceof Blinker) {
                bkC++;
            }
        }
        //print stats for creatures and treasure
        System.out.println("\n" + "OB: " + obC + " Remaining");
        System.out.println("SK: " + skC + " Remaining");
        System.out.println("BK: " + bkC + " Remaining");
        System.out.println("Remaining Treasure: " + playSpace.unFoundTreasures.size() + "\n");
    }

    // function to delete subdirectories and files. taken from https://www.geeksforgeeks.org/java-program-to-delete-a-directory/
    private void deleteDirectory(File file) {
        // store all the paths of files and folders present
        // inside directory
        for (File subfile : file.listFiles()) {
            // if it is a subfolder,e.g Rohan and Ritik,
            // recursively call function to empty subfolder
            if (subfile.isDirectory()) {
                deleteDirectory(subfile);
            }
            // delete files and empty subfolders
            subfile.delete();
        }
    }
    public void runGame() {
        // empties the "text output" folder each time runGame() is called, if it exists.
        File directory = new File("Logger-Tracker output");
        if (!directory.mkdirs()) {
            deleteDirectory(directory);
        }
        Tracker tracker = Tracker.getTracker();
        registerObserver(tracker);

        placePlayers(); //place players
        placeTreasure(); //place treasure
        Boolean gO = false; //game over set to false
        // System.out.println("Hello! Welcome to RotLA. This key will help you get to know the characters. B = Brawler, S = Sneaker, R = Runner, T = Thief, OB = Orbiter, SK = Seeker, and BK = Blinker. The game will now begin.");
        // playSpace.printBoard();
        while (!gO) {
            Logger log = Logger.getInstance(turn);
            registerObserver(log);

            actionRunner(); //run actions
            //playSpace.printBoard(); //print board
            //printStats(); //print stats
            gO = gameOver(); //check if game is over

            addEvent("turnEnd", null, null);
            turn++;
            removeObserver(log);
        }
        addEvent("gameEnd", null, null);
        removeObserver(tracker);
        userInput.close();
    }


    public static void main(String[] args) {
        /* for(int i = 0; i  < 30; i++) {
            System.out.printf("Game " + i + ": ");
            GameRunner game = new GameRunner();
            game.runGame();
        } */
        GameRunner game = new GameRunner();
        game.runGame();
    }
}
