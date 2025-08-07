package presentation;

import business.reports.Report;
import business.ReportFactory;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Servlet for generating different types of reports based on request parameters.
 * 
 * Supported report types include:
 * <ul>
 *   <li>maintenance</li>
 *   <li>cost</li>
 *   <li>operatorPerformance</li>
 * </ul>
 * 
 * Query parameters:
 * <ul>
 *   <li>{@code action} - type of report to generate</li>
 *   <li>{@code startDate} - start of the date range (optional, format: YYYY-MM-DD)</li>
 *   <li>{@code endDate} - end of the date range (optional, format: YYYY-MM-DD)</li>
 *   <li>{@code operatorId} - operator ID for filtering (optional)</li>
 * </ul>
 * 
 * Responds with a JSON object containing the generated report data.
 * 
 * URL mapping: {@code /ReportServlet}
 * 
 * @author Annabel Cheng
 * @course CST8288 Lab013 Final Project
 */
@WebServlet("/ReportServlet")
public class ReportServlet extends HttpServlet {

    /**
     * Handles GET requests to generate and return report data in JSON format.
     *
     * @param request  the {@code HttpServletRequest} containing parameters
     * @param response the {@code HttpServletResponse} for writing JSON output
     * @throws ServletException if servlet processing fails
     * @throws IOException      if I/O error occurs while writing the response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action"); // "maintenance", "cost", "operatorPerformance"
        String startDateParam = request.getParameter("startDate");
        String endDateParam = request.getParameter("endDate");
        String operatorParam = request.getParameter("operatorId");

        int operatorId = 0;
        if (operatorParam != null && !operatorParam.isEmpty()) {
            try {
                operatorId = Integer.parseInt(operatorParam);
            } catch (NumberFormatException e) {
                operatorId = 0; // Default or fallback
            }
        }

        // Prepare JSON response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // ✅ Validate and parse dates
            LocalDate startDate = null;
            LocalDate endDate = null;

            if (startDateParam != null && !startDateParam.isEmpty()) {
                startDate = LocalDate.parse(startDateParam); // "YYYY-MM-DD"
            }
            if (endDateParam != null && !endDateParam.isEmpty()) {
                endDate = LocalDate.parse(endDateParam);
            }

            // ✅ Use Simple Factory to get correct Report implementation
            Report report = ReportFactory.getReport(action);

            // ✅ Call generateReport with optional date filter and operatorId
            JsonObject reportData = report.generateReport(
                    startDate != null ? startDate.toString() : null,
                    endDate != null ? endDate.toString() : null,
                    operatorId
            );

            response.getWriter().write(reportData.toString());

        } catch (DateTimeParseException e) {
            response.getWriter().write("{\"error\":\"Invalid date format. Please use YYYY-MM-DD.\"}");
        } catch (IllegalArgumentException e) {
            response.getWriter().write("{\"error\":\"Invalid report type: " + e.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"error\":\"An unexpected error occurred.\"}");
        }
    }
}
