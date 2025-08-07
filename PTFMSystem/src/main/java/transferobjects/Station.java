package transferobjects;

/**
 * Represents a transit station in the PTFMS system.
 * 
 * This class encapsulates the data for a station, including its ID, name, 
 * and location. It is used across the system for mapping routes, 
 * GPS logging, and reporting.
 *
 * @Author: Annabel Cheng
 * Course: 25S CST8288 Section 013 Final Project
 */
public class Station {

    /** The unique identifier for the station. */
    private int stationId;

    /** The name of the station. */
    private String stationName;

    /** The physical or geographic location of the station. */
    private String location;

    /**
     * Returns the station ID.
     * @return the stationId
     */
    public int getStationId() {
        return stationId;
    }

    /**
     * Sets the station ID.
     * @param stationId the unique ID to set
     */
    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    /**
     * Returns the name of the station.
     * @return the stationName
     */
    public String getStationName() {
        return stationName;
    }

    /**
     * Sets the name of the station.
     * @param stationName the station name to set
     */
    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    /**
     * Returns the station's location.
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the station's location.
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }
}
