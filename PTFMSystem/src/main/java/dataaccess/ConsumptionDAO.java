package dataaccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import transferobjects.ConsumptionRecord;

/**
 * Data Access Object (DAO) for managing ConsumptionRecord objects in the database.
 * This class handles all database interactions related to consumption data.
 * * @author Annabel Cheng
 * @comment CST8288 Lab 013 Final Project
 */
public class ConsumptionDAO {
    /**
     * The DataSource instance used to get database connections.
     */
    private final DataSource dataSource;

    /**
     * Constructs a new ConsumptionDAO and initializes the DataSource instance.
     */
    public ConsumptionDAO() {
        dataSource = DataSource.getInstance();
    }

    /**
     * Retrieves all consumption records from the database. This includes vehicle
     * information joined with consumption log data.
     * * @return A List of all ConsumptionRecord objects.
     * @throws SQLException If a database access error occurs.
     */
    public List<ConsumptionRecord> getAllConsumption() throws SQLException {
        List<ConsumptionRecord> records = new ArrayList<>();
        String query = "SELECT v.vehicle_id, v.vehicle_number, v.vehicle_type, " +
                       "c.distance_travelled, c.fuel_used, c.energy_used " +
                       "FROM Vehicles v " +
                       "JOIN ConsumptionLogs c ON v.vehicle_id = c.vehicle_id";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ConsumptionRecord record = new ConsumptionRecord();
                record.setVehicleId(rs.getInt("vehicle_id"));
                record.setVehicleNumber(rs.getString("vehicle_number"));
                record.setVehicleType(rs.getString("vehicle_type"));
                record.setDistance(rs.getDouble("distance_travelled"));
                record.setFuelUsed(rs.getDouble("fuel_used"));
                record.setEnergyUsed(rs.getDouble("energy_used"));
                records.add(record);
            }
        }
        return records;
    }
}