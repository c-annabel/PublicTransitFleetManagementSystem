package command;

import business.BreakLogService;
import transferobjects.BreakLog;

public class StartBreakCommand implements Command {
    private final BreakLogService service;
    private final BreakLog log;

    public StartBreakCommand(BreakLogService service, BreakLog log) {
        this.service = service;
        this.log = log;
    }

    @Override
    public void execute() throws Exception {
        service.startBreak(log);
    }
}
