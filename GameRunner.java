import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GameRunner {
    GameBoard playSpace = new GameBoard(); //create game board
    ArrayList<Adventurer> aliveAdventurers = new ArrayList<Adventurer>(); //list of alive adventurers
    ArrayList<Creature> aliveCreatures = new ArrayList<Creature>(); //list of alive creatures


    int loot = 0;

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
                curRoom.removeCreature(curCre);;
                aliveCreatures.remove(curCre);
                }
            else if (winner != null) { //if creature wins
                curAdv.health -= 1; //if adventurer loses, reduce health by 1
            }
        }
        if (curAdv.health <= 0) { //if adventurer dies, remove from aliveAdventurers and room
            curRoom.removeAdventurer(curAdv);
            aliveAdventurers.remove(curAdv);
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
            }
            else if(winner != null) { //if creature wins
                curAdv.health -= 1; //if adventurer loses, reduce health by 1
            }

            if (curAdv.health <= 0) { //if adventurer dies, remove from aliveAdventurers and room
                curRoom.removeAdventurer(curAdv);
                aliveAdventurers.remove(curAdv);
            }
        }
    }


    public void creatureAction() { //creature action
        for (int i = 0; i < aliveCreatures.size(); i++) {
            Creature curCre = aliveCreatures.get(i); //get current creature
            Room curRoom = curCre.board.getRoomAt(curCre.location); //get location
            if(curCre.name == "BK" || curCre.name == "OB" && !curRoom.hasAdventurer()) { //if needed move
                curCre.move();      // POLYMORPHISM: move() is called with the same name, but performs differently depending on which creature it's applied to
            }
            else if(curCre.name == "SK") {
                curCre.move();
            }
            curRoom = curCre.board.getRoomAt(curCre.location); //get new location
            if(curRoom.hasAdventurer()) {
                fightAdventurer(curCre, curRoom); //fight adventurer
            }
        }
    }
    public void adventurerTurn(Adventurer curAdv, Room curRoom) {
        if (curAdv.getRandInt(3) == 1) { // 1/3% chance to use portal if owned
            if (!checkAndRunPortal(curAdv)) {
                curAdv.move(); //move adventurer
            }
        }
        else {
            curAdv.move(); //move adventurer
        }
        curRoom = curAdv.board.getRoomAt(curAdv.location);
        if (curRoom.hasCreature()) {
            fightCreature(curAdv, curRoom);
        }
        else {
            curAdv.search.loot(curAdv, curRoom);

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

        adventurerAction(); //run adventurer actions
        creatureAction(); //run creature actions

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
        }
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
