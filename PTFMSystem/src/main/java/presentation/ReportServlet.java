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

@WebServlet("/ReportServlet")
public class ReportServlet extends HttpServlet {

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
