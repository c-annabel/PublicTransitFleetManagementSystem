package business.strategy;

public class ElectricRailConsumptionStrategy implements ConsumptionStrategy {
    @Override
    public double calculateConsumption(double distance, double energyUsed) {
        // Efficiency = Distance per kWh
        if (energyUsed == 0) return 0;
        return distance / energyUsed; // km per kWh
    }
}