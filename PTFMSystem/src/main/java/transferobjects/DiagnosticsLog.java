package transferobjects;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class DiagnosticsLog {
    private int vehicleId;
    private String vehicleType;
    private BigDecimal engineHealth;
    private BigDecimal catenaryCondition;
    private BigDecimal pantographCondition;
    private BigDecimal circuitBreakerCondition;
    private BigDecimal hoursUsed;
    private BigDecimal brakeCondition;
    private BigDecimal tireCondition;
    private BigDecimal axleCondition;
    private Timestamp logDatetime;

    // Getters and Setters
    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public BigDecimal getEngineHealth() {
        return engineHealth;
    }

    public void setEngineHealth(BigDecimal engineHealth) {
        this.engineHealth = engineHealth;
    }

    public BigDecimal getCatenaryCondition() {
        return catenaryCondition;
    }

    public void setCatenaryCondition(BigDecimal catenaryCondition) {
        this.catenaryCondition = catenaryCondition;
    }

    public BigDecimal getPantographCondition() {
        return pantographCondition;
    }

    public void setPantographCondition(BigDecimal pantographCondition) {
        this.pantographCondition = pantographCondition;
    }

    public BigDecimal getCircuitBreakerCondition() {
        return circuitBreakerCondition;
    }

    public void setCircuitBreakerCondition(BigDecimal circuitBreakerCondition) {
        this.circuitBreakerCondition = circuitBreakerCondition;
    }

    public BigDecimal getHoursUsed() {
        return hoursUsed;
    }

    public void setHoursUsed(BigDecimal hoursUsed) {
        this.hoursUsed = hoursUsed;
    }

    public BigDecimal getBrakeCondition() {
        return brakeCondition;
    }

    public void setBrakeCondition(BigDecimal brakeCondition) {
        this.brakeCondition = brakeCondition;
    }

    public BigDecimal getTireCondition() {
        return tireCondition;
    }

    public void setTireCondition(BigDecimal tireCondition) {
        this.tireCondition = tireCondition;
    }

    public BigDecimal getAxleCondition() {
        return axleCondition;
    }

    public void setAxleCondition(BigDecimal axleCondition) {
        this.axleCondition = axleCondition;
    }

    public Timestamp getLogDatetime() {
        return logDatetime;
    }

    public void setLogDatetime(Timestamp logDatetime) {
        this.logDatetime = logDatetime;
    }
}
