package dataaccess;

import transferobjects.Alert;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlertDAO {

    private final DataSource dataSource;

    public AlertDAO() {
        dataSource = DataSource.getInstance();
    }

    // SQL Queries
    private static final String CHECK_EXISTING_ALERT =
        "SELECT alert_id FROM Alerts WHERE vehicle_id=? AND alert_type='Maintenance' AND resolved=FALSE";

    private static final String INSERT_ALERT =
        "INSERT INTO Alerts (vehicle_id, alert_type, alert_message, severity) VALUES (?,?,?,?)";

    private static final String GET_UNRESOLVED_ALERTS =
        "SELECT * FROM Alerts WHERE alert_type='Maintenance' AND resolved=FALSE";

    private static final String RESOLVE_ALERT =
        "UPDATE Alerts SET resolved=TRUE WHERE alert_id=?";

    /**
     * Check if unresolved maintenance alert exists for a vehicle.
     * @param vehicleId Vehicle ID
     * @return Alert ID if exists, -1 otherwise
     */
    public int getExistingAlertId(int vehicleId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(CHECK_EXISTING_ALERT)) {
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
     * Insert new alert and return generated alert ID.
     * @param alert Alert object
     * @return new Alert ID or -1 if failed
     */
    public int insertAlert(Alert alert) {
        int newId = -1;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_ALERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, alert.getVehicleId());
            ps.setString(2, alert.getAlertType());
            ps.setString(3, alert.getAlertMessage());
            ps.setString(4, alert.getSeverity());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    newId = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newId;
    }

    /**
     * Fetch all unresolved maintenance alerts.
     * @return List of Alert objects
     */
    public List<Alert> getUnresolvedMaintenanceAlerts() {
        List<Alert> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_UNRESOLVED_ALERTS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Alert alert = new Alert();
                alert.setAlertId(rs.getInt("alert_id"));
                alert.setVehicleId(rs.getInt("vehicle_id"));
                alert.setAlertType(rs.getString("alert_type"));
                alert.setAlertMessage(rs.getString("alert_message"));
                alert.setSeverity(rs.getString("severity"));
                alert.setResolved(rs.getBoolean("resolved"));
                list.add(alert);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Mark an alert as resolved.
     * @param alertId Alert ID to update
     * @return true if updated, false otherwise
     */
    public boolean resolveAlert(int alertId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(RESOLVE_ALERT)) {
            ps.setInt(1, alertId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
