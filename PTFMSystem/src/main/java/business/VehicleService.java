package business;

import dataaccess.VehicleDAO;
import transferobjects.Vehicle;
import java.util.List;

public class VehicleService {
    private final VehicleDAO vehicleDAO = new VehicleDAO();

    public void addVehicle(Vehicle vehicle) throws Exception {
        if (vehicle.getVehicleNumber() == null || vehicle.getVehicleNumber().trim().isEmpty()) {
            throw new Exception("Vehicle number cannot be empty.");
        }
        if (vehicleDAO.getAllVehicles().stream()
                .anyMatch(v -> v.getVehicleNumber().equalsIgnoreCase(vehicle.getVehicleNumber()))) {
            throw new Exception("Vehicle number already exists.");
        }
        vehicleDAO.addVehicle(vehicle);
    }
    
    public void updateVehicle(Vehicle vehicle) throws Exception {
    if (vehicle.getVehicleId() <= 0) {
        throw new Exception("Invalid vehicle ID.");
    }
    vehicleDAO.updateVehicle(vehicle);
}

    public void deleteVehicle(int vehicleId) throws Exception {
        if (vehicleId <= 0) {
            throw new Exception("Invalid vehicle ID.");
        }
        vehicleDAO.deleteVehicle(vehicleId);
    }

    public List<Vehicle> getAllVehicles() throws Exception {
        return vehicleDAO.getAllVehicles();
    }
}
