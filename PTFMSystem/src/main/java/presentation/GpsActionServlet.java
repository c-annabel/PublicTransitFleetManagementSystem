package presentation;

import business.GPSLogService;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Servlet responsible for logging vehicle GPS actions such as arrival and departure.
 * 
 * Handles POST requests from the GPS operator interface, identifies the requested action,
 * and delegates the operation to the {@code GPSLogService}.
 * 
 * URL Pattern: /gpsAction
 * 
 * Expected parameters:
 * <ul>
 *     <li>vehicleId: the ID of the vehicle</li>
 *     <li>stationId: the ID of the station</li>
 *     <li>action: either "arrival" or "departure"</li>
 * </ul>
 * 
 * Redirects with appropriate success or error messages.
 * 
 * @author Annabel Cheng
 * @course CST8288 Lab013 Final Project
 */
@WebServlet("/gpsAction")
public class GpsActionServlet extends HttpServlet {

    private final GPSLogService gpsService = new GPSLogService();

    /**
     * Processes POST requests to log arrival or departure of a vehicle at a station.
     *
     * @param request  the HttpServletRequest containing vehicle ID, station ID, and action
     * @param response the HttpServletResponse that redirects with status messages
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int vehicleId = Integer.parseInt(request.getParameter("vehicleId"));
            int stationId = Integer.parseInt(request.getParameter("stationId"));
            String action = request.getParameter("action");

            if ("arrival".equals(action)) {
                gpsService.logArrival(vehicleId, stationId);
                response.sendRedirect("gpsOperator.jsp?msg=Arrival+logged+successfully&type=success");
            } else if ("departure".equals(action)) {
                gpsService.logDeparture(vehicleId, stationId);
                response.sendRedirect("gpsOperator.jsp?msg=Departure+logged+successfully&type=success");
            } else {
                response.sendRedirect("gpsOperator.jsp?msg=Invalid+action&type=error");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("gpsOperator.jsp?msg=Invalid+input+format&type=error");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("gpsOperator.jsp?msg=Error+processing+request&type=error");
        }
    }
}
