package dataaccess;

import transferobjects.Station;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StationDAO {
    private final DataSource dataSource;

    public StationDAO() {
        dataSource = DataSource.getInstance();
    }

    // Fetch all stations (id + name)
    public List<String[]> getStationIdAndName() throws SQLException {
        String sql = "SELECT station_id, station_name FROM Stations";
        List<String[]> list = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new String[]{
                    String.valueOf(rs.getInt("station_id")),
                    rs.getString("station_name")
                });
            }
        }
        return list;
    }

    // (Optional) Get full Station list
    public List<Station> getAllStations() throws SQLException {
        String sql = "SELECT * FROM Stations";
        List<Station> stations = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Station s = new Station();
                s.setStationId(rs.getInt("station_id"));
                s.setStationName(rs.getString("station_name"));
                s.setLocation(rs.getString("location"));
                stations.add(s);
            }
        }
        return stations;
    }
}
