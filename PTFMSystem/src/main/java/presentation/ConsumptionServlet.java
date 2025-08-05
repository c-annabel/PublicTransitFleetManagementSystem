package presentation;

import business.ConsumptionService;
import dataaccess.ConsumptionDAO;
import dataaccess.VehicleConfigDAO;
import transferobjects.ConsumptionRecord;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet("/consumption")
public class ConsumptionServlet extends HttpServlet {

    private ConsumptionService consumptionService;
    private ConsumptionDAO consumptionDAO;
    private VehicleConfigDAO configDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        consumptionService = new ConsumptionService();
        consumptionService.registerManager("Transit Manager");
        consumptionDAO = new ConsumptionDAO();
        configDAO = new VehicleConfigDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        consumptionService.clearAlerts();
        out.println("<h3>Real-Time Fuel & Energy Monitoring</h3>");
        out.println("<table style='width:100%; border-collapse: collapse;' border='1'>");
        out.println("<thead><tr style='background:#f2f2f2;'>"
                + "<th>Vehicle</th><th>Type</th><th>Distance (km)</th>"
                + "<th>Fuel Used (L)</th><th>Energy Used (kWh)</th>"
                + "<th>Consumption (per 100km)</th></tr></thead><tbody>");

        try {
            
            // Fetch all consumption records
            List<ConsumptionRecord> records = consumptionDAO.getAllConsumption();

            // Fetch thresholds for each vehicle type
            Map<String, Double> thresholds = configDAO.getThresholds();

            for (ConsumptionRecord record : records) {
                double threshold = thresholds.getOrDefault(record.getVehicleType(), 0.0);

                // Calculate consumption and check against threshold
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

        out.println("</tbody></table>");

        // ✅ Alerts popup logic
          List<String> alerts = consumptionService.getAlertMessages();
          if (!alerts.isEmpty()) {
              out.println("<div id='hiddenAlerts' style='display:none;'>");
              for (String msg : alerts) {
                  out.println(msg + "|"); // separate with |
              }
              out.println("</div>");
          }
    }
}
