package command;

import dataaccess.MaintenanceDAO;
import transferobjects.MaintenanceTask;

/**
 * A command class that encapsulates the logic for adding a new maintenance task
 * using the Command Pattern.
 *
 * This class implements the {@code MaintenanceCommand} interface. When executed,
 * it inserts a new {@code MaintenanceTask} into the database via {@code MaintenanceDAO}.
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
public class AddTaskCommand implements MaintenanceCommand {
    private final MaintenanceTask task;
    private final MaintenanceDAO dao;

    /**
     * Constructs an {@code AddTaskCommand} with the given maintenance task and DAO.
     *
     * @param task the maintenance task to be added
     * @param dao the DAO responsible for database operations
     */
    public AddTaskCommand(MaintenanceTask task, MaintenanceDAO dao) {
        this.task = task;
        this.dao = dao;
    }

    /**
     * Executes the command to insert the maintenance task into the database.
     */
    @Override
    public void execute() {
        dao.insertMaintenanceTask(task);
    }
}
