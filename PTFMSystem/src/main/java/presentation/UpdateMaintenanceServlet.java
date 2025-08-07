package presentation;

import command.MaintenanceCommand;
import command.UpdateTaskCommand;
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

/**
 * Servlet responsible for handling updates to existing maintenance tasks.
 * 
 * This servlet enforces:
 * <ul>
 *   <li>Read-only vehicle ID</li>
 *   <li>Validation against completed tasks</li>
 *   <li>2-day minimum rule for schedule changes</li>
 *   <li>No duplicate dates across tasks</li>
 * </ul>
 * 
 * If validations pass, it uses the Command pattern to execute the update.
 * 
 * URL mapping: {@code /updateMaintenance}
 * 
 * @author Annabel Cheng
 * @course CST8288 Lab013 Final Project
 */
@WebServlet("/updateMaintenance")
public class UpdateMaintenanceServlet extends HttpServlet {
    private MaintenanceDAO dao;

    /**
     * Initializes the DAO used for maintenance task access.
     */
    @Override
    public void init() {
        dao = new MaintenanceDAO();
    }

    /**
     * Handles POST requests to update an existing maintenance task.
     *
     * Validates task existence, completion status, schedule conflicts,
     * and applies the update via command execution.
     *
     * @param request  the {@code HttpServletRequest} containing form data
     * @param response the {@code HttpServletResponse} for redirection
     * @throws IOException if redirection fails
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int taskId = Integer.parseInt(request.getParameter("taskId"));
            MaintenanceTask existing = dao.getTaskById(taskId);

            if (existing == null || existing.isCompleted()) {
                response.sendRedirect("maintenanceSchedule.jsp?error=completed");
                return;
            }

            // Get new values
            int vehicleId = existing.getVehicleId(); // Enforce read-only
            LocalDate scheduledDate = LocalDate.parse(request.getParameter("scheduledDate"));
            BigDecimal cost = new BigDecimal(request.getParameter("cost"));
            boolean completed = Boolean.parseBoolean(request.getParameter("completed"));

            LocalDate oldDate = existing.getScheduledDatetime().toLocalDateTime().toLocalDate();

            // Check if scheduled date changed
            if (!scheduledDate.equals(oldDate)) {
                if (scheduledDate.isBefore(LocalDate.now().plusDays(2))) {
                    response.sendRedirect("maintenanceSchedule.jsp?error=invalidDate");
                    return;
                }

                // Check duplicate date
                if (dao.isDateAlreadyBookedExcludingTask(vehicleId, scheduledDate, taskId)) {
                    response.sendRedirect("maintenanceSchedule.jsp?error=duplicate");
                    return;
                }

                if (dao.isDateAlreadyTaken(scheduledDate)) {
                    response.sendRedirect("maintenanceSchedule.jsp?error=duplicate");
                    return;
                }
            }

            // Set updated values
            existing.setScheduledDatetime(Timestamp.valueOf(scheduledDate.atTime(9, 0)));
            existing.setCost(cost);
            existing.setCompleted(completed);

            // Execute update
            MaintenanceCommand command = new UpdateTaskCommand(existing, dao);
            command.execute();

            response.sendRedirect("maintenanceSchedule.jsp?success=updated");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("maintenanceSchedule.jsp?error=1");
        }
    }
}
