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

@WebServlet("/book-maintenance")
public class BookMaintenanceServlet extends HttpServlet {

    private MaintenanceDAO maintenanceDAO;
    private AlertDAO alertDAO;

    @Override
    public void init() throws ServletException {
        maintenanceDAO = new MaintenanceDAO();
        alertDAO = new AlertDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            // ✅ Get parameters
            String alertIdStr = request.getParameter("alertId");
            String vehicleIdStr = request.getParameter("vehicleId");
            String taskType = request.getParameter("taskType");
            String scheduleDate = request.getParameter("scheduleDate");

            System.out.println("DEBUG Params: alertId=" + alertIdStr +
                    ", vehicleId=" + vehicleIdStr +
                    ", taskType=" + taskType +
                    ", scheduleDate=" + scheduleDate);

            // ✅ Validate input
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

            // ✅ Ensure date is at least 2 days ahead
            LocalDate selectedDate = LocalDate.parse(scheduleDate);
            LocalDate minAllowedDate = LocalDate.now().plusDays(2);
            if (selectedDate.isBefore(minAllowedDate)) {
                sendJson(out, "error", "Date must be at least 2 days in the future");
                return;
            }

            // ✅ Check if already booked
            if (maintenanceDAO.isDateAlreadyBooked(vehicleId, selectedDate)) {
                sendJson(out, "error", "This vehicle is already booked on that date.");
                return;
            }

            // ✅ Default time is 09:00
            String dateTimeStr = scheduleDate + " 09:00:00";
            Timestamp scheduledTimestamp = Timestamp.valueOf(dateTimeStr);

            // ✅ Check if scheduled time is still in the future
            Timestamp now = Timestamp.valueOf(LocalDateTime.now());
            if (scheduledTimestamp.before(now)) {
                sendJson(out, "error", "Cannot book maintenance in the past");
                return;
            }

            // ✅ Calculate cost based on task
            double cost = getTaskCost(taskType);

            // ✅ Insert maintenance task
            MaintenanceTask task = new MaintenanceTask();
            task.setVehicleId(vehicleId);
            task.setAlertId(alertId);
            task.setDescription(taskType);
            task.setScheduledDatetime(scheduledTimestamp);
            task.setCost(BigDecimal.valueOf(cost));
            task.setCompleted(false);

            int taskId = maintenanceDAO.insertMaintenanceTask(task);
            if (taskId > 0) {
                // ✅ Resolve the alert
                alertDAO.resolveAlert(alertId);

                // ✅ Respond with success
                sendJsonSuccess(out, "success", scheduledTimestamp.toString(), taskId);
            } else {
                sendJson(out, "error", "Failed to save maintenance task");
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendJson(out, "error", "Internal server error: " + e.getMessage());
        }
    }

    private boolean isEmpty(String val) {
        return val == null || val.trim().isEmpty();
    }

    private void sendJson(PrintWriter out, String status, String message) {
        out.print("{\"status\":\"" + status + "\",\"message\":\"" + message + "\"}");
    }

    private void sendJsonSuccess(PrintWriter out, String status, String datetime, int taskId) {
        out.print("{\"status\":\"" + status + "\",\"scheduledDatetime\":\"" + datetime + "\",\"taskId\":" + taskId + "}");
    }

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
