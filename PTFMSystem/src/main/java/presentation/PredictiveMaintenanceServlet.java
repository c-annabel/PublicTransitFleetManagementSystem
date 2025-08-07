package presentation;

import dataaccess.DiagnosticsDAO;
import dataaccess.AlertDAO;
import dataaccess.MaintenanceDAO;
import transferobjects.DiagnosticsLog;
import transferobjects.Alert;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

/**
 * Servlet that performs predictive maintenance analysis using diagnostics data and usage logs.
 *
 * It evaluates multiple diagnostic metrics (engine, brake, pantograph, etc.) for each vehicle,
 * identifies whether maintenance is needed based on predefined thresholds, and either:
 * <ul>
 *     <li>Triggers a new alert entry in the system</li>
 *     <li>Or uses an existing one if present</li>
 * </ul>
 * 
 * If a vehicle is due for maintenance and has no scheduled task, a "Book" button is shown.
 * 
 * URL mapping: {@code /predictive-maintenance}
 * 
 * Responsibilities:
 * <ul>
 *   <li>Retrieve latest diagnostic logs with usage info</li>
 *   <li>Compare metrics against thresholds</li>
 *   <li>Insert alert if needed</li>
 *   <li>Display a table of results and status</li>
 * </ul>
 * 
 * Hidden alerts are appended at the end of the HTML to be handled by the frontend.
 * 
 * @author Annabel Cheng
 * @course CST8288 Lab013 Final Project
 */
@WebServlet("/predictive-maintenance")
public class PredictiveMaintenanceServlet extends HttpServlet {

    private DiagnosticsDAO diagnosticsDAO;
    private AlertDAO alertDAO;
    private MaintenanceDAO maintenanceDAO;

    private static final double ENGINE_HEALTH_THRESHOLD = 80.0;
    private static final double PANTOGRAPH_THRESHOLD = 85.0;
    private static final double CATENARY_THRESHOLD = 85.0;
    private static final double BRAKE_CONDITION_THRESHOLD = 70.0;
    private static final double TIRE_CONDITION_THRESHOLD = 70.0;
    private static final double AXLE_CONDITION_THRESHOLD = 70.0;
    private static final double HOURS_USED_THRESHOLD = 1000.0;

    /**
     * Initializes required DAOs.
     *
     * @throws ServletException if initialization fails
     */
    @Override
    public void init() throws ServletException {
        diagnosticsDAO = new DiagnosticsDAO();
        alertDAO = new AlertDAO();
        maintenanceDAO = new MaintenanceDAO();
    }

