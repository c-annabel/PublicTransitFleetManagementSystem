package presentation;

import dataaccess.DiagnosticsDAO;
import dataaccess.AlertDAO;
import transferobjects.DiagnosticsLog;
import transferobjects.Alert;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/predictive-maintenance")
public class PredictiveMaintenanceServlet extends HttpServlet {

    private DiagnosticsDAO diagnosticsDAO;
    private AlertDAO alertDAO;

    // Threshold constants
    private static final double ENGINE_HEALTH_THRESHOLD = 80.0;
    private static final double PANTOGRAPH_THRESHOLD = 85.0;
    private static final double CATENARY_THRESHOLD = 85.0;

    @Override
    public void init() throws ServletException {
        diagnosticsDAO = new DiagnosticsDAO();
        alertDAO = new AlertDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Page header
        out.println("<table style='width:100%; border-collapse: collapse;' border='1'>");
        out.println("<thead><tr style='background:#f2f2f2;'>"
                + "<th>Vehicle ID</th>"
                + "<th>Type</th>"
                + "<th>Engine Health</th>"
                + "<th>Catenary</th>"
                + "<th>Pantograph</th>"
                + "<th>Circuit Breaker</th>"
                + "<th>Status</th>"
                + "<th>Action</th>"
                + "</tr></thead><tbody>");

        StringBuilder alertPopup = new StringBuilder();

        try {
            // Fetch latest diagnostics with vehicle type
            List<DiagnosticsLog> logs = diagnosticsDAO.getLatestDiagnosticsWithType();

            for (DiagnosticsLog log : logs) {
                String vehicleType = log.getVehicleType();
                BigDecimal engineHealth = log.getEngineHealth();
                BigDecimal pantograph = log.getPantographCondition();
                BigDecimal catenary = log.getCatenaryCondition();

                String status = "OK";
                String alertMsg = null;

                // Predictive Maintenance Logic
                if ("Diesel Bus".equalsIgnoreCase(vehicleType)) {
                    if (engineHealth != null && engineHealth.doubleValue() < ENGINE_HEALTH_THRESHOLD) {
                        status = "Needs Maintenance";
                        alertMsg = "Diesel Bus (ID: " + log.getVehicleId() + ") engine health below 80%.";
                    }
                } else if ("Diesel-Electric Train".equalsIgnoreCase(vehicleType)) {
                    if ((engineHealth != null && engineHealth.doubleValue() < ENGINE_HEALTH_THRESHOLD) ||
                            (pantograph != null && pantograph.doubleValue() < PANTOGRAPH_THRESHOLD)) {
                        status = "Needs Maintenance";
                        alertMsg = "Train (ID: " + log.getVehicleId() + ") engine or pantograph below threshold.";
                    }
                } else if ("Electric Light Rail".equalsIgnoreCase(vehicleType)) {
                    if ((pantograph != null && pantograph.doubleValue() < PANTOGRAPH_THRESHOLD) ||
                            (catenary != null && catenary.doubleValue() < CATENARY_THRESHOLD)) {
                        status = "Needs Maintenance";
                        alertMsg = "Light Rail (ID: " + log.getVehicleId() + ") pantograph or catenary below threshold.";
                    }
                }

                // Handle Alert Logic
                int alertId = -1;
                if (alertMsg != null) {
                    alertId = alertDAO.getExistingAlertId(log.getVehicleId());
                    if (alertId == -1) {
                        // Insert new alert
                        Alert alert = new Alert();
                        alert.setVehicleId(log.getVehicleId());
                        alert.setAlertType("Maintenance");
                        alert.setAlertMessage(alertMsg);
                        alert.setSeverity("High");
                        alertId = alertDAO.insertAlert(alert);
                        alertPopup.append(alertMsg).append("|");
                    }
                }

                // Render Table Row
                out.println("<tr>");
                out.println("<td>" + log.getVehicleId() + "</td>");
                out.println("<td>" + vehicleType + "</td>");
                out.println("<td>" + (engineHealth != null ? engineHealth : "-") + "</td>");
                out.println("<td>" + (catenary != null ? catenary : "-") + "</td>");
                out.println("<td>" + (pantograph != null ? pantograph : "-") + "</td>");
                out.println("<td>" + (log.getCircuitBreakerCondition() != null ? log.getCircuitBreakerCondition() : "-") + "</td>");
                out.println("<td>" + status + "</td>");

                // Book Maintenance button if needed
                if ("Needs Maintenance".equals(status)) {
                    out.println("<td>");
                    out.println("<form action='bookMaintenance.jsp' method='get'>");
                    out.println("<input type='hidden' name='vehicleId' value='" + log.getVehicleId() + "'/>");
                    if (alertId != -1) {
                        out.println("<input type='hidden' name='alertId' value='" + alertId + "'/>");
                    }
                    out.println("<button type='submit'>Book</button>");
                    out.println("</form>");
                    out.println("</td>");
                } else {
                    out.println("<td>-</td>");
                }
                out.println("</tr>");
            }

            out.println("</tbody></table>");

            // Hidden Alerts for Popup
            if (alertPopup.length() > 0) {
                out.println("<div id='hiddenAlerts' style='display:none;'>" + alertPopup.toString() + "</div>");
            }

        } catch (Exception e) {
            out.println("<tr><td colspan='8'>Error loading data</td></tr>");
            e.printStackTrace();
        }
    }
}
