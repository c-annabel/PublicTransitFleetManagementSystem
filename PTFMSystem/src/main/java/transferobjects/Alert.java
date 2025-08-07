package transferobjects;

import java.sql.Timestamp;

/**
 * Represents an alert generated for a vehicle in the Public Transit Fleet Management System (PTFMS).
 * 
 * This class captures essential information about system-generated alerts including their type,
 * message content, severity level, timestamp of generation, and resolution status. Alerts may be used
 * to notify managers or operators of issues like low fuel, brake wear, or scheduled maintenance.
 * 
 * @author Annabel Cheng
 * Course: 25S CST8288 Section 013 Final Project
 */
public class Alert {

    /** Unique identifier for the alert. */
    private int alertId;

    /** ID of the vehicle associated with the alert. */
    private int vehicleId;

    /** Type/category of the alert (e.g., "Fuel", "Brake", "Diagnostics"). */
    private String alertType;

    /** Descriptive message providing details about the alert condition. */
    private String alertMessage;

    /** Severity level of the alert (e.g., "Low", "Medium", "High"). */
    private String severity;

    /** Timestamp when the alert was generated. */
    private Timestamp generatedAt;

    /** Indicates whether the alert has been resolved. */
    private boolean resolved;

    /**
     * Gets the unique ID of the alert.
     * 
     * @return the alert ID
     */
    public int getAlertId() {
        return alertId;
    }

    /**
     * Sets the unique ID of the alert.
     * 
     * @param alertId the alert ID to set
     */
    public void setAlertId(int alertId) {
        this.alertId = alertId;
    }

    /**
     * Gets the vehicle ID associated with this alert.
     * 
     * @return the vehicle ID
     */
    public int getVehicleId() {
        return vehicleId;
    }

    /**
     * Sets the vehicle ID for this alert.
     * 
     * @param vehicleId the vehicle ID to set
     */
    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    /**
     * Gets the type/category of the alert.
     * 
     * @return the alert type
     */
    public String getAlertType() {
        return alertType;
    }

    /**
     * Sets the type/category of the alert.
     * 
     * @param alertType the alert type to set
     */
    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    /**
     * Gets the descriptive message of the alert.
     * 
     * @return the alert message
     */
    public String getAlertMessage() {
        return alertMessage;
    }

    /**
     * Sets the descriptive message of the alert.
     * 
     * @param alertMessage the alert message to set
     */
    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    /**
     * Gets the severity level of the alert.
     * 
     * @return the severity level
     */
    public String getSeverity() {
        return severity;
    }

    /**
     * Sets the severity level of the alert.
     * 
     * @param severity the severity level to set
     */
    public void setSeverity(String severity) {
        this.severity = severity;
    }

    /**
     * Gets the timestamp when the alert was generated.
     * 
     * @return the generation timestamp
     */
    public Timestamp getGeneratedAt() {
        return generatedAt;
    }

    /**
     * Sets the timestamp when the alert was generated.
     * 
     * @param generatedAt the generation timestamp to set
     */
    public void setGeneratedAt(Timestamp generatedAt) {
        this.generatedAt = generatedAt;
    }

    /**
     * Checks whether the alert has been resolved.
     * 
     * @return true if resolved, false otherwise
     */
    public boolean isResolved() {
        return resolved;
    }

    /**
     * Sets the resolution status of the alert.
     * 
     * @param resolved true if the alert is resolved; false otherwise
     */
    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }
}
