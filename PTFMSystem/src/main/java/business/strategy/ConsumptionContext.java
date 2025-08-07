package business.strategy;

/**
 * Context class for applying a fuel or energy consumption calculation strategy.
 * 
 * This class uses the Strategy design pattern to allow dynamic selection of a
 * {@code ConsumptionStrategy} implementation (e.g., for buses, electric vehicles, etc.).
 * 
 * The strategy must be set using {@code setStrategy()} before calling {@code executeStrategy()}.
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
public class ConsumptionContext {
    private ConsumptionStrategy strategy;

    /**
     * Sets the consumption calculation strategy.
     *
     * @param strategy the {@code ConsumptionStrategy} to use
     */
    public void setStrategy(ConsumptionStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Executes the currently set strategy to calculate consumption.
     *
     * @param distance the distance traveled (in kilometers)
     * @param fuelOrEnergyUsed the fuel or energy used (in liters or kWh)
     * @return the calculated consumption metric based on the strategy
     * @throws IllegalStateException if no strategy has been set
     */
    public double executeStrategy(double distance, double fuelOrEnergyUsed) {
        if (strategy == null) {
            throw new IllegalStateException("Strategy not set.");
        }
        return strategy.calculateConsumption(distance, fuelOrEnergyUsed);
    }
}
