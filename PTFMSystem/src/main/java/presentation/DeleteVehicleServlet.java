package presentation;

import business.VehicleService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/deleteVehicle")
public class DeleteVehicleServlet extends HttpServlet {
    private final VehicleService vehicleService = new VehicleService();

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
