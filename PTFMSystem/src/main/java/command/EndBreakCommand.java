package command;

import business.BreakLogService;

/**
 * A command class that encapsulates the logic for ending a break session
 * using the Command Pattern.
 *
 * This class implements the {@code Command} interface. When executed,
 * it delegates the task of ending a break to the {@code BreakLogService}
 * for the specified break ID.
 * 
 * This approach decouples the invoker from the execution logic,
 * promoting flexibility and modularity.
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
public class EndBreakCommand implements Command {
    private final BreakLogService service;
    private final int breakId;

    /**
     * Constructs an {@code EndBreakCommand} with the given service and break ID.
     *
     * @param service the business service responsible for ending the break
     * @param breakId the ID of the break session to be ended
     */
    public EndBreakCommand(BreakLogService service, int breakId) {
        this.service = service;
        this.breakId = breakId;
    }

    /**
     * Executes the command to end the break session using the provided service.
     *
     * @throws Exception if an error occurs during the break-ending process
     */
    @Override
    public void execute() throws Exception {
        service.endBreak(breakId);
    }
}
