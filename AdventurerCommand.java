// import java.util.Locale;
import java.util.Scanner;

public abstract class AdventurerCommand { //abstract class for adventurer commands
    GameRunner runner;
    Adventurer curAdv;
    Room curRoom;
    abstract void execute();
}

class FightCommand extends AdventurerCommand { //fight command class
    FightCommand(Adventurer adv, GameRunner gRunner) {
        curAdv = adv;
        curRoom = adv.board.getRoomAt(adv.location);
        runner = gRunner;
    }
    @Override
    void execute() {
        runner.fightCreature(curAdv, curRoom);
    }
}

class MoveCommand extends AdventurerCommand { //move command class
    MoveCommand(Adventurer adv, GameRunner gRunner) {
        curAdv = adv;
        curRoom = adv.board.getRoomAt(adv.location);
        runner = gRunner;
    }
    @Override
    void execute() {
        if(curAdv instanceof Runner) { //if the adventurer is a runner, they get to move twice
            curAdv.move();
            runner.addEvent("advMove", curAdv, null);
            escapeDamage();
            if (curAdv.health <= 0) {
                return; // prevents zombie runner turns
            }
            GameBoard.getBoard().printBoard();
            Scanner commandScan = UserInput.getInput().getScanner();
            System.out.println("Next movement will avoid damage taken from escaping creatures. Would you like to move again? (y/n)");
            String chooseCommand = commandScan.nextLine();
            if(chooseCommand.equalsIgnoreCase("yes") || chooseCommand.equalsIgnoreCase("y")) {
                curAdv.move(); // no escapeDamage necessary for second runner movement
                runner.addEvent("advMove", curAdv, null);
            }
        }
        else { //if the adventurer is not a runner, they only get to move once
            curAdv.move();
            runner.addEvent("advMove", curAdv, null);
            escapeDamage();
        }
    }
    
    // damages an adventurer if they choose to move from a room that has creatures
    private void escapeDamage() {
        int damage = 0;
        if (curRoom.hasCreature()) {
            damage += curRoom.creaturesPresent.size();
        }
        if (damage > 0) {
            curAdv.health -= damage;
            runner.addEvent("advHurt", curAdv, null);
            runner.checkForAdvDeath(curAdv, curRoom);
        }
    }
}

class CelebrateCommand extends AdventurerCommand { //celebrate command class
    CelebrateCommand(Adventurer adv, GameRunner gRunner) { //constructor
        curAdv = adv;
        curRoom = adv.board.getRoomAt(adv.location);
        runner = gRunner;
    }
    @Override
    void execute() {
        System.out.println(curAdv.name + " celebrates: " + runner.decorateAdv(curAdv));
        runner.addEvent("advCelebrate", curAdv, null);
    }
}

class SearchCommand extends AdventurerCommand { //search command class
    SearchCommand(Adventurer adv, GameRunner gRunner) {
        curAdv = adv;
        curRoom = adv.board.getRoomAt(adv.location);
        runner = gRunner;
    }
    @Override
    void execute() {
        runner.searchAndReturn(curAdv, curRoom);
    }
}