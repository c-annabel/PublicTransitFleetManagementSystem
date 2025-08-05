package presentation;

import business.GPSLogService;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/gpsAction")
public class GpsActionServlet extends HttpServlet {
    private final GPSLogService gpsService = new GPSLogService();

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
