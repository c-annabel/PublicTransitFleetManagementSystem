package business;

import business.observer.ConsumptionMonitor;
import business.observer.ManagerObserver;


import java.util.ArrayList;
import java.util.List;

public class ConsumptionService {

    private final ConsumptionMonitor monitor;
    private final List<String> alertMessages;

    public ConsumptionService() {
        this.monitor = new ConsumptionMonitor();
        this.alertMessages = new ArrayList<>();
    }

    public void registerManager(String managerName) {
        monitor.registerObserver(new ManagerObserver(managerName));
    }
    
    public void clearAlerts() {
    alertMessages.clear();
    }

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

    public List<String> getAlertMessages() {
        return alertMessages;
    }
}
