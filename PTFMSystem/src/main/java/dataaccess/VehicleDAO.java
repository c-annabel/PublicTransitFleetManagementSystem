package dataaccess;

import transferobjects.Vehicle;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for the Vehicle entity.
 * Handles CRUD operations and data retrieval from the Vehicles table.
 * 
 * Provides methods to insert, update, delete, and retrieve vehicle records.
 * 
 * @author Annabel Cheng
 * @comment Course 25S CST8288 Lab013 Final Project
 */
public class VehicleDAO {
    private final DataSource dataSource;

    /**
     * Constructs a VehicleDAO and initializes the DataSource using the Singleton pattern.
     */
    public VehicleDAO() {
        dataSource = DataSource.getInstance();
    }

    /**
     * Inserts a new vehicle record into the Vehicles table.
     *
     * @param vehicle The Vehicle object containing the details to insert.
     * @throws SQLException if a database access error occurs.
     */
    public void addVehicle(Vehicle vehicle) throws SQLException {
        String sql = "INSERT INTO Vehicles (vehicle_number, vehicle_type, fuel_type, consumption_rate, max_passengers, route_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, vehicle.getVehicleNumber());
            ps.setString(2, vehicle.getVehicleType());
            ps.setString(3, vehicle.getFuelType());
            ps.setDouble(4, vehicle.getConsumptionRate());
            ps.setInt(5, vehicle.getMaxPassengers());
            ps.setInt(6, vehicle.getRouteId());
            ps.executeUpdate();
        }
    }

    /**
     * Updates an existing vehicle record in the Vehicles table.
     *
     * @param vehicle The Vehicle object with updated data.
     * @throws SQLException if a database access error occurs.
     */
    public void updateVehicle(Vehicle vehicle) throws SQLException {
        String sql = "UPDATE Vehicles SET vehicle_number=?, vehicle_type=?, fuel_type=?, consumption_rate=?, max_passengers=?, route_id=? WHERE vehicle_id=?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, vehicle.getVehicleNumber());
            ps.setString(2, vehicle.getVehicleType());
            ps.setString(3, vehicle.getFuelType());
            ps.setDouble(4, vehicle.getConsumptionRate());
            ps.setInt(5, vehicle.getMaxPassengers());
            ps.setInt(6, vehicle.getRouteId());
            ps.setInt(7, vehicle.getVehicleId());
            ps.executeUpdate();
        }
    }

    /**
     * Deletes a vehicle from the Vehicles table using the vehicle ID.
     *
     * @param vehicleId The ID of the vehicle to delete.
     * @throws SQLException if a database access error occurs.
     */
    public void deleteVehicle(int vehicleId) throws SQLException {
        String sql = "DELETE FROM Vehicles WHERE vehicle_id=?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, vehicleId);
            ps.executeUpdate();
        }
    }

    /**
     * Retrieves a list of vehicle IDs and vehicle numbers.
     *
     * @return A list of string arrays, each containing a vehicle ID and its corresponding number.
     * @throws SQLException if a database access error occurs.
     */
    public List<String[]> getVehicleIdAndNumber() throws SQLException {
        String sql = "SELECT vehicle_id, vehicle_number FROM Vehicles";
        List<String[]> list = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new String[]{
                    String.valueOf(rs.getInt("vehicle_id")),
                    rs.getString("vehicle_number")
                });
            }
        }
        return list;
    }

    /**
     * Retrieves a list of all vehicles from the Vehicles table.
     *
     * @return A list of Vehicle objects representing all records in the table.
     * @throws SQLException if a database access error occurs.
     */
    public List<Vehicle> getAllVehicles() throws SQLException {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT * FROM Vehicles";
        try (Connection con = dataSource.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vehicle vehicle = new Vehicle.Builder()
                        .vehicleId(rs.getInt("vehicle_id"))
                        .vehicleNumber(rs.getString("vehicle_number"))
                        .vehicleType(rs.getString("vehicle_type"))
                        .fuelType(rs.getString("fuel_type"))
                        .consumptionRate(rs.getDouble("consumption_rate"))
                        .maxPassengers(rs.getInt("max_passengers"))
                        .routeId(rs.getInt("route_id"))
                        .build();
                list.add(vehicle);
            }
        }
        return list;
    }
}
