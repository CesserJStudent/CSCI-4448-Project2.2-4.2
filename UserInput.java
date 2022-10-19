import java.util.Scanner;
public class UserInput {
    // singleton for userInput, needed so input only opens once at the start of the game and closes once the game ends. 
    private static UserInput input = new UserInput();
    private Scanner scanner;

    private UserInput() {
        scanner = new Scanner(System.in);
    }

    public static UserInput getInput() {
        return input;
    }

    public Scanner getScanner() {
        return scanner;
    }
}
