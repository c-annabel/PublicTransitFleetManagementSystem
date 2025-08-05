package transferobjects;

public class ConsumptionRecord {
    private int vehicleId;
    private String vehicleNumber;
    private String vehicleType;
    private double distance;
    private double fuelUsed;
    private double energyUsed;

    // Getters and setters
    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }

    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }

    public double getFuelUsed() { return fuelUsed; }
    public void setFuelUsed(double fuelUsed) { this.fuelUsed = fuelUsed; }

    public double getEnergyUsed() { return energyUsed; }
    public void setEnergyUsed(double energyUsed) { this.energyUsed = energyUsed; }
}
