package business;

import business.observer.ConsumptionMonitor;
import business.observer.ManagerObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides consumption monitoring logic for vehicles,
 * allowing alert tracking and manager observer registration.
 * Works with {@link ConsumptionMonitor} and {@link ManagerObserver}
 * to notify when thresholds are exceeded.
 * 
 * @author Annabel Cheng
 * @version Course 25S CST8288 Lab013 Final Project
 */
public class ConsumptionService {

    private final ConsumptionMonitor monitor;
    private final List<String> alertMessages;

    /**
     * Constructs a new ConsumptionService and initializes
     * the observer monitor and alert message list.
     */
    public ConsumptionService() {
        this.monitor = new ConsumptionMonitor();
        this.alertMessages = new ArrayList<>();
    }

    /**
     * Registers a manager as an observer for consumption alerts.
     *
     * @param managerName the name of the manager to register
     */
    public void registerManager(String managerName) {
        monitor.registerObserver(new ManagerObserver(managerName));
    }

    /**
     * Clears the current list of alert messages.
     */
    public void clearAlerts() {
        alertMessages.clear();
    }

    /**
     * Calculates the actual consumption based on vehicle type and usage,
     * compares it against a threshold, and triggers alerts if exceeded.
     *
     * @param vehicleType the type of vehicle (e.g., Diesel, Electric Light Rail)
     * @param vehicleNumber the identifier of the vehicle
     * @param distance the distance travelled (in km)
     * @param fuelUsed the amount of fuel used (in liters)
     * @param energyUsed the amount of energy used (in kWh)
     * @param threshold the consumption threshold to compare against
     * @return the calculated actual consumption in L/100km or kWh/100km
     */
    public double calculateAndCheck(String vehicleType, String vehicleNumber,
                                    double distance, double fuelUsed, double energyUsed,
                                    double threshold) {
        // Actual consumption = (fuel or energy used) / (distance / 100)
        double actualConsumption = 0.0;
        if ("Electric Light Rail".equals(vehicleType)) {
            actualConsumption = energyUsed / (distance / 100);
        } else {
            actualConsumption = fuelUsed / (distance / 100);
        }

        if (actualConsumption > threshold) {
            String message = "ALERT: " + vehicleNumber + " (" + vehicleType +
                             ") consumption = " + String.format("%.2f", actualConsumption) +
                             " exceeds threshold " + threshold;
            alertMessages.add(message);
            monitor.checkConsumption(actualConsumption, threshold, vehicleNumber);
        }

        return actualConsumption; // Return actual consumption in L/100km or kWh/100km
    }

    /**
     * Retrieves the list of current alert messages.
     *
     * @return a list of alert message strings
     */
    public List<String> getAlertMessages() {
        return alertMessages;
    }
}
