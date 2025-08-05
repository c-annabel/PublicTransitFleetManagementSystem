package business.strategy;

public class DieselElectricTrainConsumptionStrategy implements ConsumptionStrategy {
    @Override
    public double calculateConsumption(double distance, double fuelUsed) {
        // Efficiency = Distance per liter (specific logic for trains if needed)
        if (fuelUsed == 0) return 0;
        return distance / fuelUsed; // km per liter
    }
}