package dataaccess;

import transferobjects.Alert;
import java.sql.*;

/**
 * Data Access Object (DAO) for managing {@code Alert} objects in the database.
 * This class handles all database interactions related to alerts, including
 * retrieving unresolved alerts, inserting new alerts, and resolving existing ones.
 * 
 * It uses a singleton {@code DataSource} for obtaining database connections.
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
public class AlertDAO {

    private final DataSource dataSource;

    /**
     * Constructs a new {@code AlertDAO} and initializes the {@code DataSource} instance.
     */
    public AlertDAO() {
        dataSource = DataSource.getInstance();
    }

    /**
     * Retrieves the ID of an existing unresolved "Maintenance" alert for a specific vehicle.
     *
     * @param vehicleId the ID of the vehicle to check
     * @return the alert ID if an unresolved alert exists; otherwise, -1
     */
    public int getExistingAlertId(int vehicleId) {
        String sql = "SELECT alert_id FROM Alerts WHERE vehicle_id=? AND alert_type='Maintenance' AND resolved=FALSE";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, vehicleId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("alert_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Inserts a new alert record into the Alerts table.
     *
     * @param alert the {@code Alert} object containing alert data
     * @return the auto-generated alert ID if the insert is successful; otherwise, -1
     */
    public int insertAlert(Alert alert) {
        String sql = "INSERT INTO Alerts (vehicle_id, alert_type, alert_message, severity) VALUES (?,?,?,?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, alert.getVehicleId());
            ps.setString(2, alert.getAlertType());
            ps.setString(3, alert.getAlertMessage());
            ps.setString(4, alert.getSeverity());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Marks a specific alert as resolved in the database.
     *
     * @param alertId the ID of the alert to be marked as resolved
     * @return {@code true} if the update was successful; {@code false} otherwise
     */
    public boolean resolveAlert(int alertId) {
        String sql = "UPDATE Alerts SET resolved = TRUE WHERE alert_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, alertId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
