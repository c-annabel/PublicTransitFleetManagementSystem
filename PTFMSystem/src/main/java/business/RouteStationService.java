package business;

import dataaccess.StationDAO;
import java.util.List;

public class RouteStationService {
    private final StationDAO stationDAO = new StationDAO();

    public List<String[]> getStationIdAndName() throws Exception {
        return stationDAO.getStationIdAndName();
    }
}
