package dataaccess;

import transferobjects.Alert;
import java.sql.*;

/**
 * Data Access Object (DAO) for managing Alert objects in the database.
 * This class handles all database interactions related to alerts, such as
 * retrieving, inserting, and updating alert information.
 *
 * @author Annabel Cheng
 * @comment CST8288 Lab 013 Final Project
 */
public class AlertDAO {

    /**
     * The DataSource instance used to get database connections.
     */
    private final DataSource dataSource;

    /**
     * Constructs a new AlertDAO and initializes the DataSource instance.
     */
    public AlertDAO() {
        dataSource = DataSource.getInstance();
    }

    /**
     * Retrieves the ID of an existing, unresolved maintenance alert for a specific vehicle.
     *
     * @param vehicleId The ID of the vehicle to check for an alert.
     * @return The alert ID if an unresolved maintenance alert exists, otherwise -1.
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
     * Inserts a new alert into the database.
     *
     * @param alert The Alert object containing the data to be inserted.
     * @return The auto-generated ID of the newly inserted alert, or -1 if the insertion failed.
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
     * Marks an alert as resolved in the database.
     *
     * @param alertId The ID of the alert to be resolved.
     * @return {@code true} if the alert was successfully marked as resolved, {@code false} otherwise.
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