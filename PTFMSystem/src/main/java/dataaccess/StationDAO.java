package dataaccess;

import transferobjects.Station;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for handling operations related to Stations.
 * This class provides methods to retrieve station data from the database.
 * 
 * @author Annabel Cheng
 * @comment Course 25S CST8288 Lab013 Final Project
 */
public class StationDAO {
    private final DataSource dataSource;

    /**
     * Constructs a StationDAO and initializes the DataSource instance.
     */
    public StationDAO() {
        dataSource = DataSource.getInstance();
    }

    /**
     * Retrieves a list of all stations containing only their ID and name.
     *
     * @return A list of string arrays, where each array contains the station ID and station name.
     * @throws SQLException if a database access error occurs.
     */
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

    /**
     * Retrieves a list of all stations with full details (ID, name, location).
     *
     * @return A list of Station objects containing complete station information.
     * @throws SQLException if a database access error occurs.
     */
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
