package presentation;

import business.BreakLogService;
import command.*;
import transferobjects.BreakLog;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/breakAction")
public class BreakActionServlet extends HttpServlet {
    private final BreakLogService service = new BreakLogService();
    private final BreakLogInvoker invoker = new BreakLogInvoker();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String operatorParam = request.getParameter("operatorId");
        String vehicleParam = request.getParameter("vehicleId");
        String breakIdParam = request.getParameter("breakId");

        try {
            if (action == null || operatorParam == null || operatorParam.isEmpty()) {
                response.sendRedirect("breakLog.jsp?msg=Invalid+request+parameters");
                return;
            }

            int operatorId = Integer.parseInt(operatorParam);

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

            // PAUSE or END BREAK with validation
            } else if ("pause".equals(action) || "end".equals(action)) {
                if (breakIdParam == null || breakIdParam.isEmpty()) {
                    response.sendRedirect("breakLog.jsp?msg=Break+ID+is+required&type=error");
                    return;
                }
                int breakId = Integer.parseInt(breakIdParam);

                // ✅ Check if Break ID exists
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