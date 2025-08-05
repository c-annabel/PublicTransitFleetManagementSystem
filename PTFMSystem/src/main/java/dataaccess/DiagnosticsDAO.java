package dataaccess;

import java.sql.*;
import java.util.*;
import transferobjects.DiagnosticsLog;

public class DiagnosticsDAO {
    private final DataSource dataSource;

    public DiagnosticsDAO() {
        dataSource = DataSource.getInstance();
    }

    private static final String GET_LATEST_DIAGNOSTICS_WITH_TYPE =
        "SELECT d.*, v.vehicle_type FROM DiagnosticsLogs d " +
        "INNER JOIN (SELECT vehicle_id, MAX(log_datetime) AS latest_time FROM DiagnosticsLogs GROUP BY vehicle_id) latest " +
        "ON d.vehicle_id = latest.vehicle_id AND d.log_datetime = latest.latest_time " +
        "INNER JOIN Vehicles v ON d.vehicle_id = v.vehicle_id";

    public List<DiagnosticsLog> getLatestDiagnosticsWithType() throws SQLException {
        List<DiagnosticsLog> logs = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_LATEST_DIAGNOSTICS_WITH_TYPE);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DiagnosticsLog log = new DiagnosticsLog();
                log.setVehicleId(rs.getInt("vehicle_id"));
                log.setVehicleType(rs.getString("vehicle_type")); // New field
                log.setEngineHealth(rs.getBigDecimal("engine_health"));
                log.setCatenaryCondition(rs.getBigDecimal("catenary_condition"));
                log.setPantographCondition(rs.getBigDecimal("pantograph_condition"));
                log.setCircuitBreakerCondition(rs.getBigDecimal("circuit_breaker_condition"));
                log.setLogDatetime(rs.getTimestamp("log_datetime"));
                logs.add(log);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }
}
