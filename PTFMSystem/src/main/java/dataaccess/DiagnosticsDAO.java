// === DiagnosticsDAO.java ===
package dataaccess;

import transferobjects.DiagnosticsLog;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing DiagnosticsLog objects.
 * This class provides methods to retrieve diagnostic information from the database,
 * often joined with usage data.
 *
 * @author Annabel Cheng
 * @comment CST8288 Lab 013 Final Project
 */
public class DiagnosticsDAO {
    /**
     * The DataSource instance used to get database connections.
     */
    private final DataSource dataSource;

    /**
     * Constructs a new DiagnosticsDAO and initializes the DataSource instance.
     */
    public DiagnosticsDAO() {
        dataSource = DataSource.getInstance();
    }
    
    /**
     * Retrieves a list of the latest diagnostics logs for each vehicle,
     * including associated usage data.
     *
     * @return A list of DiagnosticsLog objects, each representing the latest
     * diagnostics and usage data for a vehicle.
     * @throws SQLException If a database access error occurs.
     */
    public List<DiagnosticsLog> getLatestDiagnosticsWithUsage() throws SQLException {
        List<DiagnosticsLog> diagnosticsList = new ArrayList<>();

        String query = """
            SELECT d.vehicle_id, d.engine_health, d.catenary_condition, d.pantograph_condition, 
                       d.circuit_breaker_condition, d.log_datetime, v.vehicle_type,
                       u.hours_used, u.brake_condition, u.tire_condition, u.axle_condition
                FROM (
                    SELECT *
                    FROM DiagnosticsLogs
                    WHERE (vehicle_id, log_datetime) IN (
                        SELECT vehicle_id, MAX(log_datetime)
                        FROM DiagnosticsLogs
                        GROUP BY vehicle_id
                    )
                ) d
                JOIN Vehicles v ON d.vehicle_id = v.vehicle_id
                LEFT JOIN (
                    SELECT *
                    FROM UsageLogs
                    WHERE (vehicle_id, log_datetime) IN (
                        SELECT vehicle_id, MAX(log_datetime)
                        FROM UsageLogs
                        GROUP BY vehicle_id
                    )
                ) u ON d.vehicle_id = u.vehicle_id
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