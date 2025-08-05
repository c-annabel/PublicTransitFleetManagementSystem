package business;

import dataaccess.GPSLogDAO;
import java.util.List;

public class GPSLogService {
    private final GPSLogDAO dao = new GPSLogDAO();

    public void logArrival(int vehicleId, int stationId) throws Exception {
        dao.insertArrival(vehicleId, stationId);
    }

    public void logDeparture(int vehicleId, int stationId) throws Exception {
        dao.updateDeparture(vehicleId, stationId);
    }

    public List<String[]> getDetailedLogs() throws Exception {
        return dao.fetchDetailedLogs();
    }
}
