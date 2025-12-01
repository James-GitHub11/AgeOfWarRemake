package ObserverPackage;

import java.util.ArrayList;
import java.util.List;

/**
 * Subject: GameEvents. Holds and notifies Observers.
 */
public class GameEvents {
    private final List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer o) { observers.add(o); }
    public void removeObserver(Observer o) { observers.remove(o); }

    public void notifyObservers(String message) {
        for (Observer o : new ArrayList<>(observers)) o.update(message);
    }

    public void performGameEvent(String message) {
        // potential event logic
        notifyObservers(message);
    }
}
