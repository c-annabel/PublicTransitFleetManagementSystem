package business;

import dataaccess.BreakLogDAO;
import transferobjects.BreakLog;
import java.util.List;

/**
 * Provides business logic for managing operator break logs, 
 * including start, pause, end, and retrieval operations.
 * 
 * @author Annabel Cheng
 * @version Course 25S CST8288 Lab013 Final Project
 */
public class BreakLogService {

    private final BreakLogDAO dao = new BreakLogDAO();

    /**
     * Starts a new break for the given operator.
     *
     * @param log the {@link BreakLog} object containing break details
     * @throws Exception if there is an error starting the break
     */
    public void startBreak(BreakLog log) throws Exception {
        dao.startBreak(log);
    }

    /**
     * Checks whether a break entry exists with the specified break ID.
     *
     * @param breakId the ID of the break to check
     * @return true if the break exists, false otherwise
     * @throws Exception if there is an error accessing the data
     */
    public boolean checkBreakExists(int breakId) throws Exception {
        return dao.breakExists(breakId);
    }

    /**
     * Pauses the break with the given break ID.
     *
     * @param breakId the ID of the break to pause
     * @throws Exception if there is an error pausing the break
     */
    public void pauseBreak(int breakId) throws Exception {
        dao.pauseBreak(breakId);
    }

    /**
     * Ends the break with the specified break ID.
     *
     * @param breakId the ID of the break to end
     * @throws Exception if there is an error ending the break
     */
    public void endBreak(int breakId) throws Exception {
        dao.endBreak(breakId);
    }

    /**
     * Retrieves the list of break logs for the specified operator.
     *
     * @param operatorId the ID of the operator
     * @return a list of {@link BreakLog} entries for the operator
     * @throws Exception if there is an error retrieving the logs
     */
    public List<BreakLog> getBreakLogs(int operatorId) throws Exception {
        return dao.getBreakLogsByOperator(operatorId);
    }
}
