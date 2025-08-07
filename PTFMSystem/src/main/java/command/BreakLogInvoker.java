package command;

/**
 * Invoker class for executing {@code Command} objects related to break logs or other actions.
 * 
 * This class encapsulates the call to the {@code execute()} method of a {@code Command}
 * and allows for flexible command execution using the Command Pattern.
 * 
 * It is designed to decouple the caller from the command execution logic.
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
public class BreakLogInvoker {

    /**
     * Executes the given command.
     *
     * @param command the command to be executed
     * @throws Exception if the command execution fails
     */
    public void executeCommand(Command command) throws Exception {
        command.execute();
    }
}
