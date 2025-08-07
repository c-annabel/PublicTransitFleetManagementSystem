package business;

import dataaccess.VehicleDAO;
import transferobjects.Vehicle;
import java.util.List;

/**
 * Service layer for vehicle-related operations such as adding, updating,
 * deleting, and retrieving vehicle data. Validation is handled before
 * delegating persistence operations to {@link VehicleDAO}.
 * 
 * @author Annabel Cheng
 * Course 25S CST8288 Lab013 Final Project
 */
public class VehicleService {
    private final VehicleDAO vehicleDAO = new VehicleDAO();

    /**
     * Adds a new vehicle after validating the vehicle number.
     * Ensures the vehicle number is not empty and does not already exist.
     *
     * @param vehicle the {@code Vehicle} object containing the details to be added
     * @throws Exception if the vehicle number is invalid or already exists
     */
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

    /**
     * Updates an existing vehicle's details.
     * Validates the vehicle ID before delegating to DAO.
     *
     * @param vehicle the {@code Vehicle} object containing updated information
     * @throws Exception if the vehicle ID is invalid
     */
    public void updateVehicle(Vehicle vehicle) throws Exception {
        if (vehicle.getVehicleId() <= 0) {
            throw new Exception("Invalid vehicle ID.");
        }
        vehicleDAO.updateVehicle(vehicle);
    }

    /**
     * Deletes a vehicle from the system using its ID.
     *
     * @param vehicleId the ID of the vehicle to be deleted
     * @throws Exception if the vehicle ID is invalid
     */
    public void deleteVehicle(int vehicleId) throws Exception {
        if (vehicleId <= 0) {
            throw new Exception("Invalid vehicle ID.");
        }
        vehicleDAO.deleteVehicle(vehicleId);
    }

    /**
     * Retrieves a list of vehicle IDs and their corresponding vehicle numbers.
     *
     * @return a list of String arrays, each containing a vehicle ID and number
     * @throws Exception if there is a problem retrieving the data
     */
    public List<String[]> getVehicleIdAndNumber() throws Exception {
        return vehicleDAO.getVehicleIdAndNumber();
    }

    /**
     * Retrieves all vehicles from the database.
     *
     * @return a list of {@code Vehicle} objects
     * @throws Exception if there is a problem retrieving the data
     */
    public List<Vehicle> getAllVehicles() throws Exception {
        return vehicleDAO.getAllVehicles();
    }
}
