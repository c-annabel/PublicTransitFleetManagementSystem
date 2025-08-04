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
        
        String idParam = request.getParameter("vehicleId");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect("vehicleManagement.jsp?error=noIdSelected");
            return;
        } 
        
        int vehicleId = Integer.parseInt(request.getParameter("vehicleId"));
        String number = request.getParameter("vehicleNumber");
        String type = request.getParameter("vehicleType");
        String fuel = request.getParameter("fuelType");
        double consumption = Double.parseDouble(request.getParameter("consumptionRate"));
        int passengers = Integer.parseInt(request.getParameter("maxPassengers"));
        int routeId = Integer.parseInt(request.getParameter("routeId"));

        Vehicle v = new Vehicle();
        v.setVehicleId(vehicleId);
        v.setVehicleNumber(number);
        v.setVehicleType(type);
        v.setFuelType(fuel);
        v.setConsumptionRate(consumption);
        v.setMaxPassengers(passengers);
        v.setRouteId(routeId);

        try {
            vehicleService.updateVehicle(v);
            response.sendRedirect("vehicleManagement.jsp?success=updated");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("vehicleManagement.jsp?error=updateFailed");
        }
    }
}
