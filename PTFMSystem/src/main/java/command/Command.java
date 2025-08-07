package command;

/**
 * Command interface used in the Command design pattern.
 * 
 * This interface defines a standard method {@code execute()} that all command
 * implementations must provide. It allows encapsulation of a request as an object,
 * enabling parameterization and queuing of requests, and supporting operations like undo/redo.
 * 
 * Commonly used to decouple the object that invokes an operation from the one that knows how to perform it.
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
public interface Command {

    /**
     * Executes the encapsulated operation.
     *
     * @throws Exception if execution fails
     */
    void execute() throws Exception;

}
