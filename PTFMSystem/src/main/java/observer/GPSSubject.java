package observer;

import java.util.ArrayList;
import java.util.List;

public class GPSSubject {
    private List<GPSObserver> observers = new ArrayList<>();
    private String gpsData;

    public void attach(GPSObserver observer) {
        observers.add(observer);
    }

    public void detach(GPSObserver observer) {
        observers.remove(observer);
    }

    public void setGPSData(String gpsData) {
        this.gpsData = gpsData;
        notifyObservers();
    }

    private void notifyObservers() {
        for (GPSObserver observer : observers) {
            observer.update(gpsData);
        }
    }
}
