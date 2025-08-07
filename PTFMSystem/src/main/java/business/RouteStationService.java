package business;

import dataaccess.StationDAO;
import java.util.List;

/**
 * Service class responsible for retrieving station-related data for route management.
 * It delegates the data access operations to {@link StationDAO}.
 * 
 * @author Annabel Cheng
 * Course 25S CST8288 Lab013 Final Project
 */
public class RouteStationService {
    private final StationDAO stationDAO = new StationDAO();

    /**
     * Retrieves a list of station IDs and their corresponding names.
     *
     * @return A list of String arrays, where each array contains station ID and station name.
     * @throws Exception if there is an error while accessing the data.
     */
    public List<String[]> getStationIdAndName() throws Exception {
        return stationDAO.getStationIdAndName();
    }
}
