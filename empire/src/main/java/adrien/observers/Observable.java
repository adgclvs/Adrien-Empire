package adrien.observers;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable {
    private List<Observer> observers;

    public Observable() {
        observers = new ArrayList<>();
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    protected void notifyObservers() {
        List<Observer> observersCopy;
        synchronized (this) {
            observersCopy = new ArrayList<>(observers);
        }
        for (Observer observer : observersCopy) {
            observer.update();
        }
    }
}
