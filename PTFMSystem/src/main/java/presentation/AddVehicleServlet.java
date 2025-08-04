package presentation;

import business.VehicleService;
import transferobjects.Vehicle;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/addVehicle")
public class AddVehicleServlet extends HttpServlet {
    private final VehicleService vehicleService = new VehicleService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String number = request.getParameter("vehicleNumber");
        String type = request.getParameter("vehicleType");
        String fuel = request.getParameter("fuelType");
        double consumption = Double.parseDouble(request.getParameter("consumptionRate"));
        int passengers = Integer.parseInt(request.getParameter("maxPassengers"));
        int routeId = Integer.parseInt(request.getParameter("routeId"));

        Vehicle v = new Vehicle();
        v.setVehicleNumber(number);
        v.setVehicleType(type);
        v.setFuelType(fuel);
        v.setConsumptionRate(consumption);
        v.setMaxPassengers(passengers);
        v.setRouteId(routeId);

        try {
            vehicleService.addVehicle(v);
            response.sendRedirect("vehicleManagement.jsp?success=1");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("vehicleManagement.jsp?error=1");
        }
    }
}
