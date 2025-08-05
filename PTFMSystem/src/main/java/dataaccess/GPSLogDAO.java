package dataaccess;

import transferobjects.GPSLog;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GPSLogDAO {
    private final DataSource dataSource;

    public GPSLogDAO() {
        dataSource = DataSource.getInstance();
    }

    public void insertArrival(int vehicleId, int stationId) throws SQLException {
        String sql = "INSERT INTO GPSLogs (vehicle_id, station_id, arrival_time) VALUES (?, ?, NOW())";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, vehicleId);
            ps.setInt(2, stationId);
            ps.executeUpdate();
        }
    }
    
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
     * Update departure time for the latest pending arrival record.
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
