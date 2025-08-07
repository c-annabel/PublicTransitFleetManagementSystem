package command;

/**
 * Interface for maintenance-related commands using the Command Pattern.
 * 
 * Classes implementing this interface encapsulate specific actions
 * related to maintenance tasks (e.g., add, update, delete), providing
 * a uniform method for execution.
 * 
 * This promotes loose coupling between the requester and the executor of a task.
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
public interface MaintenanceCommand {

    /**
     * Executes the maintenance-related command.
     */
    void execute();
}
