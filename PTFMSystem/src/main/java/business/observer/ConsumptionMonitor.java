package business.observer;

import java.util.ArrayList;
import java.util.List;

public class ConsumptionMonitor implements Subject {
    private final List<Observer> observers;
    
    public ConsumptionMonitor() {
        observers = new ArrayList<>();
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer o : observers) {
            o.update(message);
        }
    }

    // Business logic: check if consumption exceeds threshold
    public void checkConsumption(double value, double threshold, String vehicleNumber) {
        if (value > threshold) {
            String alertMessage = "Alert: " + vehicleNumber + " exceeded threshold! (" + value + " > " + threshold + ")";
            notifyObservers(alertMessage);
        }
    }
}
