package presentation;

import business.ConsumptionService;
import dataaccess.ConsumptionDAO;
import dataaccess.VehicleConfigDAO;
import dataaccess.DiagnosticsDAO;
import transferobjects.ConsumptionRecord;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Servlet that handles real-time fuel and energy consumption monitoring.
 * 
 * This servlet retrieves consumption data from the database and displays it
 * in an HTML table. It also triggers alert messages when consumption exceeds
 * configured thresholds, using the {@code ConsumptionService}.
 * 
 * Alerts are rendered in a hidden HTML element for potential client-side usage.
 * 
 * URL mapping: {@code /consumption}
 * 
 * Responsibilities:
 * <ul>
 *   <li>Fetch vehicle consumption records</li>
 *   <li>Apply strategy-based consumption calculation</li>
 *   <li>Compare against thresholds</li>
 *   <li>Display alerts and diagnostic data</li>
 * </ul>
 * 
 * Requires properly initialized DAOs and threshold settings.
 * 
 * Output: HTML table rendered directly in response
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
@WebServlet("/consumption")
public class ConsumptionServlet extends HttpServlet {

    private ConsumptionService consumptionService;
    private ConsumptionDAO consumptionDAO;
    private VehicleConfigDAO configDAO;
    private DiagnosticsDAO diagnosticsDAO;

    /**
     * Initializes DAOs and registers the manager observer for alert tracking.
     *
     * @throws ServletException if initialization fails
     */
    @Override
    public void init() throws ServletException {
        super.init();
        consumptionService = new ConsumptionService();
        consumptionService.registerManager("Transit Manager");
        consumptionDAO = new ConsumptionDAO();
        configDAO = new VehicleConfigDAO();
        diagnosticsDAO = new DiagnosticsDAO();
    }

    /**
     * Handles GET requests by generating an HTML page that shows real-time consumption data
     * and hidden alerts if thresholds are exceeded.
     *
     * @param request  the {@code HttpServletRequest}
     * @param response the {@code HttpServletResponse}
     * @throws ServletException if servlet processing fails
     * @throws IOException      if I/O error occurs while writing response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        consumptionService.clearAlerts();

        // === FUEL & ENERGY MONITORING TABLE ===
        out.println("<h3>Real-Time Fuel & Energy Monitoring</h3>");
        out.println("<table style='width:100%; border-collapse: collapse;' border='1'>");
        out.println("<thead><tr style='background:#f2f2f2;'>"
                + "<th>Vehicle</th><th>Type</th><th>Distance (km)</th>"
                + "<th>Fuel Used (L)</th><th>Energy Used (kWh)</th>"
                + "<th>Consumption (per 100km)</th></tr></thead><tbody>");

        try {
            List<ConsumptionRecord> records = consumptionDAO.getAllConsumption();
            Map<String, Double> thresholds = configDAO.getThresholds();

            for (ConsumptionRecord record : records) {
                double threshold = thresholds.getOrDefault(record.getVehicleType(), 0.0);
                double actualConsumption = consumptionService.calculateAndCheck(
                        record.getVehicleType(),
                        record.getVehicleNumber(),
                        record.getDistance(),
                        record.getFuelUsed(),
                        record.getEnergyUsed(),
                        threshold
                );

                out.println("<tr>");
                out.println("<td>" + record.getVehicleNumber() + "</td>");
                out.println("<td>" + record.getVehicleType() + "</td>");
                out.println("<td>" + record.getDistance() + "</td>");
                out.println("<td>" + record.getFuelUsed() + "</td>");
                out.println("<td>" + record.getEnergyUsed() + "</td>");
                out.println("<td>" + String.format("%.2f", actualConsumption) + "</td>");
                out.println("</tr>");
            }

        } catch (SQLException e) {
            out.println("<tr><td colspan='6'>Error loading data</td></tr>");
            e.printStackTrace();
        }

        out.println("</tbody></table><br><br>");

        // === Alerts from fuel monitor ===
        List<String> alerts = consumptionService.getAlertMessages();
        if (!alerts.isEmpty()) {
            out.println("<div id='hiddenAlerts' style='display:none;'>");
            for (String msg : alerts) {
                out.println(msg + "|");
            }
            out.println("</div>");
        }
    }
}
