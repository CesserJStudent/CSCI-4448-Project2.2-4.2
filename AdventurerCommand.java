// import java.util.Locale;
import java.util.Scanner;

public class AdventurerCommand {
    GameRunner runner;
    Adventurer curAdv;
    Room curRoom;
   void execute() {};
}

class FightCommand extends AdventurerCommand {
    FightCommand(Adventurer adv, Room room, GameRunner gRunner) {
        curAdv = adv;
        curRoom = room;
        runner = gRunner;
    }
    @Override
    void execute() {
        runner.fightCreature(curAdv, curRoom);
    }
}

class MoveCommand extends AdventurerCommand {
    MoveCommand(Adventurer adv, Room room, GameRunner gRunner) {
        curAdv = adv;
        curRoom = room;
        runner = gRunner;
    }
    @Override
    void execute() {
        if(curAdv instanceof Runner) {
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
                curAdv.move(); // no fleeDamage necessary for second runner movement
                runner.addEvent("advMove", curAdv, null);
            }
        }
        else {
            curAdv.move();
            runner.addEvent("advMove", curAdv, null);
            escapeDamage();
        }
    }
    
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

class CelebrateCommand extends AdventurerCommand {
    CelebrateCommand(Adventurer adv, Room room, GameRunner gRunner) {
        curAdv = adv;
        curRoom = room;
        runner = gRunner;
    }
    @Override
    void execute() {
        System.out.println(curAdv.name + " celebrates: " + runner.decorateAdv(curAdv));
        runner.addEvent("advCelebrate", curAdv, null);
    }
}

class SearchCommand extends AdventurerCommand {
    SearchCommand(Adventurer adv, Room room, GameRunner gRunner) {
        curAdv = adv;
        curRoom = room;
        runner = gRunner;
    }
    @Override
    void execute() {
        runner.searchAndReturn(curAdv, curRoom);
    }
}