package command;

import dataaccess.MaintenanceDAO;
import transferobjects.MaintenanceTask;

public class AddTaskCommand implements MaintenanceCommand {
    private final MaintenanceTask task;
    private final MaintenanceDAO dao;

    public AddTaskCommand(MaintenanceTask task, MaintenanceDAO dao) {
        this.task = task;
        this.dao = dao;
    }

    @Override
    public void execute() {
        dao.insertMaintenanceTask(task);
    }
}
