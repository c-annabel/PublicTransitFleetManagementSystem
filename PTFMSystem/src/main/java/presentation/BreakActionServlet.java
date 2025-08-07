package presentation;

import business.BreakLogService;
import command.*;
import transferobjects.BreakLog;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Servlet responsible for handling break-related actions (start, pause, end) for operators.
 * 
 * This servlet processes POST requests to start, pause, or end a break using the Command pattern.
 * It validates input parameters and delegates the action to appropriate command implementations.
 * 
 * URL mapping: {@code /breakAction}
 * 
 * Expected parameters:
 * <ul>
 *   <li>{@code action} – one of: "start", "pause", "end"</li>
 *   <li>{@code operatorId} – the ID of the operator performing the break</li>
 *   <li>{@code vehicleId} – required only when starting a break</li>
 *   <li>{@code breakId} – required when pausing or ending a break</li>
 * </ul>
 * 
 * Redirects to {@code breakLog.jsp} with appropriate status messages.
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
@WebServlet("/breakAction")
public class BreakActionServlet extends HttpServlet {
    private final BreakLogService service = new BreakLogService();
    private final BreakLogInvoker invoker = new BreakLogInvoker();

    /**
     * Handles POST requests for break actions such as start, pause, and end.
     *
     * @param request  the {@code HttpServletRequest} containing user input
     * @param response the {@code HttpServletResponse} used to redirect with status
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an input or output error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String operatorParam = request.getParameter("operatorId");
        String vehicleParam = request.getParameter("vehicleId");
        String breakIdParam = request.getParameter("breakId");

        try {
            // Validate required operator ID and action
            if (action == null || operatorParam == null || operatorParam.isEmpty()) {
                response.sendRedirect("breakLog.jsp?msg=Invalid+request+parameters");
                return;
            }

            int operatorId = Integer.parseInt(operatorParam);

            // Start Break
            if ("start".equals(action)) {
                if (vehicleParam == null || vehicleParam.isEmpty()) {
                    response.sendRedirect("breakLog.jsp?msg=Vehicle+ID+is+required+to+start+break");
                    return;
                }
                int vehicleId = Integer.parseInt(vehicleParam);
                BreakLog log = new BreakLog();
                log.setOperatorId(operatorId);
                log.setVehicleId(vehicleId);

                invoker.executeCommand(new StartBreakCommand(service, log));
                response.sendRedirect("breakLog.jsp?msg=Break+started+successfully");

            // Pause or End Break
            } else if ("pause".equals(action) || "end".equals(action)) {
                if (breakIdParam == null || breakIdParam.isEmpty()) {
                    response.sendRedirect("breakLog.jsp?msg=Break+ID+is+required&type=error");
                    return;
                }
                int breakId = Integer.parseInt(breakIdParam);

                boolean exists = service.checkBreakExists(breakId);
                if (!exists) {
                    response.sendRedirect("breakLog.jsp?msg=Break+ID+not+found&type=error");
                    return;
                }

                if ("pause".equals(action)) {
                    invoker.executeCommand(new PauseBreakCommand(service, breakId));
                    response.sendRedirect("breakLog.jsp?msg=Break+paused+successfully&type=success");
                } else {
                    invoker.executeCommand(new EndBreakCommand(service, breakId));
                    response.sendRedirect("breakLog.jsp?msg=Break+ended+successfully&type=success");
                }

            // Unknown Action
            } else {
                response.sendRedirect("breakLog.jsp?msg=Unknown+action&type=error");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect("breakLog.jsp?msg=Invalid+number+format&type=error");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("breakLog.jsp?msg=Error+processing+request&type=error");
        }
    }
}
