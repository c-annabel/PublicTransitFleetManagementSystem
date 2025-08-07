package command;

import dataaccess.MaintenanceDAO;

/**
 * A command class that encapsulates the logic for deleting a maintenance task
 * using the Command Pattern.
 *
 * This class implements the {@code MaintenanceCommand} interface. When executed,
 * it removes the maintenance task identified by {@code taskId} from the database
 * using the provided {@code MaintenanceDAO}.
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
public class DeleteTaskCommand implements MaintenanceCommand {
    private final int taskId;
    private final MaintenanceDAO dao;

    /**
     * Constructs a {@code DeleteTaskCommand} with the specified task ID and DAO.
     *
     * @param taskId the ID of the maintenance task to be deleted
     * @param dao the DAO responsible for executing the deletion
     */
    public DeleteTaskCommand(int taskId, MaintenanceDAO dao) {
        this.taskId = taskId;
        this.dao = dao;
    }

    /**
     * Executes the command to delete the specified maintenance task from the database.
     */
    @Override
    public void execute() {
        dao.deleteTask(taskId); // also needs to exist in your DAO
    }
}
