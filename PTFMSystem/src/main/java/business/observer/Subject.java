package business.observer;

/**
 * Subject interface for the Observer design pattern.
 * 
 * Classes that implement this interface manage a list of observers
 * and provide methods to register, remove, and notify them of updates.
 * 
 * This is typically used to decouple components and promote reactive updates,
 * such as alerting systems or UI refresh mechanisms when data changes.
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
public interface Subject {

    /**
     * Registers an observer to receive updates from this subject.
     *
     * @param o the observer to be registered
     */
    void registerObserver(Observer o);

    /**
     * Removes a previously registered observer.
     *
     * @param o the observer to be removed
     */
    void removeObserver(Observer o);

    /**
     * Notifies all registered observers with the given message.
     *
     * @param message the message or data update to be sent to observers
     */
    void notifyObservers(String message);
}
