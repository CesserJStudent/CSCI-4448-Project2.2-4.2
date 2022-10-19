import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

abstract class Observer {
    abstract public void update(HashMap<String, String> event);
    abstract public String processEvent(HashMap<String, String> event);

    static protected FileWriter openFile(String fileName) {
        try {
            File directory = new File("Logger-Tracker output");
            File file = new File(directory, fileName);
            FileWriter outputFile = new FileWriter(file);
            return outputFile;
        } 
        catch (IOException err) {
            err.printStackTrace();
            return null;
        }
    }
    static protected void writeToFile(FileWriter outputFile, String output) {
        try {
            outputFile.write(output);
        } 
        catch (IOException err) {
            err.printStackTrace();
        }
    }

    static protected void closeFile(FileWriter outputFile) {
        try {
            outputFile.close();
        } 
        catch (IOException err) {
            err.printStackTrace();
        }
    }

    static public String convertName(String playerName) {
        if (playerName == null) return null;
        switch(playerName) {
            // creature names
            case "OB":
                return "Orbiter";
            case "SK":
                return "Seeker";
            case "BK":
                return "Blinker";
            default:
                return null;
        }
    }
}

// OBSERVER: By implementing Observer, Logger can receive events from a Subject.
class Logger extends Observer {
    static FileWriter outputFile;
    static String fileName;
    private static Logger uniqueInstance;

    private Logger(int turn) {
        fileName = "Logger-" + turn + ".txt"; 
        outputFile = openFile(fileName);
    }

    public static synchronized Logger getInstance(int turn) {
        if (uniqueInstance == null) {
            uniqueInstance = new Logger(turn);
        }
        fileName = "Logger-" + turn +".txt";
        outputFile = openFile(fileName);
        return uniqueInstance;
    }
    
    public void update(HashMap<String, String> event) {
        // returns the string to be printed to a text file
        String eventOutput = processEvent(event);
        if (eventOutput != null) {
            writeToFile(outputFile, eventOutput);
        }
        else {
            closeFile(outputFile);
        }
    }

    public String processEvent(HashMap<String, String> event) {
        // these are all the valid keys of "event". any keys not in the current "event" will have null values
        String readableEvent = null;
        String adventurerName = event.get("advName");
        String adventurerHealth = event.get("advHealth");
        String creatureName = convertName(event.get("creName"));
        String location = event.get("location");
        String treasureName = event.get("treasureName");
        
        // updates "readableEvent" based on the event's type
        String eventType = event.get("eventType");
        switch(eventType) {
            case "advMove":
                readableEvent = adventurerName + " has moved to room " + location + ".";
                break;
            case "advWin":
                readableEvent = adventurerName + " has defeated " + creatureName + "!";
                break;
            case "advLoss":
                readableEvent = adventurerName + " has been defeated by " + creatureName + "!";
                break;
            case "advHurt":
                readableEvent = adventurerName + " has been hurt! Their health is now " + adventurerHealth + ".";
                break;
            case "advHeal":
                readableEvent = adventurerName + " drank a potion and healed! Their health is now " + adventurerHealth + ".";
                break;
            case "advDeath":
                readableEvent = adventurerName + " has died...";
                break;
            case "advCelebrate":
                readableEvent = adventurerName + " celebrates! (but it's output currently only prints to the terminal)";
                break;
            case "foundTreasure":
                readableEvent = adventurerName + " has found a treasure! They have gained the effects of " + treasureName + ".";
                break;
            case "foundTrap":
                readableEvent = adventurerName + " has found a trap...";
                break;
            case "creMove":
                readableEvent = creatureName + " has moved to room " + location + ".";
                break;
            case "creWin":
                readableEvent = creatureName + " has defeated " + adventurerName + "!";
                break;
            case "creLoss":
                readableEvent = creatureName + " has been defeated by " + adventurerName + "!";
                break;
            case "creDeath":
                readableEvent = creatureName + " has died...";
                break;
            case "turnEnd": // loggers don't do anything at turn end. this sets "readableEvent" to null and causes closeFile() to be called in update
                break;
        }
        // appends a newline to the end of the event if it was set to a string
        if (readableEvent != null) {
            readableEvent += "\n";
        }
        return readableEvent;
    }
}

