package observer;

public class ManagerDashboard implements GPSObserver {
    @Override
    public void update(String gpsData) {
        System.out.println("Manager Dashboard updated with GPS Data: " + gpsData);
    }
}
