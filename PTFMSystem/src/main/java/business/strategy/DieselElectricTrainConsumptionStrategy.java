package business.strategy;

/**
 * Concrete strategy for calculating fuel efficiency of diesel-electric trains.
 * 
 * This class implements the {@code ConsumptionStrategy} interface and calculates
 * the consumption as kilometers per liter (km/L), which is the ratio of distance
 * traveled to fuel used.
 * 
 * Returns 0 if {@code fuelUsed} is 0 to avoid division by zero.
 * 
 * This class enables dynamic selection of consumption logic for diesel-electric train types.
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
public class DieselElectricTrainConsumptionStrategy implements ConsumptionStrategy {

    /**
     * Calculates the consumption for diesel-electric trains based on distance and fuel used.
     *
     * @param distance the distance traveled in kilometers
     * @param fuelUsed the fuel consumed in liters
     * @return the calculated consumption in kilometers per liter, or 0 if {@code fuelUsed} is 0
     */
    @Override
    public double calculateConsumption(double distance, double fuelUsed) {
        // Efficiency = Distance per liter (specific logic for trains if needed)
        if (fuelUsed == 0) return 0;
        return distance / fuelUsed; // km per liter
    }
}
