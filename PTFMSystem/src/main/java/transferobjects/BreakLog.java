package transferobjects;

import java.sql.Timestamp;

public class BreakLog {
    private int breakId;
    private int operatorId;
    private int vehicleId;
    private Timestamp startTime;
    private Timestamp endTime;
    private String status;

    // Getters and Setters
    public int getBreakId() { return breakId; }
    public void setBreakId(int breakId) { this.breakId = breakId; }

    public int getOperatorId() { return operatorId; }
    public void setOperatorId(int operatorId) { this.operatorId = operatorId; }

    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }

    public Timestamp getStartTime() { return startTime; }
    public void setStartTime(Timestamp startTime) { this.startTime = startTime; }

    public Timestamp getEndTime() { return endTime; }
    public void setEndTime(Timestamp endTime) { this.endTime = endTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
