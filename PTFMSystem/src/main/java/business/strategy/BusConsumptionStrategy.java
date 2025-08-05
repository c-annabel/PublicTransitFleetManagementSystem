package business.strategy;

public class BusConsumptionStrategy implements ConsumptionStrategy {
    @Override
    public double calculateConsumption(double distance, double fuelUsed) {
        // Efficiency = Distance per liter
        if (fuelUsed == 0) return 0;
        return distance / fuelUsed; // km per liter
    }
}
