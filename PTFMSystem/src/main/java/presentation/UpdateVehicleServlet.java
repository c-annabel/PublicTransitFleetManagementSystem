package presentation;

import business.VehicleService;
import transferobjects.Vehicle;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Servlet responsible for handling POST requests to update vehicle information.
 * 
 * This servlet performs input validation, constructs a {@code Vehicle} object
 * using the Builder Pattern, and delegates the update operation to the {@code VehicleService}.
 * 
 * URL mapping: /updateVehicle
 * 
 * Redirects to {@code vehicleManagement.jsp} with appropriate success or error query parameters.
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
@WebServlet("/updateVehicle")
public class UpdateVehicleServlet extends HttpServlet {
    private final VehicleService vehicleService = new VehicleService();

    /**
     * Handles the HTTP POST request to update a vehicle's data.
     * Validates input, constructs a {@code Vehicle}, and triggers the update service.
     *
     * @param request  the {@code HttpServletRequest} containing form data
     * @param response the {@code HttpServletResponse} used to redirect after processing
     * @throws ServletException if servlet-specific error occurs
     * @throws IOException      if an input or output error is detected
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Validate that the vehicle ID is present
            String idParam = request.getParameter("vehicleId");
            if (idParam == null || idParam.isEmpty()) {
                response.sendRedirect("vehicleManagement.jsp?error=noIdSelected");
                return;
            }

            // Validate required form fields
            String number = request.getParameter("vehicleNumber");
            String type = request.getParameter("vehicleType");
            String fuel = request.getParameter("fuelType");
            String consumptionStr = request.getParameter("consumptionRate");
            String passengersStr = request.getParameter("maxPassengers");
            String routeIdStr = request.getParameter("routeId");

            if (number == null || number.isEmpty() ||
                type == null || type.isEmpty() ||
                fuel == null || fuel.isEmpty() ||
                consumptionStr == null || consumptionStr.isEmpty() ||
                passengersStr == null || passengersStr.isEmpty() ||
                routeIdStr == null || routeIdStr.isEmpty()) {
                response.sendRedirect("vehicleManagement.jsp?error=missingFields");
                return;
            }

            // Parse numeric values
            int vehicleId = Integer.parseInt(idParam);
            double consumption = Double.parseDouble(consumptionStr);
            int passengers = Integer.parseInt(passengersStr);
            int routeId = Integer.parseInt(routeIdStr);

            // Create Vehicle object using Builder Pattern
            Vehicle vehicle = new Vehicle.Builder()
                    .vehicleId(vehicleId)
                    .vehicleNumber(number)
                    .vehicleType(type)
                    .fuelType(fuel)
                    .consumptionRate(consumption)
                    .maxPassengers(passengers)
                    .routeId(routeId)
                    .build();

            // Invoke service layer to perform update
            vehicleService.updateVehicle(vehicle);
            response.sendRedirect("vehicleManagement.jsp?success=updated");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("vehicleManagement.jsp?error=updateFailed");
        }
    }
}
