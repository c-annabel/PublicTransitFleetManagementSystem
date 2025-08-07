package presentation;

import dataaccess.MaintenanceDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

/**
 * Servlet that returns JSON data of already booked maintenance dates.
 * 
 * If a {@code vehicleId} parameter is provided, it returns only the dates
 * booked for that specific vehicle. Otherwise, it returns all booked dates.
 * 
 * This servlet is useful for frontend validation, such as disabling already
 * reserved dates in a date picker.
 * 
 * URL mapping: {@code /check-booked-dates}
 * 
 * Query parameters:
 * <ul>
 *   <li>{@code vehicleId} – optional; filters results to a specific vehicle</li>
 * </ul>
 * 
 * JSON Response:
 * <pre>
 * ["2025-08-10", "2025-08-15", "2025-08-18"]
 * </pre>
 * 
 * Error response:
 * <pre>
 * {"status":"error","message":"Unable to fetch booked dates."}
 * </pre>
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
@WebServlet("/check-booked-dates")
public class CheckBookedDatesServlet extends HttpServlet {

    private MaintenanceDAO maintenanceDAO;

    /**
     * Initializes the DAO used to fetch maintenance data.
     */
    @Override
    public void init() throws ServletException {
        maintenanceDAO = new MaintenanceDAO();
    }

    /**
     * Handles GET requests to retrieve maintenance booking dates.
     *
     * @param request  the HTTP request (may contain {@code vehicleId} as a parameter)
     * @param response the HTTP response to return JSON results
     * @throws IOException if writing the response fails
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            List<LocalDate> bookedDates;
            String vehicleIdParam = request.getParameter("vehicleId");

            if (vehicleIdParam != null && !vehicleIdParam.isEmpty()) {
                int vehicleId = Integer.parseInt(vehicleIdParam);
                bookedDates = maintenanceDAO.getBookedDatesForVehicle(vehicleId);
            } else {
                bookedDates = maintenanceDAO.getAllBookedDates();
            }

            // Build and send JSON array of booked dates
            StringBuilder json = new StringBuilder();
            json.append("[");

            for (int i = 0; i < bookedDates.size(); i++) {
                json.append("\"").append(bookedDates.get(i).toString()).append("\"");
                if (i < bookedDates.size() - 1) {
                    json.append(",");
                }
            }

            json.append("]");
            out.print(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"status\":\"error\",\"message\":\"Unable to fetch booked dates.\"}");
        }
    }
}
