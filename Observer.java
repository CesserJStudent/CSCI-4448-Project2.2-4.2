import java.util.ArrayList;

interface Observer {
    public void update(ArrayList<String> event);
}

class Logger implements Observer {
    public void update(ArrayList<String> event) {
        // do stuff to process the passed in event
    }
}

class Tracker implements Observer {
    public void update(ArrayList<String> event) {
        // do stuff to process the passed in event
    }
}