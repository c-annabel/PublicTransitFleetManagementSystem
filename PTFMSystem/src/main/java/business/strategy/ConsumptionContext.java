package business.strategy;


public class ConsumptionContext {
    private ConsumptionStrategy strategy;

    public void setStrategy(ConsumptionStrategy strategy) {
        this.strategy = strategy;
    }

    public double executeStrategy(double distance, double fuelOrEnergyUsed) {
        if (strategy == null) {
            throw new IllegalStateException("Strategy not set.");
        }
        return strategy.calculateConsumption(distance, fuelOrEnergyUsed);
    }
}
