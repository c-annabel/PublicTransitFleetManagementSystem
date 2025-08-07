package business.observer;

/**
 * Concrete implementation of the {@code Observer} interface representing a transit manager.
 * 
 * This observer receives alert messages from the {@code ConsumptionMonitor} and
 * handles them accordingly. Currently, the alert is printed to the console,
 * but future implementations could include UI notifications or logging.
 * 
 * This class participates in the Observer design pattern.
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
public class ManagerObserver implements Observer {
    private final String managerName;

    /**
     * Constructs a {@code ManagerObserver} with the specified manager's name.
     *
     * @param managerName the name of the manager observing the system
     */
    public ManagerObserver(String managerName) {
        this.managerName = managerName;
    }

    /**
     * Called when the subject (e.g., {@code ConsumptionMonitor}) sends an update.
     * Prints the received alert message to the console.
     *
     * @param message the alert message to be processed
     */
    @Override
    public void update(String message) {
        // For now, just print the alert (later can integrate with UI)
        System.out.println("Manager [" + managerName + "] received alert: " + message);
    }
}