class Tracker extends Observer {
    FileWriter outputFile;
    String fileName = "Tracker.txt";
    ArrayList<String> adventurerStats = new ArrayList<String>();

    private static Tracker uniqueInstance = new Tracker();

    public static Tracker getTracker() {
        return uniqueInstance;
    }

    private Tracker() {
        outputFile = openFile(fileName);
    }

    public void update(HashMap<String, String> event) {
        // returns the string to be printed to a text file
        String eventOutput = processEvent(event);
        // closes the file when "event' only contains the "gameEnd" key
        if (eventOutput == null) {
            closeFile(outputFile);
        }
        // writes to the file when "event" contains the "turnEnd" key
        else if (eventOutput != "") {
            writeToFile(outputFile, eventOutput);
        }
    }

    // helper function for processEvent. needed so that tracker values aren't all set to null when an adventurer dies
    private void updateAdventurerStats(HashMap<String, String> event) {
        String adventurerName = event.get("advName");
        if (adventurerName != null) { // only enters if the adventurer is still alive since all adventurer keys are null when they're dead.
            if (!adventurerStats.isEmpty()) { // clears out the previous data in adventurerStats if it existed
                adventurerStats.clear();
            }
            // updates adventurerStats to their most recent stats at the end of a turn.
            adventurerStats.add(adventurerName);
            adventurerStats.add(event.get(adventurerName + "Location"));
            adventurerStats.add(event.get(adventurerName + "Health"));
            adventurerStats.add(event.get(adventurerName + "Treasure"));
        }
        else { // the adventurer died so their health should be set to 0, but change nothing else to preserve their final stats in Tracker output
            if (adventurerStats.get(2) != "0") adventurerStats.set(2, "0"); // only enters the first time the adventurer died
        }
    }

    public String processEvent(HashMap<String, String> event) {
        String statusUpdate = "";
        String eventType = event.get("eventType");
        // tracker only cares about the status at the end of turns and the end of the game, so all other event types are ignored.
        if (eventType == "gameEnd") {
            statusUpdate = null;
        }
        else if (eventType == "turnEnd") {
            updateAdventurerStats(event); // updates all adventurer stats in Tracker to the stats sent in event. if an adventurer is dead, their health is set to 0 and all other stats are preserved
            String nl = "\n";
            String advFormat = "%-20s | %-15s | %-10s | %s";
            String creFormat = "%-20s | %-15s";
            statusUpdate += "==========  Turn " + event.get("turn") + "  ==========" + nl + nl;
            statusUpdate += "Total Active Adventurers: " + event.get("totalAdv") + nl;

            String currLine = String.format(advFormat, "ADVENTURERS", "ROOM", "HEALTH", "TREASURE") + nl;
            statusUpdate += currLine;
            currLine = String.format(advFormat, adventurerStats.get(0), adventurerStats.get(1), adventurerStats.get(2), adventurerStats.get(3)) + nl + nl;
            statusUpdate += currLine;
            statusUpdate += "Total Active Creatures: " + event.get("totalCre") + nl;
            currLine = String.format(creFormat, "CREATURES", "ROOM") + nl;
            statusUpdate += currLine;
            for (int i = 0; i < Integer.parseInt(event.get("totalCre")); i++) {
                String curCre = "cre" + (i + 1);
                currLine = String.format(creFormat, convertName(event.get(curCre + "Name")), event.get(curCre + "Location")) + nl;
                statusUpdate += currLine;
            }
            statusUpdate += nl;
        }
        return statusUpdate;
    }
}