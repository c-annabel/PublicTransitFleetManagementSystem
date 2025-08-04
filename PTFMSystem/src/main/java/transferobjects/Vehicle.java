package transferobjects;

public class Vehicle {
    private int vehicleId;
    private String vehicleNumber;
    private String vehicleType;
    private String fuelType;
    private double consumptionRate;
    private int maxPassengers;
    private int routeId;

    // Getters and Setters
    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }

    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }

    public double getConsumptionRate() { return consumptionRate; }
    public void setConsumptionRate(double consumptionRate) { this.consumptionRate = consumptionRate; }

    public int getMaxPassengers() { return maxPassengers; }
    public void setMaxPassengers(int maxPassengers) { this.maxPassengers = maxPassengers; }

    public int getRouteId() { return routeId; }
    public void setRouteId(int routeId) { this.routeId = routeId; }
}
