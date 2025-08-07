package presentation;

import dataaccess.AlertDAO;
import dataaccess.MaintenanceDAO;
import transferobjects.MaintenanceTask;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Servlet responsible for handling maintenance bookings triggered by alerts.
 * 
 * This servlet validates parameters, checks scheduling rules, computes task cost,
 * persists the task in the database, resolves the related alert, and returns
 * a JSON response.
 * 
 * URL mapping: {@code /book-maintenance}
 * 
 * Expected POST parameters:
 * <ul>
 *   <li>{@code alertId} – the associated alert ID</li>
 *   <li>{@code vehicleId} – the ID of the vehicle needing maintenance</li>
 *   <li>{@code taskType} – the type of maintenance (used to calculate cost)</li>
 *   <li>{@code scheduleDate} – the selected maintenance date (YYYY-MM-DD)</li>
 * </ul>
 * 
 * Returns a JSON response indicating success or failure.
 * 
 * Example success response:
 * <pre>
 * {
 *   "status": "success",
 *   "scheduledDatetime": "2025-08-12 09:00:00",
 *   "taskId": 18
 * }
 * </pre>
 * 
 * Example error response:
 * <pre>
 * {
 *   "status": "error",
 *   "message": "This vehicle is already booked on that date."
 * }
 * </pre>
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
@WebServlet("/book-maintenance")
public class BookMaintenanceServlet extends HttpServlet {

    private MaintenanceDAO maintenanceDAO;
    private AlertDAO alertDAO;

    /**
     * Initializes DAOs used by this servlet.
     */
    @Override
    public void init() throws ServletException {
        maintenanceDAO = new MaintenanceDAO();
        alertDAO = new AlertDAO();
    }

    /**
     * Handles POST requests to schedule a maintenance task.
     *
     * @param request  the incoming HTTP request with booking parameters
     * @param response the HTTP response to be returned as JSON
     * @throws IOException if an I/O error occurs during processing
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            // Get and validate parameters
            String alertIdStr = request.getParameter("alertId");
            String vehicleIdStr = request.getParameter("vehicleId");
            String taskType = request.getParameter("taskType");
            String scheduleDate = request.getParameter("scheduleDate");

            if (isEmpty(alertIdStr) || isEmpty(vehicleIdStr) || isEmpty(taskType) || isEmpty(scheduleDate)) {
                sendJson(out, "error", "Missing required fields");
                return;
            }

            int alertId;
            int vehicleId;
            try {
                alertId = Integer.parseInt(alertIdStr);
                vehicleId = Integer.parseInt(vehicleIdStr);
            } catch (NumberFormatException ex) {
                sendJson(out, "error", "Invalid ID values");
                return;
            }

            // Validate date is at least 2 days ahead
            LocalDate selectedDate = LocalDate.parse(scheduleDate);
            LocalDate minAllowedDate = LocalDate.now().plusDays(2);
            if (selectedDate.isBefore(minAllowedDate)) {
                sendJson(out, "error", "Date must be at least 2 days in the future");
                return;
            }

            // Check if date is already booked
            if (maintenanceDAO.isDateAlreadyBooked(vehicleId, selectedDate)) {
                sendJson(out, "error", "This vehicle is already booked on that date.");
                return;
            }

            // Construct full timestamp (09:00)
            String dateTimeStr = scheduleDate + " 09:00:00";
            Timestamp scheduledTimestamp = Timestamp.valueOf(dateTimeStr);

            // Ensure time is still in the future
            Timestamp now = Timestamp.valueOf(LocalDateTime.now());
            if (scheduledTimestamp.before(now)) {
                sendJson(out, "error", "Cannot book maintenance in the past");
                return;
            }

            // Compute task cost
            double cost = getTaskCost(taskType);

            // Create and persist MaintenanceTask
            MaintenanceTask task = new MaintenanceTask();
            task.setVehicleId(vehicleId);
            task.setAlertId(alertId);
            task.setDescription(taskType);
            task.setScheduledDatetime(scheduledTimestamp);
            task.setCost(BigDecimal.valueOf(cost));
            task.setCompleted(false);

            int taskId = maintenanceDAO.insertMaintenanceTask(task);
            if (taskId > 0) {
                alertDAO.resolveAlert(alertId);
                sendJsonSuccess(out, "success", scheduledTimestamp.toString(), taskId);
            } else {
                sendJson(out, "error", "Failed to save maintenance task");
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendJson(out, "error", "Internal server error: " + e.getMessage());
        }
    }

    /**
     * Checks if a string is null or empty after trimming.
     *
     * @param val the input string
     * @return true if empty or null
     */
    private boolean isEmpty(String val) {
        return val == null || val.trim().isEmpty();
    }

    /**
     * Sends a generic JSON response with a status and message.
     *
     * @param out     the PrintWriter to write to
     * @param status  the status ("success" or "error")
     * @param message the response message
     */
    private void sendJson(PrintWriter out, String status, String message) {
        out.print("{\"status\":\"" + status + "\",\"message\":\"" + message + "\"}");
    }

    /**
     * Sends a success JSON response containing the scheduled timestamp and task ID.
     *
     * @param out      the PrintWriter to write to
     * @param status   the status ("success")
     * @param datetime the scheduled timestamp as string
     * @param taskId   the generated task ID
     */
    private void sendJsonSuccess(PrintWriter out, String status, String datetime, int taskId) {
        out.print("{\"status\":\"" + status + "\",\"scheduledDatetime\":\"" + datetime + "\",\"taskId\":" + taskId + "}");
    }

    /**
     * Returns the predefined cost for a given maintenance task type.
     *
     * @param taskType the description/type of task
     * @return the associated cost in dollars
     */
    private double getTaskCost(String taskType) {
        switch (taskType) {
            case "Engine Check": return 500.00;
            case "Brake Inspection": return 300.00;
            case "Pantograph Check": return 400.00;
            case "Electrical System": return 600.00;
            default: return 250.00;
        }
    }
}
