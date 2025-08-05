package dataaccess;

import transferobjects.BreakLog;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BreakLogDAO {
    private final DataSource dataSource;

    public BreakLogDAO() {
        dataSource = DataSource.getInstance();
    }

    public void startBreak(BreakLog log) throws SQLException {
        String sql = "INSERT INTO BreakLogs (operator_id, vehicle_id, status, start_time) VALUES (?, ?, 'Started', NOW())";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, log.getOperatorId());
            ps.setInt(2, log.getVehicleId());
            ps.executeUpdate();
        }
    }
    
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

    public void pauseBreak(int breakId) throws SQLException {
        String sql = "UPDATE BreakLogs SET status='Paused' WHERE break_id=?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, breakId);
            ps.executeUpdate();
        }
    }

    public void endBreak(int breakId) throws SQLException {
        String sql = "UPDATE BreakLogs SET status='Ended', end_time=NOW() WHERE break_id=?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, breakId);
            ps.executeUpdate();
        }
    }

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
