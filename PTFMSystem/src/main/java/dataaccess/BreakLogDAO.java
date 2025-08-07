package dataaccess;

import transferobjects.BreakLog;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing BreakLog objects in the database.
 * This class handles all database interactions related to operator break logs.
 *
 * @author Annabel Cheng
 * @comment CST8288 Lab 013 Final Project
 */
public class BreakLogDAO {
    /**
     * The DataSource instance used to get database connections.
     */
    private final DataSource dataSource;

    /**
     * Constructs a new BreakLogDAO and initializes the DataSource instance.
     */
    public BreakLogDAO() {
        dataSource = DataSource.getInstance();
    }

    /**
     * Inserts a new break log into the database with a 'Started' status and the
     * current timestamp for the start time.
     *
     * @param log The BreakLog object containing the operator and vehicle IDs.
     * @throws SQLException If a database access error occurs.
     */
    public void startBreak(BreakLog log) throws SQLException {
        String sql = "INSERT INTO BreakLogs (operator_id, vehicle_id, status, start_time) VALUES (?, ?, 'Started', NOW())";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, log.getOperatorId());
            ps.setInt(2, log.getVehicleId());
            ps.executeUpdate();
        }
    }
    
    /**
     * Checks if a break log with the given ID exists in the database.
     *
     * @param breakId The ID of the break log to check.
     * @return {@code true} if a break log with the specified ID exists, {@code false} otherwise.
     * @throws SQLException If a database access error occurs.
     */
    public boolean breakExists(int breakId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM BreakLogs WHERE break_id=?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, breakId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * Updates the status of a break log to 'Paused'.
     *
     * @param breakId The ID of the break log to be paused.
     * @throws SQLException If a database access error occurs.
     */
    public void pauseBreak(int breakId) throws SQLException {
        String sql = "UPDATE BreakLogs SET status='Paused' WHERE break_id=?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, breakId);
            ps.executeUpdate();
        }
    }

    /**
     * Updates the status of a break log to 'Ended' and sets the end time to the
     * current timestamp.
     *
     * @param breakId The ID of the break log to be ended.
     * @throws SQLException If a database access error occurs.
     */
    public void endBreak(int breakId) throws SQLException {
        String sql = "UPDATE BreakLogs SET status='Ended', end_time=NOW() WHERE break_id=?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, breakId);
            ps.executeUpdate();
        }
    }

    /**
     * Retrieves a list of all break logs for a specific operator, ordered by
     * start time in descending order.
     *
     * @param operatorId The ID of the operator.
     * @return A List of BreakLog objects associated with the given operator.
     * @throws SQLException If a database access error occurs.
     */
    public List<BreakLog> getBreakLogsByOperator(int operatorId) throws SQLException {
        List<BreakLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM BreakLogs WHERE operator_id=? ORDER BY start_time DESC";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, operatorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BreakLog log = new BreakLog();
                    log.setBreakId(rs.getInt("break_id"));
                    log.setOperatorId(rs.getInt("operator_id"));
                    log.setVehicleId(rs.getInt("vehicle_id"));
                    log.setStartTime(rs.getTimestamp("start_time"));
                    log.setEndTime(rs.getTimestamp("end_time"));
                    log.setStatus(rs.getString("status"));
                    logs.add(log);
                }
            }
        }
        return logs;
    }
}