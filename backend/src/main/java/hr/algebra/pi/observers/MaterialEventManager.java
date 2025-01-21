package hr.algebra.pi.observers;

import java.util.ArrayList;
import java.util.List;

public class MaterialEventManager {
    private final List<MaterialObserver> observers = new ArrayList<>();

    public void subscribe(MaterialObserver observer) {
        observers.add(observer);
    }

    public void unsubscribe(MaterialObserver observer) {
        observers.remove(observer);
    }

    public void notify(String event, Object data) {
        for (MaterialObserver observer : observers) {
            observer.onMaterialEvent(event, data);
        }
    }
}

