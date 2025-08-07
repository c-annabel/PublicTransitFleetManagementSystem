package transferobjects;

/**
 * Vehicle.java - Transfer Object representing a Vehicle entity.
 *
 * This class is part of the CST8288 Final Project.
 * It encapsulates vehicle attributes such as ID, number, type, fuel type,
 * and provides standard getters and setters for data access and manipulation.
 *
 * Used across the data access layer and business logic for vehicle management.
 * 
 * @author Annabel Cheng
 */

public class Vehicle {
    private final int vehicleId;
    private final String vehicleNumber;
    private final String vehicleType;
    private final String fuelType;
    private final double consumptionRate;
    private final int maxPassengers;
    private final int routeId;

    // ✅ Private constructor to enforce Builder usage
    private Vehicle(Builder builder) {
        this.vehicleId = builder.vehicleId;
        this.vehicleNumber = builder.vehicleNumber;
        this.vehicleType = builder.vehicleType;
        this.fuelType = builder.fuelType;
        this.consumptionRate = builder.consumptionRate;
        this.maxPassengers = builder.maxPassengers;
        this.routeId = builder.routeId;
    }

    // ✅ Getters
    public int getVehicleId() { return vehicleId; }
    public String getVehicleNumber() { return vehicleNumber; }
    public String getVehicleType() { return vehicleType; }
    public String getFuelType() { return fuelType; }
    public double getConsumptionRate() { return consumptionRate; }
    public int getMaxPassengers() { return maxPassengers; }
    public int getRouteId() { return routeId; }

    // ✅ Builder Class
    public static class Builder {
        private int vehicleId;
        private String vehicleNumber;
        private String vehicleType;
        private String fuelType;
        private double consumptionRate;
        private int maxPassengers;
        private int routeId;

        public Builder vehicleId(int vehicleId) {
            this.vehicleId = vehicleId;
            return this;
        }

        public Builder vehicleNumber(String vehicleNumber) {
            this.vehicleNumber = vehicleNumber;
            return this;
        }

        public Builder vehicleType(String vehicleType) {
            this.vehicleType = vehicleType;
            return this;
        }

        public Builder fuelType(String fuelType) {
            this.fuelType = fuelType;
            return this;
        }

        public Builder consumptionRate(double consumptionRate) {
            this.consumptionRate = consumptionRate;
            return this;
        }

        public Builder maxPassengers(int maxPassengers) {
            this.maxPassengers = maxPassengers;
            return this;
        }

        public Builder routeId(int routeId) {
            this.routeId = routeId;
            return this;
        }

        public Vehicle build() {
            return new Vehicle(this);
        }
    }
}
