package transferobjects;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class MaintenanceTask {
    private int taskId;
    private int vehicleId;
    private int alertId;
    private String description;
    private Timestamp scheduledDatetime;
    private BigDecimal cost;
    private boolean completed;

    // Getters and Setters
    public int getTaskId() {
        return taskId;
    }
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getVehicleId() {
        return vehicleId;
    }
    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getAlertId() {
        return alertId;
    }
    public void setAlertId(int alertId) {
        this.alertId = alertId;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getScheduledDatetime() {
        return scheduledDatetime;
    }
    public void setScheduledDatetime(Timestamp scheduledDatetime) {
        this.scheduledDatetime = scheduledDatetime;
    }

    public BigDecimal getCost() {
        return cost;
    }
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public boolean isCompleted() {
        return completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
