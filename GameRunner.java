import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

interface Subject {
    public void registerObserver(Observer obs);
    public void removeObserver(Observer obs);
    public void notifyObserver();
}

public class GameRunner implements Subject{
    GameBoard playSpace = new GameBoard(); //create game board
    ArrayList<Adventurer> aliveAdventurers = new ArrayList<Adventurer>();   // list of alive adventurers
    ArrayList<Creature> aliveCreatures = new ArrayList<Creature>();         // list of alive creatures
    ArrayList<Observer> observers = new ArrayList<Observer>();              // list of current observers
    ArrayList<String> event = new ArrayList<String>();                      // list containing strings needed to describe an event
    int turn = 1; // the current turn

    public void registerObserver(Observer obs) {
        observers.add(obs);
    }

    public void removeObserver(Observer obs) {
        observers.remove(obs);
    }

    public void notifyObserver() {
        for (Observer i : observers) {
            i.update(event);
        }
    }

    private void addEvent(String eventType, Adventurer adv, Creature cre) {
        if (!event.isEmpty()) event.clear();          // clears out the past event, if it exists
        event.add(eventType);                         // adds the event type to "event"
        
        switch(eventType) {
            case "adventurer movement":               // adds the adventurer's name and location
                event.add(adv.name);
                event.add(Arrays.toString(adv.location));
                break;
            case "adventurer combat win":             // adds the adventurer's name and creature's name for both wins and losses
                event.add(adv.name);
                event.add(cre.name);
                addEvent("creature defeated", null, cre); // since this event always follows, recursively call it here
                break;
            case "adventurer combat loss":
                event.add(adv.name);
                event.add(cre.name);
                addEvent("adventurer damaged", adv, null); // since this event always follows, recursively call it here
                break;
            case "adventurer damaged":                // adds the damaged adventurer's name and health
                event.add(adv.name);
                event.add(String.valueOf(adv.health));
                break;
            case "adventurer defeated":               // adds the defeated adventurer's name to "event"
                event.add(adv.name);
                break;
            case "celebration":
                event.add(adv.name);
                event.add("adventurer celebration message"); // not sure how to add this yet  
                break;
            case "found treasure":
                event.add(adv.name);
                event.add(adv.ownedTreasures.get(adv.ownedTreasures.size() - 1).name); // this should work for everything but traps
                break;
            case "creature movement":                 // creature events are handled the same as adventurer events, except the creature's name is added first
                event.add(cre.name);
                event.add(Arrays.toString(cre.location));
                break;
            case "creature combat win":
                event.add(cre.name);
                event.add(adv.name);
                addEvent("adventurer damaged", adv, null); // since this event always follows, recursively call it here
                break;
            case "creature combat loss":
                event.add(cre.name);
                event.add(adv.name);
                addEvent("creature defeated", null, cre);  // since this event always follows, recursively call it here
                break;
            case "creature defeated":
                event.add(cre.name);
                break;
            default:
                event.clear();
                System.out.println("invalid event added");
                return;
        }
        notifyObserver();   // passes this event to all observers
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
        //add Adventurers to start room
        startR.addAdventurer(new Brawler(playSpace,startI));
        startR.addAdventurer(new Sneaker(playSpace,startI));
        startR.addAdventurer(new Runner(playSpace,startI));
        startR.addAdventurer(new Thief(playSpace,startI));
        //add Creatures to the board
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
            Orbiter ob = new Orbiter(playSpace, curLoc);
            playSpace.getRoomAt(curLoc).addCreature(ob); // add orbiter to board
            aliveCreatures.add(ob); //add orbiter to aliveCreatures

            curLoc = getRandRoom(); //get random room for seeker placement
            Seeker sk = new Seeker(playSpace, curLoc);
            playSpace.getRoomAt(curLoc).addCreature(sk); // add Seeker to board
            aliveCreatures.add(sk); //add Seeker to aliveCreatures

            y = getRandInt(3); //set y for blinker placement
            z = getRandInt(3); //set z for blinker placement
            curLoc = new Integer[]{4, y, z};
            Blinker bk = new Blinker(playSpace, curLoc);
            playSpace.getRoomAt(curLoc).addCreature(bk); // add Blinker to board
            aliveCreatures.add(bk); //add Blinker to aliveCreatures

        }


        for (int i = 0; i < 4; i++) { //add adventurers to aliveAdventurers
            aliveAdventurers.add(startR.adventurersPresent.get(i));
        }

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

