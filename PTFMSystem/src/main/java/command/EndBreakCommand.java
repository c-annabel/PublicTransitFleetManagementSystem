package command;

import business.BreakLogService;

public class EndBreakCommand implements Command {
    private final BreakLogService service;
    private final int breakId;

    public EndBreakCommand(BreakLogService service, int breakId) {
        this.service = service;
        this.breakId = breakId;
    }

    @Override
    public void execute() throws Exception {
        service.endBreak(breakId);
    }
}
