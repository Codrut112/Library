package observer;


import java.util.ArrayList;
import java.util.List;

public interface Observable {
    List<Observer> observers = new ArrayList<Observer>();

    void addObserver(Observer o);

    void removeObserver(Observer o);

    void notifyall();
}
