package business.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Monitors vehicle consumption and notifies registered observers
 * when the consumption exceeds a defined threshold.
 * 
 * This class implements the Subject interface in the Observer pattern.
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
public class ConsumptionMonitor implements Subject {
    private final List<Observer> observers;

    /**
     * Constructs a new ConsumptionMonitor with an empty list of observers.
     */
    public ConsumptionMonitor() {
        observers = new ArrayList<>();
    }

    /**
     * Registers an observer to receive notifications.
     *
     * @param o the observer to be added
     */
    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    /**
     * Removes a previously registered observer.
     *
     * @param o the observer to be removed
     */
    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    /**
     * Notifies all registered observers with a message.
     *
     * @param message the message to send to all observers
     */
    @Override
    public void notifyObservers(String message) {
        for (Observer o : observers) {
            o.update(message);
        }
    }

    /**
     * Checks if a given consumption value exceeds the specified threshold.
     * If it does, notifies all observers with an alert message.
     *
     * @param value the current consumption value
     * @param threshold the maximum allowable threshold
     * @param vehicleNumber the identifier of the vehicle being monitored
     */
    public void checkConsumption(double value, double threshold, String vehicleNumber) {
        if (value > threshold) {
            String alertMessage = "Alert: " + vehicleNumber + " exceeded threshold! (" + value + " > " + threshold + ")";
            notifyObservers(alertMessage);
        }
    }
}