    public Boolean checkAndRunPortal(Adventurer adv) {
        for(int i = 0; i < adv.ownedTreasures.size(); i++) {
            if (adv.ownedTreasures.get(i) instanceof Portal) {
                adv.ownedTreasures.get(i).treasureAction(adv);
                return true;
            }
        }
        return false;
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
                addEvent("adventurer combat win", curAdv, curCre);
            }
            else if (winner != null) { //if creature wins
                curAdv.health -= 1; //if adventurer loses, reduce health by 1
                addEvent("adventurer combat loss", curAdv, curCre);
            }
        }
        if (curAdv.health <= 0) { //if adventurer dies, remove from aliveAdventurers and room
            curRoom.removeAdventurer(curAdv);
            aliveAdventurers.remove(curAdv);
            addEvent("adventurer defeated", curAdv, null);
        }
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
                addEvent("creature combat loss", curAdv, curCre);
            }
            else if(winner != null) { //if creature wins
                curAdv.health -= 1; //if adventurer loses, reduce health by 1
                addEvent("creature combat win", curAdv, curCre);
            }

            if (curAdv.health <= 0) { //if adventurer dies, remove from aliveAdventurers and room
                curRoom.removeAdventurer(curAdv);
                aliveAdventurers.remove(curAdv);
                addEvent("adventurer defeated", curAdv, null);
            }
        }
    }


    public void creatureAction() { //creature action
        for (int i = 0; i < aliveCreatures.size(); i++) {
            Creature curCre = aliveCreatures.get(i); //get current creature
            Room curRoom = curCre.board.getRoomAt(curCre.location); //get location
            if (!curRoom.hasAdventurer()) { //if needed move
                curCre.move(); // POLYMORPHISM: move() is called with the same name, but performs differently depending on which creature it's applied to
                Room newRoom = curCre.board.getRoomAt(curCre.location);         // stores location of the room moved to. needed to check for Seeker movement since move() won't always move them if their conditions aren't met
                if (newRoom.hasAdventurer()) {                                  // checks "newRoom" for adventurers and fights them if found
                    fightAdventurer(curCre, newRoom);
                }
                if (!newRoom.equals(curRoom)) {
                    addEvent("creature movement", null, curCre); // updates event if "newRoom" and "curRoom" are different rooms. they may be the same if "curCre" is a Seeker.
                }
            }
            else {  // else fight
                fightAdventurer(curCre, curRoom); //fight adventurer
            }
            // old code
            // if(curCre.name == "BK" || curCre.name == "OB" && !curRoom.hasAdventurer()) { //if needed move
            //     curCre.move();      // POLYMORPHISM: move() is called with the same name, but performs differently depending on which creature it's applied to
            //     addEvent("creature movement", null, curCre);
            // }
            // else if(curCre.name == "SK") {
            //     curCre.move();
            // }
            // curRoom = curCre.board.getRoomAt(curCre.location); //get new location
            // if(curRoom.hasAdventurer()) {
            //     fightAdventurer(curCre, curRoom); //fight adventurer
            // }
        }
    }
    public void adventurerTurn(Adventurer curAdv, Room curRoom) {
        if (getRandInt(3) == 1) { // 1/3% chance to use portal if owned
            if (!checkAndRunPortal(curAdv)) {
                curAdv.move(); //move adventurer
                addEvent("adventurer movement", curAdv, null);
            }
        }
        else {
            curAdv.move(); //move adventurer
            addEvent("adventurer movement", curAdv, null);
        }
        curRoom = curAdv.board.getRoomAt(curAdv.location);
        if (curRoom.hasCreature()) {
            fightCreature(curAdv, curRoom);
        }
        else {
            if(curAdv.search.loot(curAdv, curRoom)) {
                addEvent("found treasure", curAdv, null);
            }
        }
    }
    public void adventurerAction() {
        for (int i = 0; i < aliveAdventurers.size(); i++) { //check for alive adventurers
            Adventurer curAdv = aliveAdventurers.get(i);
            Room curRoom = curAdv.board.getRoomAt(curAdv.location);
            if (curAdv.name == "R") { //if runner move twice
                adventurerTurn(curAdv, curRoom);
                //Second move
                adventurerTurn(curAdv, curRoom);
            }
            else { //otherwise single turn
                adventurerTurn(curAdv, curRoom);
            }
        }
    }


    public void actionRunner() {
        Logger log = new Logger();
        registerObserver(log);

        adventurerAction(); //run adventurer actions
        creatureAction(); //run creature actions

        removeObserver(log);
    }

    /**
     * @return A Boolean value representing whether the game is over
     */
    public Boolean gameOver() {
        if (aliveAdventurers.size() == 0) { //game over if all adventurers die
            System.out.printf("All Adventurers eliminated \n");
            return true;
        }
        else if (playSpace.unFoundTreasures.size() == 0) { //game over if treasure is collected
            System.out.printf("All treasure found \n");
            return true;
        }
        else if (aliveCreatures.size() == 0) { //game over if creatures are eliminated
            System.out.printf("All Creatures eliminated \n");
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
            System.out.println(curAdv.name + " has " + curAdv.health + " health and " + curAdv.ownedTreasures + " treasure.");
        }

        int obC = 0;
        int skC = 0;
        int bkC = 0;
        //update creature count
        for (int i = 0; i < aliveCreatures.size(); i++) {
            Creature curCre = aliveCreatures.get(i);
            if(curCre.name == "OB") {
                obC++;
            }
            else if(curCre.name == "SK") {
                skC++;
            }
            else if(curCre.name == "BK") {
                bkC++;
            }
        }
        //print stats for creatures and treasure
        System.out.println("OB: " + obC + " Remaining");
        System.out.println("SK: " + skC + " Remaining");
        System.out.println("BK: " + bkC + " Remaining");
        System.out.println("Total Treasure: " + playSpace.unFoundTreasures.size());
    }
    public void runGame() {
        Tracker tracker = new Tracker();
        registerObserver(tracker);

        playSpace.init_game(5, 3, 3); //init game
        placePlayers(); //place players
        placeTreasure(); //place treasure
        Boolean gO = false; //game over set to false
        System.out.println("Hello! Welcome to RotLA. This key will help you get to know the characters. B = Brawler, S = Sneaker, R = Runner, T = Thief, OB = Orbiter, SK = Seeker, and BK = Blinker. The game will now begin.");
        playSpace.printBoard();
        while (!gO) {
            actionRunner(); //run actions
            playSpace.printBoard(); //print board
            printStats(); //print stats
            gO = gameOver(); //check if game is over
            turn++;
        }

        removeObserver(tracker);
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
