package command;

import dataaccess.MaintenanceDAO;

public class DeleteTaskCommand implements MaintenanceCommand {
    private final int taskId;
    private final MaintenanceDAO dao;

    public DeleteTaskCommand(int taskId, MaintenanceDAO dao) {
        this.taskId = taskId;
        this.dao = dao;
    }

    @Override
    public void execute() {
        dao.deleteTask(taskId); // also needs to exist in your DAO
    }
}
