package command;

import business.BreakLogService;
import transferobjects.BreakLog;

/**
 * A command class that encapsulates the logic for starting a new break session
 * using the Command Pattern.
 *
 * This class implements the {@code Command} interface. When executed,
 * it delegates the task of starting a break to the {@code BreakLogService}
 * with the provided {@code BreakLog} data.
 * 
 * This abstraction allows flexible and decoupled execution of break-related actions.
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
public class StartBreakCommand implements Command {
    private final BreakLogService service;
    private final BreakLog log;

    /**
     * Constructs a {@code StartBreakCommand} with the given service and break log.
     *
     * @param service the service responsible for handling break operations
     * @param log the {@code BreakLog} object containing break session details
     */
    public StartBreakCommand(BreakLogService service, BreakLog log) {
        this.service = service;
        this.log = log;
    }

    /**
     * Executes the command to start a new break session using the provided service.
     *
     * @throws Exception if an error occurs while starting the break
     */
    @Override
    public void execute() throws Exception {
        service.startBreak(log);
    }
}
