package command;

import business.BreakLogService;

public class PauseBreakCommand implements Command {
    private final BreakLogService service;
    private final int breakId;

    public PauseBreakCommand(BreakLogService service, int breakId) {
        this.service = service;
        this.breakId = breakId;
    }

    @Override
    public void execute() throws Exception {
        service.pauseBreak(breakId);
    }
}
