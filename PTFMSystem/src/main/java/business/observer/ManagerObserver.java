
package business.observer;


public class ManagerObserver implements Observer {
    private final String managerName;

    public ManagerObserver(String managerName) {
        this.managerName = managerName;
    }

    @Override
    public void update(String message) {
        // For now, just print the alert (later can integrate with UI)
        System.out.println("Manager [" + managerName + "] received alert: " + message);
    }
}
