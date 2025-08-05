package presentation;

import business.VehicleService;
import transferobjects.Vehicle;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/updateVehicle")
public class UpdateVehicleServlet extends HttpServlet {
    private final VehicleService vehicleService = new VehicleService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // ✅ Validate ID first
            String idParam = request.getParameter("vehicleId");
            if (idParam == null || idParam.isEmpty()) {
                response.sendRedirect("vehicleManagement.jsp?error=noIdSelected");
                return;
            }

            // ✅ Validate input fields
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

            // ✅ Parse numeric fields
            int vehicleId = Integer.parseInt(idParam);
            double consumption = Double.parseDouble(consumptionStr);
            int passengers = Integer.parseInt(passengersStr);
            int routeId = Integer.parseInt(routeIdStr);

            // ✅ Build Vehicle using Builder Pattern
            Vehicle vehicle = new Vehicle.Builder()
                    .vehicleId(vehicleId)
                    .vehicleNumber(number)
                    .vehicleType(type)
                    .fuelType(fuel)
                    .consumptionRate(consumption)
                    .maxPassengers(passengers)
                    .routeId(routeId)
                    .build();

            // ✅ Perform update
            vehicleService.updateVehicle(vehicle);
            response.sendRedirect("vehicleManagement.jsp?success=updated");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("vehicleManagement.jsp?error=updateFailed");
        }
    }
}

