package business;

import dataaccess.BreakLogDAO;
import transferobjects.BreakLog;
import java.util.List;

public class BreakLogService {
    private final BreakLogDAO dao = new BreakLogDAO();

    public void startBreak(BreakLog log) throws Exception {
        dao.startBreak(log);
    }
    
    public boolean checkBreakExists(int breakId) throws Exception {
    return dao.breakExists(breakId);
}

    public void pauseBreak(int breakId) throws Exception {
        dao.pauseBreak(breakId);
    }

    public void endBreak(int breakId) throws Exception {
        dao.endBreak(breakId);
    }

    public List<BreakLog> getBreakLogs(int operatorId) throws Exception {
        return dao.getBreakLogsByOperator(operatorId);
    }
}
