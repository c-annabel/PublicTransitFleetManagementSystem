// === DiagnosticsDAO.java ===
package dataaccess;

import transferobjects.DiagnosticsLog;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiagnosticsDAO {
        private final DataSource dataSource;

    public DiagnosticsDAO() {
        dataSource = DataSource.getInstance();
    }
    
    public List<DiagnosticsLog> getLatestDiagnosticsWithUsage() throws SQLException {
        List<DiagnosticsLog> diagnosticsList = new ArrayList<>();

        String query = """
            SELECT d.vehicle_id, d.engine_health, d.catenary_condition, d.pantograph_condition, 
                   d.circuit_breaker_condition, d.log_datetime, v.vehicle_type,
                   u.hours_used, u.brake_condition, u.tire_condition, u.axle_condition
            FROM DiagnosticsLogs d
            JOIN Vehicles v ON d.vehicle_id = v.vehicle_id
            LEFT JOIN UsageLogs u ON d.vehicle_id = u.vehicle_id
            WHERE (d.vehicle_id, d.log_datetime) IN (
                SELECT vehicle_id, MAX(log_datetime)
                FROM DiagnosticsLogs
                GROUP BY vehicle_id
            )
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                DiagnosticsLog log = new DiagnosticsLog();

                log.setVehicleId(rs.getInt("vehicle_id"));
                log.setEngineHealth(rs.getBigDecimal("engine_health"));
                log.setCatenaryCondition(rs.getBigDecimal("catenary_condition"));
                log.setPantographCondition(rs.getBigDecimal("pantograph_condition"));
                log.setCircuitBreakerCondition(rs.getBigDecimal("circuit_breaker_condition"));
                log.setLogDatetime(rs.getTimestamp("log_datetime"));
                log.setVehicleType(rs.getString("vehicle_type"));

                log.setHoursUsed(rs.getBigDecimal("hours_used"));
                log.setBrakeCondition(rs.getBigDecimal("brake_condition"));
                log.setTireCondition(rs.getBigDecimal("tire_condition"));
                log.setAxleCondition(rs.getBigDecimal("axle_condition"));

                diagnosticsList.add(log);
            }
        }

        return diagnosticsList;
    }
}