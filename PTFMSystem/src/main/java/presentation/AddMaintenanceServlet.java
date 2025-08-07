package presentation;

import command.AddTaskCommand;
import command.MaintenanceCommand;
import dataaccess.MaintenanceDAO;
import transferobjects.MaintenanceTask;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Servlet responsible for handling POST requests to add a new maintenance task.
 * 
 * This servlet performs validation to ensure the scheduled date is at least
 * two days in the future and not already booked. If validations pass, a new
 * {@code MaintenanceTask} is created and persisted using the Command Pattern.
 * 
 * URL mapping: /addMaintenance
 * 
 * Redirects to {@code maintenanceSchedule.jsp} with appropriate success or error query parameters.
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
@WebServlet("/addMaintenance")
public class AddMaintenanceServlet extends HttpServlet {
    private MaintenanceDAO dao;

    /**
     * Initializes the servlet and sets up the {@code MaintenanceDAO} instance.
     */
    @Override
    public void init() {
        dao = new MaintenanceDAO();
    }

    /**
     * Handles the HTTP POST request to add a maintenance task.
     * Validates the input, constructs a {@code MaintenanceTask},
     * and executes the {@code AddTaskCommand}.
     *
     * @param request the HttpServletRequest containing task parameters
     * @param response the HttpServletResponse used to redirect on success or error
     * @throws IOException if an I/O error occurs during redirection
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int vehicleId = Integer.parseInt(request.getParameter("vehicleId"));
            String description = request.getParameter("description");
            LocalDate date = LocalDate.parse(request.getParameter("scheduledDate"));
            BigDecimal cost = new BigDecimal(request.getParameter("cost"));
            boolean completed = Boolean.parseBoolean(request.getParameter("completed"));

            // Ensure date is at least two days in the future
            if (!date.isAfter(LocalDate.now().plusDays(1))) {
                response.sendRedirect("maintenanceSchedule.jsp?error=1");
                return;
            }

            MaintenanceDAO dao = new MaintenanceDAO();
            // Prevent duplicate bookings for the same date
            if (dao.isDateAlreadyTaken(date)) {
                response.sendRedirect("maintenanceSchedule.jsp?error=duplicate");
                return;
            }

            // Default alert ID is 0 if not triggered by an alert
            int alertId = 0;

            MaintenanceTask task = new MaintenanceTask();
            task.setVehicleId(vehicleId);
            task.setAlertId(alertId);
            task.setDescription(description);
            task.setScheduledDatetime(Timestamp.valueOf(
                LocalDateTime.of(date, LocalDateTime.now().toLocalTime().withHour(9).withMinute(0).withSecond(0).withNano(0))));
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
