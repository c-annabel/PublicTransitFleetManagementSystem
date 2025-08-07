package presentation;

import command.DeleteTaskCommand;
import command.MaintenanceCommand;
import dataaccess.MaintenanceDAO;
import transferobjects.MaintenanceTask;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet responsible for deleting a maintenance task.
 * 
 * Ensures only non-completed tasks are deletable.
 * Utilizes the Command Pattern to encapsulate deletion logic.
 * 
 * Redirects to maintenanceSchedule.jsp with success or error messages.
 * 
 * URL: /deleteMaintenance
 * 
 * Expected request param: taskId
 * 
 * @author Annabel Cheng
 * @course CST8288 Lab013 Final Project
 */
@WebServlet("/deleteMaintenance")
public class DeleteMaintenanceServlet extends HttpServlet {
    private MaintenanceDAO dao;

    /**
     * Initializes DAO for maintenance operations.
     */
    @Override
    public void init() {
        dao = new MaintenanceDAO();
    }

    /**
     * Handles POST request to delete a maintenance task.
     *
     * @param request  the HttpServletRequest containing taskId
     * @param response the HttpServletResponse used for redirection
     * @throws IOException on redirect failure or internal errors
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String taskIdParam = request.getParameter("taskId");

        if (taskIdParam == null || taskIdParam.isEmpty()) {
            response.sendRedirect("maintenanceSchedule.jsp?error=missingTaskId");
            return;
        }

        try {
            int taskId = Integer.parseInt(taskIdParam);
            MaintenanceTask task = dao.getTaskById(taskId);

            if (task == null) {
                response.sendRedirect("maintenanceSchedule.jsp?error=taskNotFound");
                return;
            }

            if (task.isCompleted()) {
                response.sendRedirect("maintenanceSchedule.jsp?error=completed");
                return;
            }

            MaintenanceCommand command = new DeleteTaskCommand(taskId, dao);
            command.execute();

            response.sendRedirect("maintenanceSchedule.jsp?success=deleted");

        } catch (NumberFormatException e) {
            response.sendRedirect("maintenanceSchedule.jsp?error=invalidTaskId");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("maintenanceSchedule.jsp?error=serverError");
        }
    }
}
