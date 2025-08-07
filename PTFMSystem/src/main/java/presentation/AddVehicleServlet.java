package presentation;

import business.VehicleService;
import transferobjects.Vehicle;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Servlet responsible for handling POST requests to add a new vehicle.
 * 
 * This servlet validates form input, constructs a {@code Vehicle} object using
 * the Builder Pattern, and passes it to the {@code VehicleService} for persistence.
 * 
 * It then redirects the user to {@code vehicleManagement.jsp} with a success or error flag.
 * 
 * URL mapping: {@code /addVehicle}
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
@WebServlet("/addVehicle")
public class AddVehicleServlet extends HttpServlet {
    private final VehicleService vehicleService = new VehicleService();

    /**
     * Processes the POST request to add a new vehicle.
     * Validates required parameters, builds a {@code Vehicle} object,
     * and delegates insertion to the service layer.
     *
     * @param request  the HTTP request containing vehicle form data
     * @param response the HTTP response used to redirect to the result page
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs during redirection
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Validate input fields
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

            double consumption = Double.parseDouble(consumptionStr);
            int passengers = Integer.parseInt(passengersStr);
            int routeId = Integer.parseInt(routeIdStr);

            // Build Vehicle object using Builder Pattern
            Vehicle vehicle = new Vehicle.Builder()
                    .vehicleNumber(number)
                    .vehicleType(type)
                    .fuelType(fuel)
                    .consumptionRate(consumption)
                    .maxPassengers(passengers)
                    .routeId(routeId)
                    .build();

            // Call Service to Add Vehicle
            vehicleService.addVehicle(vehicle);
            response.sendRedirect("vehicleManagement.jsp?success=1");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("vehicleManagement.jsp?error=1");
        }
    }
}
