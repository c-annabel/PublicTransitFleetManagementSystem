// ✅ AddMaintenanceServlet.java
package presentation;

import command.AddTaskCommand;
import command.MaintenanceCommand;
import dataaccess.MaintenanceDAO;
import transferobjects.MaintenanceTask;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@WebServlet("/addMaintenance")
public class AddMaintenanceServlet extends HttpServlet {
    private MaintenanceDAO dao;

    @Override
    public void init() {
        dao = new MaintenanceDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int vehicleId = Integer.parseInt(request.getParameter("vehicleId"));
            String description = request.getParameter("description");
            LocalDate date = LocalDate.parse(request.getParameter("scheduledDate"));
            BigDecimal cost = new BigDecimal(request.getParameter("cost"));
            boolean completed = Boolean.parseBoolean(request.getParameter("completed"));
           

            if (!date.isAfter(LocalDate.now().plusDays(1))) {
                response.sendRedirect("maintenanceSchedule.jsp?error=1");
                return;
            }
            
                    MaintenanceDAO dao = new MaintenanceDAO();
if (dao.isDateAlreadyTaken(date)) {
    response.sendRedirect("maintenanceSchedule.jsp?error=duplicate");
    return;
}
                   // Set the default alert ID if not coming from an alert
        int alertId = 0;

            MaintenanceTask task = new MaintenanceTask();
            task.setVehicleId(vehicleId);
            task.setAlertId(alertId);
            task.setDescription(description);
            task.setScheduledDatetime(Timestamp.valueOf(LocalDateTime.of(date, LocalDateTime.now().toLocalTime().withHour(9).withMinute(0).withSecond(0).withNano(0))));
            task.setCost(cost);
            task.setCompleted(completed);

            MaintenanceCommand command = new AddTaskCommand(task, dao);
            command.execute();

            response.sendRedirect("maintenanceSchedule.jsp?success=1");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("maintenanceSchedule.jsp?error=1");
        }
    }
}
