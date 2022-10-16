import java.util.Locale;
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
            Scanner commandScan = new Scanner(System.in);
            System.out.println("Would you like to move again");
            String chooseCommand = commandScan.nextLine();
            if(chooseCommand.equalsIgnoreCase("yes") || chooseCommand.equalsIgnoreCase("y")) {
                curAdv.move();
            }
        }
        else {
            curAdv.move();
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