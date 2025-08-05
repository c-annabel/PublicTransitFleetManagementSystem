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

@WebServlet("/ReportServlet")
public class ReportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action"); // "maintenance", "cost", "operatorPerformance"
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String operatorParam = request.getParameter("operatorId");

        int operatorId = 0;
        if (operatorParam != null && !operatorParam.isEmpty()) {
            try {
                operatorId = Integer.parseInt(operatorParam);
            } catch (NumberFormatException e) {
                operatorId = 0;
            }
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Use Simple Factory to create the correct report
            Report report = ReportFactory.getReport(action);

            // Generate report JSON
            JsonObject reportData = report.generateReport(startDate, endDate, operatorId);

            response.getWriter().write(reportData.toString());

        } catch (IllegalArgumentException e) {
            response.getWriter().write("{\"error\":\"Invalid report type: " + e.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"error\":\"An unexpected error occurred\"}");
        }
    }
}
