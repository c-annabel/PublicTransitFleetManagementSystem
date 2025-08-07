package command;

import business.BreakLogService;

/**
 * A command class that encapsulates the logic for pausing a break session
 * using the Command Pattern.
 *
 * This class implements the {@code Command} interface. When executed,
 * it instructs the {@code BreakLogService} to pause the break session
 * identified by the given break ID.
 * 
 * This decouples the action request from the actual business logic implementation.
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
public class PauseBreakCommand implements Command {
    private final BreakLogService service;
    private final int breakId;

    /**
     * Constructs a {@code PauseBreakCommand} with the specified service and break ID.
     *
     * @param service the service responsible for handling break-related operations
     * @param breakId the ID of the break session to pause
     */
    public PauseBreakCommand(BreakLogService service, int breakId) {
        this.service = service;
        this.breakId = breakId;
    }

    /**
     * Executes the command to pause the break session via the {@code BreakLogService}.
     *
     * @throws Exception if an error occurs during the pause operation
     */
    @Override
    public void execute() throws Exception {
        service.pauseBreak(breakId);
    }
}
