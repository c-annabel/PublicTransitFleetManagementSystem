package business.strategy;

/**
 * Concrete implementation of the {@code ConsumptionStrategy} interface
 * for calculating the fuel efficiency of buses.
 * 
 * This strategy computes consumption as kilometers per liter (km/L),
 * which reflects how far a bus can travel per unit of fuel used.
 * 
 * Returns 0 if fuel used is 0 to avoid division by zero.
 * 
 * This class is part of the Strategy design pattern for flexible consumption calculation.
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
public class BusConsumptionStrategy implements ConsumptionStrategy {

    /**
     * Calculates fuel efficiency based on distance traveled and fuel used.
     *
     * @param distance the distance traveled in kilometers
     * @param fuelUsed the amount of fuel consumed in liters
     * @return the fuel efficiency in kilometers per liter, or 0 if fuelUsed is 0
     */
    @Override
    public double calculateConsumption(double distance, double fuelUsed) {
        // Efficiency = Distance per liter
        if (fuelUsed == 0) return 0;
        return distance / fuelUsed; // km per liter
    }
}
