package transferobjects;

/**
 * Represents a consumption record for a vehicle, including
 * fuel or energy usage and the distance traveled.
 *
 * This class is used in the PTFMS system to support monitoring,
 * reporting, and analytics for vehicle operations.
 *
 * @Author: Annabel Cheng
 * Course: 25S CST8288 Section 013 Final Project
 */
public class ConsumptionRecord {

    /** The unique ID of the vehicle. */
    private int vehicleId;

    /** The display number or license plate of the vehicle. */
    private String vehicleNumber;

    /** The type of vehicle (e.g., Diesel Bus, Electric Light Rail). */
    private String vehicleType;

    /** Total distance traveled during the recorded period (in km). */
    private double distance;

    /** Total fuel consumed (in liters) for fuel-based vehicles. */
    private double fuelUsed;

    /** Total electrical energy consumed (in kWh) for electric vehicles. */
    private double energyUsed;

    /**
     * Returns the vehicle ID.
     * @return the vehicleId
     */
    public int getVehicleId() {
        return vehicleId;
    }

    /**
     * Sets the vehicle ID.
     * @param vehicleId the vehicle ID to set
     */
    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    /**
     * Returns the vehicle number.
     * @return the vehicleNumber
     */
    public String getVehicleNumber() {
        return vehicleNumber;
    }

    /**
     * Sets the vehicle number.
     * @param vehicleNumber the vehicle number to set
     */
    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    /**
     * Returns the vehicle type.
     * @return the vehicleType
     */
    public String getVehicleType() {
        return vehicleType;
    }

    /**
     * Sets the vehicle type.
     * @param vehicleType the vehicle type to set
     */
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    /**
     * Returns the distance traveled.
     * @return the distance in kilometers
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Sets the distance traveled.
     * @param distance the distance in kilometers to set
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * Returns the amount of fuel used.
     * @return fuelUsed in liters
     */
    public double getFuelUsed() {
        return fuelUsed;
    }

    /**
     * Sets the amount of fuel used.
     * @param fuelUsed the fuel usage in liters
     */
    public void setFuelUsed(double fuelUsed) {
        this.fuelUsed = fuelUsed;
    }

    /**
     * Returns the amount of energy used.
     * @return energyUsed in kilowatt-hours (kWh)
     */
    public double getEnergyUsed() {
        return energyUsed;
    }

    /**
     * Sets the amount of energy used.
     * @param energyUsed the energy usage in kWh
     */
    public void setEnergyUsed(double energyUsed) {
        this.energyUsed = energyUsed;
    }
}
