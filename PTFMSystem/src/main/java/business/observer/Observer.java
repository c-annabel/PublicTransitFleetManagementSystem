package business.observer;

/**
 * Observer interface for implementing the Observer design pattern.
 * 
 * Classes that implement this interface will receive update notifications
 * from a {@code Subject} when relevant data changes or specific events occur.
 * 
 * Typically used in scenarios like monitoring, alert systems, or UI refresh mechanisms.
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
public interface Observer {

    /**
     * Called by the subject to notify the observer of an event or state change.
     *
     * @param message the message or data update sent from the subject
     */
    void update(String message);
}
