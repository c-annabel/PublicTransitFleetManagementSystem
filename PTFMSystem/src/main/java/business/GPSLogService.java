package business;

import dataaccess.GPSLogDAO;
import java.util.List;

/**
 * Service class that handles GPS logging operations such as
 * logging arrivals, departures, and retrieving detailed logs.
 * Delegates operations to {@link GPSLogDAO}.
 * 
 * @author Annabel Cheng
 * @version Course 25S CST8288 Lab013 Final Project
 */
public class GPSLogService {

    private final GPSLogDAO dao = new GPSLogDAO();

    /**
     * Logs the arrival of a vehicle at a station.
     *
     * @param vehicleId the ID of the arriving vehicle
     * @param stationId the ID of the station
     * @throws Exception if a database access error occurs
     */
    public void logArrival(int vehicleId, int stationId) throws Exception {
        dao.insertArrival(vehicleId, stationId);
    }

    /**
     * Logs the departure of a vehicle from a station.
     *
     * @param vehicleId the ID of the departing vehicle
     * @param stationId the ID of the station
     * @throws Exception if a database access error occurs
     */
    public void logDeparture(int vehicleId, int stationId) throws Exception {
        dao.updateDeparture(vehicleId, stationId);
    }

    /**
     * Retrieves detailed GPS logs including arrival and departure data.
     *
     * @return a list of string arrays representing detailed log entries
     * @throws Exception if a database access error occurs
     */
    public List<String[]> getDetailedLogs() throws Exception {
        return dao.fetchDetailedLogs();
    }
}
