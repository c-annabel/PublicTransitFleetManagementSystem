package presentation;

import business.VehicleService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Servlet responsible for deleting a vehicle from the system.
 * 
 * Handles POST requests to remove a vehicle using its ID and delegates the
 * deletion operation to the VehicleService class.
 * 
 * Redirects the client to the vehicle management page with success or error indicators.
 * 
 * URL Pattern: /deleteVehicle
 * 
 * Expected parameter:
 * - vehicleId: the ID of the vehicle to be deleted
 * 
 * @author Annabel Cheng
 * @course CST8288 Lab013 Final Project
 */
@WebServlet("/deleteVehicle")
public class DeleteVehicleServlet extends HttpServlet {

    private final VehicleService vehicleService = new VehicleService();

    /**
     * Processes POST requests to delete a vehicle.
     *
     * @param request  the HttpServletRequest object that contains the request the client made
     * @param response the HttpServletResponse object that contains the response the servlet returns
     * @throws ServletException if the request could not be handled
     * @throws IOException if an input or output error is detected when the servlet handles the request
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int vehicleId = Integer.parseInt(request.getParameter("vehicleId"));
        try {
            vehicleService.deleteVehicle(vehicleId);
            response.sendRedirect("vehicleManagement.jsp?success=deleted");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("vehicleManagement.jsp?error=deleteFailed");
        }
    }
}
