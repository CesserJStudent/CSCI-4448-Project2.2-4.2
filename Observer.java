import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

interface Observer {
    public void update(HashMap<String, String> event);
    public String processEvent(HashMap<String, String> event);

    default public FileWriter openFile(String fileName) {
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
    default public void writeToFile(FileWriter outputFile, String output) {
        try {
            outputFile.write(output);
        } 
        catch (IOException err) {
            err.printStackTrace();
        }
    }

    default public void closeFile(FileWriter outputFile) {
        try {
            outputFile.close();
        } 
        catch (IOException err) {
            err.printStackTrace();
        }
    }

    default public String convertName(String playerName) {
        if (playerName == null) return null;
        switch(playerName) {
            // adventurer names
            case "B":
                return "Brawler";
            case "S":
                return "Sneaker";
            case "R":
                return "Runner";
            case "T":
                return "Thief";
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

// OBSERVER: By implemented Observer, Logger can receive events from a Subject.
class Logger implements Observer {
    FileWriter outputFile;
    String fileName;

    Logger(int turn) {
        fileName = "Logger-" + turn + ".txt"; 
        outputFile = openFile(fileName);
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
        String adventurerName = convertName(event.get("advName"));
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

class Tracker implements Observer {
    FileWriter outputFile;
    String fileName = "Tracker.txt";
    ArrayList<String> brawlerStats = new ArrayList<String>();
    ArrayList<String> sneakerStats = new ArrayList<String>();
    ArrayList<String> runnerStats = new ArrayList<String>();
    ArrayList<String> thiefStats = new ArrayList<String>();

    Tracker() {
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
        if (event.get("BLocation") != null) { // only enters if the brawler is still alive since all brawler keys are null when they're dead.
            if (!brawlerStats.isEmpty()) { // clears out the previous data in brawlerStats if it existed
                brawlerStats.clear();
            }
            // updates brawlerStats to their most recent stats at the end of a turn. 
            brawlerStats.add(event.get("BLocation"));
            brawlerStats.add(event.get("BHealth"));
            brawlerStats.add(event.get("BTreasure"));
        }
        else { // the adventurer died so their health should be set to 0, but change nothing else to preserve their final stats in Tracker output
            if (brawlerStats.get(1) != "0") brawlerStats.set(1, "0"); // only enters the first time the adventurer died
        }

        if (event.get("SLocation") != null) {
            if (!sneakerStats.isEmpty()) {
                sneakerStats.clear();
            }
            sneakerStats.add(event.get("SLocation"));
            sneakerStats.add(event.get("SHealth"));
            sneakerStats.add(event.get("STreasure"));
        }
        else {
            if (sneakerStats.get(1) != "0") sneakerStats.set(1, "0");
        }

        if (event.get("RLocation") != null) {
            if (!runnerStats.isEmpty()) {
                runnerStats.clear();
            }
            runnerStats.add(event.get("RLocation"));
            runnerStats.add(event.get("RHealth"));
            runnerStats.add(event.get("RTreasure"));
        }
        else {
            if (runnerStats.get(1) != "0") runnerStats.set(1, "0");
        }

        if (event.get("TLocation") != null) {
            if (!thiefStats.isEmpty()) {
                thiefStats.clear();
            }
            thiefStats.add(event.get("TLocation"));
            thiefStats.add(event.get("THealth"));
            thiefStats.add(event.get("TTreasure"));
        }
        else {
            if (thiefStats.get(1) != "0") thiefStats.set(1, "0");
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

            //
            String currLine = String.format(advFormat, "ADVENTURERS", "ROOM", "HEALTH", "TREASURE") + nl;
            statusUpdate += currLine;
            currLine = String.format(advFormat, "Brawler", brawlerStats.get(0), brawlerStats.get(1), brawlerStats.get(2)) + nl;
            statusUpdate += currLine;
            currLine = String.format(advFormat, "Sneaker", sneakerStats.get(0), sneakerStats.get(1), sneakerStats.get(2)) + nl;
            statusUpdate += currLine;
            currLine = String.format(advFormat, "Runner", runnerStats.get(0), runnerStats.get(1), runnerStats.get(2)) + nl;
            statusUpdate += currLine;
            currLine = String.format(advFormat, "Thief", thiefStats.get(0), thiefStats.get(1), thiefStats.get(2)) + nl + nl;
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