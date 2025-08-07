package dataaccess;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Data Access Object (DAO) for retrieving vehicle configuration data,
 * particularly threshold values associated with vehicle types.
 * This class interfaces with the VehicleTypeConfig table.
 * 
 * @author Annabel Cheng
 * @comment Course 25S CST8288 Lab013 Final Project
 */
public class VehicleConfigDAO {

    private final DataSource dataSource;

    /**
     * Constructs a VehicleConfigDAO and initializes the DataSource using the Singleton pattern.
     */
    public VehicleConfigDAO() {
        dataSource = DataSource.getInstance();
    }

    /**
     * Retrieves threshold values for each vehicle type from the database.
     *
     * @return A map where the key is the vehicle type (String) and the value is the threshold (Double).
     * @throws SQLException if a database access error occurs.
     */
    public Map<String, Double> getThresholds() throws SQLException {
        Map<String, Double> thresholds = new HashMap<>();
        String query = "SELECT vehicle_type, threshold FROM VehicleTypeConfig";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                thresholds.put(rs.getString("vehicle_type"), rs.getDouble("threshold"));
            }
        }
        return thresholds;
    }
}
