package business.strategy;

/**
 * Strategy interface for calculating fuel or energy consumption.
 * 
 * This interface is part of the Strategy design pattern, allowing different
 * implementations for vehicles with different consumption models (e.g., diesel buses,
 * electric trains, etc.).
 * 
 * Implementations define how to compute consumption based on distance and fuel/energy used.
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
public interface ConsumptionStrategy {

    /**
     * Calculates the consumption value based on distance and resource used.
     *
     * @param distance the distance traveled (in kilometers)
     * @param fuelOrEnergyUsed the amount of fuel or energy used (in liters or kWh)
     * @return the computed consumption metric (e.g., km/L or km/kWh)
     */
    double calculateConsumption(double distance, double fuelOrEnergyUsed);
}
