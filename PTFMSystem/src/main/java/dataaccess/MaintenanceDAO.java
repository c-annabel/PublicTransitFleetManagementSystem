package dataaccess;

import transferobjects.MaintenanceTask;
import java.sql.*;

public class MaintenanceDAO {

    private final DataSource dataSource;

    public MaintenanceDAO() {
        dataSource = DataSource.getInstance();
    }

    // ✅ Check if a scheduled maintenance task exists for this vehicle
    public boolean hasScheduledTask(int vehicleId) {
        String sql = "SELECT COUNT(*) FROM MaintenanceTasks WHERE vehicle_id = ? AND completed = FALSE";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, vehicleId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Insert a new maintenance task and return its generated ID
    public int insertMaintenanceTask(MaintenanceTask task) {
        String sql = "INSERT INTO MaintenanceTasks (vehicle_id, alert_id, description, scheduled_datetime, cost, completed) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, task.getVehicleId());
            ps.setInt(2, task.getAlertId());
            ps.setString(3, task.getDescription());
            ps.setTimestamp(4, task.getScheduledDatetime());
            ps.setBigDecimal(5, task.getCost());
            ps.setBoolean(6, task.isCompleted());

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
    
        // MaintenanceDAO.java
    public boolean hasScheduledTaskForVehicle(int vehicleId) {
        String sql = "SELECT COUNT(*) FROM MaintenanceTasks WHERE vehicle_id = ? AND completed = FALSE";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vehicleId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ✅ Mark a maintenance task as completed
    public boolean markTaskCompleted(int taskId) {
        String sql = "UPDATE MaintenanceTasks SET completed = TRUE WHERE task_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, taskId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
