package transferobjects;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class DiagnosticsLog {
    private int vehicleId;
    private BigDecimal engineHealth;
    private BigDecimal catenaryCondition;
    private BigDecimal pantographCondition;
    private BigDecimal circuitBreakerCondition;
    private Timestamp logDatetime;
    private String    vehicleType;

    // Getters & Setters
    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }

    public BigDecimal getEngineHealth() { return engineHealth; }
    public void setEngineHealth(BigDecimal engineHealth) { this.engineHealth = engineHealth; }

    public BigDecimal getCatenaryCondition() { return catenaryCondition; }
    public void setCatenaryCondition(BigDecimal catenaryCondition) { this.catenaryCondition = catenaryCondition; }

    public BigDecimal getPantographCondition() { return pantographCondition; }
    public void setPantographCondition(BigDecimal pantographCondition) { this.pantographCondition = pantographCondition; }

    public BigDecimal getCircuitBreakerCondition() { return circuitBreakerCondition; }
    public void setCircuitBreakerCondition(BigDecimal circuitBreakerCondition) { this.circuitBreakerCondition = circuitBreakerCondition; }

    public Timestamp getLogDatetime() { return logDatetime; }
    public void setLogDatetime(Timestamp logDatetime) { this.logDatetime = logDatetime; }
    
    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
}
