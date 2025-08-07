package command;

import dataaccess.MaintenanceDAO;
import transferobjects.MaintenanceTask;

/**
 * A command class that encapsulates the logic for updating
 * a maintenance task in the database using the Command Pattern.
 * 
 * This class implements the {@code MaintenanceCommand} interface.
 * When executed, it triggers the update operation for a specific
 * {@code MaintenanceTask} through the {@code MaintenanceDAO}.
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
public class UpdateTaskCommand implements MaintenanceCommand {
    private final MaintenanceTask task;
    private final MaintenanceDAO dao;

    /**
     * Constructs an UpdateTaskCommand with the specified task and DAO.
     *
     * @param task the maintenance task to be updated
     * @param dao the DAO used to perform the update
     */
    public UpdateTaskCommand(MaintenanceTask task, MaintenanceDAO dao) {
        this.task = task;
        this.dao = dao;
    }

    /**
     * Executes the command to update the maintenance task in the database.
     */
    @Override
    public void execute() {
        dao.updateTask(task); // make sure this method exists in your DAO
    }
}