    /**
     * Handles GET requests by analyzing diagnostics data and generating the HTML table view.
     * Vehicles that meet failure conditions are flagged, and alerts are managed accordingly.
     *
     * @param request  the {@code HttpServletRequest}
     * @param response the {@code HttpServletResponse}
     * @throws ServletException if servlet processing fails
     * @throws IOException      if an I/O error occurs during rendering
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<table style='width:100%; border-collapse: collapse;' border='1'>");
        out.println("<thead><tr style='background:#f2f2f2;'>"
                + "<th>Vehicle ID</th>"
                + "<th>Type</th>"
                + "<th>Engine Health</th>"
                + "<th>Hours Used</th>"
                + "<th>Brake Condition</th>"
                + "<th>Tire Condition</th>"
                + "<th>Axle Condition</th>"
                + "<th>Catenary</th>"
                + "<th>Pantograph</th>"
                + "<th>Circuit Breaker</th>"
                + "<th>Status</th>"
                + "<th>Action</th>"
                + "</tr></thead><tbody>");

        StringBuilder alertPopup = new StringBuilder();

        try {
            List<DiagnosticsLog> logs = diagnosticsDAO.getLatestDiagnosticsWithUsage();

            for (DiagnosticsLog log : logs) {
                // Vehicle metrics extraction
                int vehicleId = log.getVehicleId();
                String vehicleType = log.getVehicleType();

                BigDecimal engineHealth = log.getEngineHealth();
                BigDecimal pantograph = log.getPantographCondition();
                BigDecimal catenary = log.getCatenaryCondition();
                BigDecimal circuitBreaker = log.getCircuitBreakerCondition();
                BigDecimal brakeCond = log.getBrakeCondition();
                BigDecimal tireCond = log.getTireCondition();
                BigDecimal axleCond = log.getAxleCondition();
                BigDecimal hoursUsed = log.getHoursUsed();

                // Decision logic
                String status = "OK";
                String alertMsg = null;
                boolean needsMaintenance = false;

                // Determine failures by type
                if ("Diesel Bus".equalsIgnoreCase(vehicleType)) {
                    if (engineHealth != null && engineHealth.doubleValue() < ENGINE_HEALTH_THRESHOLD) {
                        needsMaintenance = true;
                        alertMsg = "Diesel Bus (ID: " + vehicleId + ") engine health below threshold";
                    }
                } else if ("Diesel-Electric Train".equalsIgnoreCase(vehicleType)) {
                    if ((engineHealth != null && engineHealth.doubleValue() < ENGINE_HEALTH_THRESHOLD)
                            || (pantograph != null && pantograph.doubleValue() < PANTOGRAPH_THRESHOLD)) {
                        needsMaintenance = true;
                        alertMsg = "Train (ID: " + vehicleId + ") engine or pantograph below threshold";
                    }
                } else if ("Electric Light Rail".equalsIgnoreCase(vehicleType)) {
                    if ((pantograph != null && pantograph.doubleValue() < PANTOGRAPH_THRESHOLD)
                            || (catenary != null && catenary.doubleValue() < CATENARY_THRESHOLD)) {
                        needsMaintenance = true;
                        alertMsg = "Light Rail (ID: " + vehicleId + ") pantograph or catenary below threshold";
                    }
                }

                // Additional checks
                if (!needsMaintenance && brakeCond != null && brakeCond.doubleValue() < BRAKE_CONDITION_THRESHOLD) {
                    needsMaintenance = true;
                    alertMsg = "Vehicle (ID: " + vehicleId + ") brake wear below threshold";
                } else if (!needsMaintenance && tireCond != null && tireCond.doubleValue() < TIRE_CONDITION_THRESHOLD) {
                    needsMaintenance = true;
                    alertMsg = "Vehicle (ID: " + vehicleId + ") tire wear below threshold";
                } else if (!needsMaintenance && axleCond != null && axleCond.doubleValue() < AXLE_CONDITION_THRESHOLD) {
                    needsMaintenance = true;
                    alertMsg = "Vehicle (ID: " + vehicleId + ") axle wear below threshold";
                } else if (!needsMaintenance && hoursUsed != null && hoursUsed.doubleValue() > HOURS_USED_THRESHOLD) {
                    needsMaintenance = true;
                    alertMsg = "Vehicle (ID: " + vehicleId + ") has exceeded recommended usage hours";
                }

                int alertId = -1;
                boolean hasExistingTask = false;

                // If maintenance required, insert or reuse alert
                if (needsMaintenance && alertMsg != null) {
                    status = "Needs Maintenance";
                    alertId = alertDAO.getExistingAlertId(vehicleId);
                    if (alertId == -1) {
                        Alert alert = new Alert();
                        alert.setVehicleId(vehicleId);
                        alert.setAlertType("Maintenance");
                        alert.setAlertMessage(alertMsg);
                        alert.setSeverity("High");
                        alertId = alertDAO.insertAlert(alert);
                    }

                    hasExistingTask = maintenanceDAO.hasScheduledTaskForVehicle(vehicleId);
                    if (!hasExistingTask) {
                        alertPopup.append(alertMsg).append("|");
                    }
                }

                // Render result row
                out.println("<tr>");
                out.println("<td>" + vehicleId + "</td>");
                out.println("<td>" + vehicleType + "</td>");
                out.println("<td>" + (engineHealth != null ? engineHealth : "-") + "</td>");
                out.println("<td>" + (hoursUsed != null ? hoursUsed : "-") + "</td>");
                out.println("<td>" + (brakeCond != null ? brakeCond : "-") + "</td>");
                out.println("<td>" + (tireCond != null ? tireCond : "-") + "</td>");
                out.println("<td>" + (axleCond != null ? axleCond : "-") + "</td>");
                out.println("<td>" + (catenary != null ? catenary : "-") + "</td>");
                out.println("<td>" + (pantograph != null ? pantograph : "-") + "</td>");
                out.println("<td>" + (circuitBreaker != null ? circuitBreaker : "-") + "</td>");
                out.println("<td>" + status + "</td>");

                if ("Needs Maintenance".equals(status)) {
                    if (!hasExistingTask) {
                        out.println("<td><button type='button' data-action='book' data-vehicle-id='" + vehicleId + "' data-alert-id='" + alertId + "'>Book</button></td>");
                    } else {
                        out.println("<td><span style='color:gray;'>Maintenance booked</span></td>");
                    }
                } else {
                    out.println("<td>-</td>");
                }

                out.println("</tr>");
            }

            out.println("</tbody></table>");

            if (alertPopup.length() > 0) {
                out.println("<div id='hiddenAlerts' style='display:none;'>" + alertPopup.toString() + "</div>");
            }

        } catch (Exception e) {
            out.println("<tr><td colspan='12'>Error loading data</td></tr>");
            e.printStackTrace();
        }
    }
}
