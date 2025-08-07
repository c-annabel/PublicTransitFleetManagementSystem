package transferobjects;

import java.sql.Timestamp;

/**
 * GPSLog.java - Transfer Object for logging GPS events of transit vehicles.
 *
 * This class is part of the CST8288 Final Project.
 * It holds vehicle arrival and departure times at stations for route tracking and analytics.
 *
 * Utilized in both data logging and performance evaluation.
 * 
 * @author Annabel Cheng
 */

public class GPSLog {
    private int gpsId;
    private int vehicleId;
    private int stationId;
    private Timestamp arrivalTime;
    private Timestamp departureTime;

    public int getGpsId() {
        return gpsId;
    }

    public void setGpsId(int gpsId) {
        this.gpsId = gpsId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public Timestamp getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Timestamp arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Timestamp getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Timestamp departureTime) {
        this.departureTime = departureTime;
    }
}
