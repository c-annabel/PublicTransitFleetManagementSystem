package dataaccess;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class VehicleConfigDAO {
    private final DataSource dataSource;

    public VehicleConfigDAO() {
        dataSource = DataSource.getInstance();
    }

    public Map<String, Double> getThresholds() throws SQLException {
        Map<String, Double> thresholds = new HashMap<>();
        String query = "SELECT vehicle_type, threshold FROM VehicleTypeConfig";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                thresholds.put(rs.getString("vehicle_type"), rs.getDouble("threshold"));
            }
        }
        return thresholds;
    }
}
