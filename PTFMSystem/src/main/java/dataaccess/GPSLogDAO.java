package dataaccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing GPSLog objects in the database.
 * This class handles all database interactions related to vehicle GPS logs,
 * including arrivals, departures, and detailed log retrieval.
 *
 * @author Annabel Cheng
 * @comment CST8288 Lab 013 Final Project
 */
public class GPSLogDAO {
    /**
     * The DataSource instance used to get database connections.
     */
    private final DataSource dataSource;

    /**
     * Constructs a new GPSLogDAO and initializes the DataSource instance.
     */
    public GPSLogDAO() {
        dataSource = DataSource.getInstance();
    }

    /**
     * Inserts a new arrival log for a vehicle at a station with the current timestamp.
     *
     * @param vehicleId The ID of the vehicle.
     * @param stationId The ID of the station.
     * @throws SQLException If a database access error occurs.
     */
    public void insertArrival(int vehicleId, int stationId) throws SQLException {
        String sql = "INSERT INTO GPSLogs (vehicle_id, station_id, arrival_time) VALUES (?, ?, NOW())";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, vehicleId);
            ps.setInt(2, stationId);
            ps.executeUpdate();
        }
    }
    
    /**
     * Checks if a pending arrival record exists for a vehicle at a specific station
     * (i.e., an arrival with a null departure time).
     *
     * @param vehicleId The ID of the vehicle.
     * @param stationId The ID of the station.
     * @return {@code true} if a pending arrival exists, {@code false} otherwise.
     * @throws SQLException If a database access error occurs.
     */
    public boolean hasPendingArrival(int vehicleId, int stationId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM GPSLogs WHERE vehicle_id=? AND station_id=? AND departure_time IS NULL";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, vehicleId);
            ps.setInt(2, stationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    /**
     * Updates the departure time for the latest pending arrival record of a vehicle
     * at a specific station.
     *
     * @param vehicleId The ID of the vehicle.
     * @param stationId The ID of the station.
     * @return {@code true} if the departure time was successfully updated, {@code false} otherwise.
     * @throws SQLException If a database access error occurs.
     */
    public boolean updateDeparture(int vehicleId, int stationId) throws SQLException {
        if (!hasPendingArrival(vehicleId, stationId)) {
            return false; // No arrival record exists for this station
        }

        String sql = "UPDATE GPSLogs SET departure_time=NOW() " +
                     "WHERE vehicle_id=? AND station_id=? AND departure_time IS NULL";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, vehicleId);
            ps.setInt(2, stationId);
            return ps.executeUpdate() > 0;
        }
    }
    
    /**
     * Fetches a list of detailed GPS logs, including vehicle numbers, station names,
     * arrival times, and departure times. The logs are ordered by vehicle number
     * and arrival time.
     *
     * @return A List of String arrays, where each array contains the vehicle number,
     * station name, arrival time, and departure time.
     * @throws SQLException If a database access error occurs.
     */
    public List<String[]> fetchDetailedLogs() throws SQLException {
        String sql = "SELECT v.vehicle_number, s.station_name, g.arrival_time, g.departure_time " +
                     "FROM GPSLogs g " +
                     "JOIN Vehicles v ON g.vehicle_id = v.vehicle_id " +
                     "JOIN Stations s ON g.station_id = s.station_id " +
                     "ORDER BY v.vehicle_number ASC, g.arrival_time DESC";
        
        List<String[]> logs = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                logs.add(new String[]{
                        rs.getString("vehicle_number"),
                        rs.getString("station_name"),
                        String.valueOf(rs.getTimestamp("arrival_time")),
                        String.valueOf(rs.getTimestamp("departure_time"))
                });
            }
        }
        return logs;
    }
}