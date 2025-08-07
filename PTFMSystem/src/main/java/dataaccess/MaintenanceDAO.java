package dataaccess;

import transferobjects.MaintenanceTask;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing MaintenanceTask objects in the database.
 * This class handles all database interactions related to maintenance tasks.
 * 
 * @author Annabel Cheng
 * @comment Course 25S CST8288 Lab013 Final Project
 */
public class MaintenanceDAO {

    /**
     * The DataSource instance used to get database connections.
     */
    private final DataSource dataSource;

    /**
     * Constructs a MaintenanceDAO and initializes the DataSource instance.
     */
    public MaintenanceDAO() {
        dataSource = DataSource.getInstance();
    }

    /**
     * Checks if a vehicle already has a scheduled (incomplete) maintenance task.
     *
     * @param vehicleId The ID of the vehicle to check.
     * @return true if an incomplete task exists for the vehicle; false otherwise.
     */
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

    /**
     * Inserts a new maintenance task into the database and returns the generated task ID.
     *
     * @param task The MaintenanceTask object to insert.
     * @return The generated task ID if successful; -1 otherwise.
     */
    public int insertMaintenanceTask(MaintenanceTask task) {
        String sql = "INSERT INTO MaintenanceTasks (vehicle_id, alert_id, description, scheduled_datetime, cost, completed) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, task.getVehicleId());
            if (task.getAlertId() > 0) {
                ps.setInt(2, task.getAlertId());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
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

    /**
     * Checks if a vehicle has any scheduled maintenance tasks that are not completed.
     *
     * @param vehicleId The vehicle ID to check.
     * @return true if there is an incomplete scheduled task; false otherwise.
     */
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

    /**
     * Checks if a specific date is already booked for a specific vehicle.
     *
     * @param vehicleId The ID of the vehicle.
     * @param selectedDate The date to check.
     * @return true if the date is already booked for that vehicle; false otherwise.
     */
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

    /**
     * Checks if a given date is booked by any vehicle.
     *
     * @param selectedDate The date to check.
     * @return true if the date is already taken; false otherwise.
     */
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

    /**
     * Retrieves a list of all distinct booked maintenance dates.
     *
     * @return A list of booked LocalDate values.
     */
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

    /**
     * Retrieves all booked dates for a specific vehicle.
     *
     * @param vehicleId The vehicle ID.
     * @return A list of LocalDate values the vehicle is scheduled for maintenance.
     */
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

    /**
     * Updates an existing maintenance task.
     *
     * @param task The MaintenanceTask object containing updated values.
     * @return true if the update was successful; false otherwise.
     */
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

    /**
     * Deletes a maintenance task by its ID.
     *
     * @param taskId The ID of the task to delete.
     * @return true if the task was successfully deleted; false otherwise.
     */
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

    /**
     * Retrieves all maintenance tasks ordered by scheduled date.
     *
     * @return A list of MaintenanceTask objects.
     */
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

    /**
     * Retrieves a specific maintenance task by its ID.
     *
     * @param taskId The ID of the task.
     * @return The corresponding MaintenanceTask object; or null if not found.
     */
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

    /**
     * Checks if the selected date is already booked for the given vehicle,
     * excluding the task with the specified ID.
     *
     * @param vehicleId The vehicle ID.
     * @param date The date to check.
     * @param taskId The task ID to exclude from the check.
     * @return true if the date is booked by the same vehicle excluding the given task; false otherwise.
     */
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
