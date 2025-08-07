package transferobjects;

import java.sql.Timestamp;

/**
 * Represents a break log record for an operator in the PTFMS system.
 *
 * This class stores information about operator breaks including
 * the start and end times, associated vehicle, and status.
 *
 * @Author: Annabel Cheng
 * Course: 25S CST8288 Section 013 Final Project
 */
public class BreakLog {

    /** Unique identifier for the break log entry. */
    private int breakId;

    /** ID of the operator taking the break. */
    private int operatorId;

    /** ID of the vehicle associated with the operator's break. */
    private int vehicleId;

    /** Start time of the break. */
    private Timestamp startTime;

    /** End time of the break. */
    private Timestamp endTime;

    /** Status of the break (e.g., "Scheduled", "Completed", "Missed"). */
    private String status;

    /**
     * Gets the unique break ID.
     * @return the breakId
     */
    public int getBreakId() {
        return breakId;
    }

    /**
     * Sets the unique break ID.
     * @param breakId the break ID to set
     */
    public void setBreakId(int breakId) {
        this.breakId = breakId;
    }

    /**
     * Gets the operator ID.
     * @return the operatorId
     */
    public int getOperatorId() {
        return operatorId;
    }

    /**
     * Sets the operator ID.
     * @param operatorId the operator ID to set
     */
    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * Gets the vehicle ID associated with the break.
     * @return the vehicleId
     */
    public int getVehicleId() {
        return vehicleId;
    }

    /**
     * Sets the vehicle ID.
     * @param vehicleId the vehicle ID to set
     */
    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    /**
     * Gets the start time of the break.
     * @return the startTime
     */
    public Timestamp getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the break.
     * @param startTime the start timestamp to set
     */
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the end time of the break.
     * @return the endTime
     */
    public Timestamp getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the break.
     * @param endTime the end timestamp to set
     */
    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the status of the break.
     * @return the status (e.g., "Scheduled", "Completed")
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the break.
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
