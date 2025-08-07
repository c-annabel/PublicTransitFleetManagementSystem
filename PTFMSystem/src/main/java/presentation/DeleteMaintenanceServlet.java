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

@WebServlet("/deleteMaintenance")
public class DeleteMaintenanceServlet extends HttpServlet {
    private MaintenanceDAO dao;

    @Override
    public void init() {
        dao = new MaintenanceDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int taskId = Integer.parseInt(request.getParameter("taskId"));
            MaintenanceTask task = dao.getTaskById(taskId);
            if (task == null || task.isCompleted()) {
                response.sendRedirect("maintenanceSchedule.jsp?error=completed");
                return;
            }

            MaintenanceCommand command = new DeleteTaskCommand(taskId, dao);
            command.execute();

            response.sendRedirect("maintenanceSchedule.jsp?success=deleted");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("maintenanceSchedule.jsp?error=1");
        }
    }
}
