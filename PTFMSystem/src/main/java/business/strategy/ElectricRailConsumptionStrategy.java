package business.strategy;

/**
 * Concrete strategy for calculating energy efficiency of electric rail systems.
 * 
 * This class implements the {@code ConsumptionStrategy} interface and computes
 * consumption as kilometers per kilowatt-hour (km/kWh), which indicates how far
 * the vehicle travels per unit of electrical energy consumed.
 * 
 * Returns 0 if {@code energyUsed} is 0 to prevent division by zero.
 * 
 * Suitable for electric trains or light rail transit systems using electricity as the energy source.
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
public class ElectricRailConsumptionStrategy implements ConsumptionStrategy {

    /**
     * Calculates the energy efficiency based on distance traveled and energy consumed.
     *
     * @param distance the distance traveled in kilometers
     * @param energyUsed the energy used in kilowatt-hours (kWh)
     * @return the energy efficiency in kilometers per kilowatt-hour, or 0 if {@code energyUsed} is 0
     */
    @Override
    public double calculateConsumption(double distance, double energyUsed) {
        // Efficiency = Distance per kWh
        if (energyUsed == 0) return 0;
        return distance / energyUsed; // km per kWh
    }
}
