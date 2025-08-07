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

@WebServlet("/check-booked-dates")
public class CheckBookedDatesServlet extends HttpServlet {

    private MaintenanceDAO maintenanceDAO;

    @Override
    public void init() throws ServletException {
        maintenanceDAO = new MaintenanceDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
      
           List<LocalDate> bookedDates;
           
            String vehicleIdParam = request.getParameter("vehicleId");
            
            if (vehicleIdParam != null && !vehicleIdParam.isEmpty()) {
                int vehicleId = Integer.parseInt(vehicleIdParam);
                bookedDates = maintenanceDAO.getBookedDatesForVehicle(vehicleId); // ⬅️ new DAO method (see below)
            } else {
                bookedDates = maintenanceDAO.getAllBookedDates(); // ⬅️ fallback to existing behavior
            }

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
