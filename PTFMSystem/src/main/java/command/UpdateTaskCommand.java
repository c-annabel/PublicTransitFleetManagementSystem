package command;

import dataaccess.MaintenanceDAO;
import transferobjects.MaintenanceTask;

public class UpdateTaskCommand implements MaintenanceCommand {
    private final MaintenanceTask task;
    private final MaintenanceDAO dao;

    public UpdateTaskCommand(MaintenanceTask task, MaintenanceDAO dao) {
        this.task = task;
        this.dao = dao;
    }

    @Override
    public void execute() {
        dao.updateTask(task); // make sure this method exists in your DAO
    }
}
