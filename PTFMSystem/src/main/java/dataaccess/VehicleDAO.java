package dataaccess;

import transferobjects.Vehicle;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {
    private final DataSource dataSource;

    public VehicleDAO() {
        dataSource = DataSource.getInstance();
    }

    public void addVehicle(Vehicle vehicle) throws SQLException {
        String sql = "INSERT INTO Vehicles (vehicle_number, vehicle_type, fuel_type, consumption_rate, max_passengers, route_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, vehicle.getVehicleNumber());
            ps.setString(2, vehicle.getVehicleType());
            ps.setString(3, vehicle.getFuelType());
            ps.setDouble(4, vehicle.getConsumptionRate());
            ps.setInt(5, vehicle.getMaxPassengers());
            ps.setInt(6, vehicle.getRouteId());
            ps.executeUpdate();
        }
    }
    
    
    public void updateVehicle(Vehicle vehicle) throws SQLException {
    String sql = "UPDATE Vehicles SET vehicle_number=?, vehicle_type=?, fuel_type=?, consumption_rate=?, max_passengers=?, route_id=? WHERE vehicle_id=?";
    try (Connection con = dataSource.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, vehicle.getVehicleNumber());
        ps.setString(2, vehicle.getVehicleType());
        ps.setString(3, vehicle.getFuelType());
        ps.setDouble(4, vehicle.getConsumptionRate());
        ps.setInt(5, vehicle.getMaxPassengers());
        ps.setInt(6, vehicle.getRouteId());
        ps.setInt(7, vehicle.getVehicleId());
        ps.executeUpdate();
    }
}

    public void deleteVehicle(int vehicleId) throws SQLException {
        String sql = "DELETE FROM Vehicles WHERE vehicle_id=?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, vehicleId);
            ps.executeUpdate();
        }
    }

    public List<Vehicle> getAllVehicles() throws SQLException {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT * FROM Vehicles";
        try (Connection con = dataSource.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Vehicle v = new Vehicle();
                v.setVehicleId(rs.getInt("vehicle_id"));
                v.setVehicleNumber(rs.getString("vehicle_number"));
                v.setVehicleType(rs.getString("vehicle_type"));
                v.setFuelType(rs.getString("fuel_type"));
                v.setConsumptionRate(rs.getDouble("consumption_rate"));
                v.setMaxPassengers(rs.getInt("max_passengers"));
                v.setRouteId(rs.getInt("route_id"));
                list.add(v);
            }
        }
        return list;
    }
}
