package dataaccess;

import transferobjects.MaintenanceTask;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
            // ✅ Handle nullable alertId
        if (task.getAlertId() > 0) {
            ps.setInt(2, task.getAlertId());
        } else {
            ps.setNull(2, java.sql.Types.INTEGER); // <-- THIS IS IMPORTANT
        }
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
    
        public boolean isDateAlreadyBooked(int vehicleId, LocalDate selectedDate) {
        String sql = "SELECT COUNT(*) FROM MaintenanceTasks WHERE vehicle_id = ? AND DATE(scheduled_datetime) = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, vehicleId);
            ps.setDate(2, java.sql.Date.valueOf(selectedDate));
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
        
        public boolean isDateAlreadyTaken(LocalDate selectedDate) {
    String sql = "SELECT COUNT(*) FROM MaintenanceTasks WHERE DATE(scheduled_datetime) = ?";
    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setDate(1, java.sql.Date.valueOf(selectedDate));

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
        
        

        public List<LocalDate> getAllBookedDates() {
        List<LocalDate> dates = new ArrayList<>();
        String sql = "SELECT DISTINCT DATE(scheduled_datetime) as booked_date FROM MaintenanceTasks";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                dates.add(rs.getDate("booked_date").toLocalDate());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dates;
        
    }
        
    public List<LocalDate> getBookedDatesForVehicle(int vehicleId) {
    List<LocalDate> dates = new ArrayList<>();
    String sql = "SELECT DISTINCT DATE(scheduled_datetime) as booked_date FROM MaintenanceTasks WHERE vehicle_id = ?";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, vehicleId);

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                dates.add(rs.getDate("booked_date").toLocalDate());
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return dates;
}


    public boolean updateTask(MaintenanceTask task) {
    String sql = "UPDATE MaintenanceTasks SET scheduled_datetime = ?, cost = ?, completed = ? WHERE task_id = ?";
    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setTimestamp(1, task.getScheduledDatetime());
        ps.setBigDecimal(2, task.getCost());
        ps.setBoolean(3, task.isCompleted());
        ps.setInt(4, task.getTaskId());

        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

    public boolean deleteTask(int taskId) {
    String sql = "DELETE FROM MaintenanceTasks WHERE task_id = ?";
    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, taskId);
        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
    
    public List<MaintenanceTask> getAllMaintenanceTasks() {
    List<MaintenanceTask> tasks = new ArrayList<>();

    String sql = "SELECT * FROM MaintenanceTasks ORDER BY scheduled_datetime ASC";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            MaintenanceTask task = new MaintenanceTask();
            task.setTaskId(rs.getInt("task_id"));
            task.setVehicleId(rs.getInt("vehicle_id"));
            task.setAlertId(rs.getInt("alert_id"));
            task.setDescription(rs.getString("description"));
            task.setScheduledDatetime(rs.getTimestamp("scheduled_datetime"));
            task.setCost(rs.getBigDecimal("cost"));
            task.setCompleted(rs.getBoolean("completed"));

            tasks.add(task);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return tasks;
}
    
    public MaintenanceTask getTaskById(int taskId) {
    String sql = "SELECT * FROM MaintenanceTasks WHERE task_id = ?";
    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, taskId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                MaintenanceTask task = new MaintenanceTask();
                task.setTaskId(taskId);
                task.setVehicleId(rs.getInt("vehicle_id"));
                task.setAlertId(rs.getInt("alert_id"));
                task.setDescription(rs.getString("description"));
                task.setScheduledDatetime(rs.getTimestamp("scheduled_datetime"));
                task.setCost(rs.getBigDecimal("cost"));
                task.setCompleted(rs.getBoolean("completed"));
                return task;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
public boolean isDateAlreadyBookedExcludingTask(int vehicleId, LocalDate date, int taskId) {
    String sql = "SELECT COUNT(*) FROM MaintenanceTasks WHERE vehicle_id = ? AND DATE(scheduled_datetime) = ? AND task_id <> ?";
    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, vehicleId);
        ps.setDate(2, java.sql.Date.valueOf(date));
        ps.setInt(3, taskId);
        try (ResultSet rs = ps.executeQuery()) {
            return rs.next() && rs.getInt(1) > 0;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}
    

}
