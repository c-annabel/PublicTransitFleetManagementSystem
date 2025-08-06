package dataaccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {
    private final DataSource dataSource;

    public ReportDAO() {
        dataSource = DataSource.getInstance();
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // ================= Maintenance Summary =================
    public double[] getMaintenanceSummary(String startDate, String endDate) throws SQLException {
        if (startDate == null || startDate.isEmpty()) startDate = "1970-01-01";
        if (endDate == null || endDate.isEmpty()) endDate = "2099-12-31";

        String sql = "SELECT " +
                     "SUM(CASE WHEN completed = TRUE THEN 1 ELSE 0 END) AS completed, " +
                     "SUM(CASE WHEN completed = FALSE THEN 1 ELSE 0 END) AS pending " +
                     "FROM MaintenanceTasks WHERE scheduled_datetime BETWEEN ? AND ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new double[]{rs.getDouble("completed"), rs.getDouble("pending")};
            }
        }
        return new double[]{0, 0};
    }

    // ================= Maintenance Cost Trend =================
    public String[] getMaintenanceCostLabels(String startDate, String endDate) throws SQLException {
        if (startDate == null || startDate.isEmpty()) startDate = "1970-01-01";
        if (endDate == null || endDate.isEmpty()) endDate = "2099-12-31";

        List<String> labels = new ArrayList<>();
        String sql = "SELECT DATE(scheduled_datetime) AS date FROM MaintenanceTasks " +
                     "WHERE scheduled_datetime BETWEEN ? AND ? GROUP BY DATE(scheduled_datetime) ORDER BY DATE(scheduled_datetime)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                labels.add(rs.getString("date"));
            }
        }
        return labels.toArray(new String[0]);
    }

    public double[] getMaintenanceCostValues(String startDate, String endDate) throws SQLException {
        if (startDate == null || startDate.isEmpty()) startDate = "1970-01-01";
        if (endDate == null || endDate.isEmpty()) endDate = "2099-12-31";

        List<Double> values = new ArrayList<>();
        String sql = "SELECT SUM(cost) AS total FROM MaintenanceTasks " +
                     "WHERE scheduled_datetime BETWEEN ? AND ? GROUP BY DATE(scheduled_datetime) ORDER BY DATE(scheduled_datetime)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                values.add(rs.getDouble("total"));
            }
        }
        return values.stream().mapToDouble(Double::doubleValue).toArray();
    }

    // ================= Cost Analysis =================
    public String[] getCostAnalysisLabels() {
        return new String[]{"Fuel", "Maintenance"};
    }

    public double[] getCostAnalysisValues(String startDate, String endDate) throws SQLException {
        if (startDate == null || startDate.isEmpty()) startDate = "1970-01-01";
        if (endDate == null || endDate.isEmpty()) endDate = "2099-12-31";

        double fuelCost = 0.0, maintenanceCost = 0.0;

        // Fuel cost calculation
        String sqlFuel = "SELECT SUM(c.fuel_used * p.price_per_unit) AS totalFuel FROM ConsumptionLogs c " +
                         "JOIN Vehicles v ON c.vehicle_id = v.vehicle_id " +
                         "JOIN PriceConfig p ON v.fuel_type = p.fuel_type " +
                         "WHERE c.log_datetime BETWEEN ? AND ? " +
                         "AND p.effective_date = (" +
                         " SELECT MAX(effective_date) FROM PriceConfig WHERE fuel_type = v.fuel_type AND effective_date <= c.log_datetime)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlFuel)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                fuelCost = rs.getDouble("totalFuel");
            }
        }

        // Maintenance cost calculation
        String sqlMaint = "SELECT SUM(cost) AS totalMaint FROM MaintenanceTasks WHERE scheduled_datetime BETWEEN ? AND ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlMaint)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                maintenanceCost = rs.getDouble("totalMaint");
            }
        }

        return new double[]{fuelCost, maintenanceCost};
    }

    // ================= Operator Performance =================
    public double getOnTimeRate(int operatorId) throws SQLException {
        String sql = "SELECT (COUNT(CASE WHEN g.arrival_time <= t.planned_arrival_time THEN 1 END) * 100.0 / COUNT(*)) AS rate " +
                     "FROM GPSLogs g " +
                     "JOIN TripSchedules t ON g.station_id = t.station_id AND DATE(g.arrival_time) = DATE(t.planned_arrival_time) " +
                     "JOIN OperatorAssignments oa ON g.vehicle_id = oa.vehicle_id " +
                     "WHERE oa.operator_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, operatorId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("rate");
            }
        }
        return 0.0;
    }

    public double getEfficiencyScore(int operatorId) throws SQLException {
        String sql = "SELECT AVG(percentage) AS efficiency FROM ( " +
                " SELECT CASE WHEN planned_count > 0 THEN (actual_count * 100.0 / planned_count) ELSE 0 END AS percentage " +
                " FROM ( " +
                "   SELECT oa.assignment_id, " +
                "     (SELECT COUNT(*) FROM TripSchedules ts " +
                "      JOIN Vehicles v ON ts.route_id = v.route_id " +
                "      WHERE v.vehicle_id = oa.vehicle_id " +
                "        AND DATE(ts.planned_arrival_time) = DATE(oa.start_time)) AS planned_count, " +
                "     (SELECT COUNT(*) FROM GPSLogs g " +
                "      WHERE g.vehicle_id = oa.vehicle_id " +
                "        AND g.arrival_time BETWEEN oa.start_time AND oa.end_time) AS actual_count " +
                "   FROM OperatorAssignments oa WHERE oa.operator_id = ? " +
                " ) sub " +
                ") avg_table";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, operatorId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("efficiency");
            }
        }
        return 0.0;
    }
}
