package dataaccess;

import transferobjects.Alert;
import java.sql.*;

public class AlertDAO {

    private final DataSource dataSource;

    public AlertDAO() {
        dataSource = DataSource.getInstance();
    }

    // ✅ Check if an unresolved maintenance alert exists for this vehicle
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

    // ✅ Insert a new maintenance alert and return its ID
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

    // ✅ Mark alert as resolved after booking
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
