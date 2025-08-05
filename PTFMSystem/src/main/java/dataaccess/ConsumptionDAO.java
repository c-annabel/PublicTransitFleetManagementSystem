package dataaccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import transferobjects.ConsumptionRecord;

public class ConsumptionDAO {
    private final DataSource dataSource;

    public ConsumptionDAO() {
        dataSource = DataSource.getInstance();
    }

    public List<ConsumptionRecord> getAllConsumption() throws SQLException {
        List<ConsumptionRecord> records = new ArrayList<>();
        String query = "SELECT v.vehicle_id, v.vehicle_number, v.vehicle_type, " +
                       "c.distance_travelled, c.fuel_used, c.energy_used " +
                       "FROM Vehicles v " +
                       "JOIN ConsumptionLogs c ON v.vehicle_id = c.vehicle_id";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ConsumptionRecord record = new ConsumptionRecord();
                record.setVehicleId(rs.getInt("vehicle_id"));
                record.setVehicleNumber(rs.getString("vehicle_number"));
                record.setVehicleType(rs.getString("vehicle_type"));
                record.setDistance(rs.getDouble("distance_travelled"));
                record.setFuelUsed(rs.getDouble("fuel_used"));
                record.setEnergyUsed(rs.getDouble("energy_used"));
                records.add(record);
            }
        }
        return records;
    }
}
