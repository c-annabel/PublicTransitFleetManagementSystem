package presentation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Handles user logout by invalidating the current session and redirecting to the login page.
 *
 * This servlet ensures that after logout, users cannot navigate back to protected pages
 * using the browser's back button by clearing cache headers.
 *
 * URL mapping: {@code /logout}
 * 
 * Behavior:
 * <ul>
 *   <li>Invalidates the existing session if present</li>
 *   <li>Clears client-side cache to prevent re-access to authenticated pages</li>
 *   <li>Redirects to {@code login.jsp}</li>
 * </ul>
 * 
 * Handles both GET and POST requests (POST is internally delegated to GET).
 * 
 * @author Annabel Cheng
 * @course CST8288 Lab013 Final Project
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    /**
     * Processes GET logout requests by ending the session and redirecting to login.
     *
     * @param request  the {@code HttpServletRequest}
     * @param response the {@code HttpServletResponse}
     * @throws ServletException if servlet-specific error occurs
     * @throws IOException      if an I/O error occurs during redirection
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Invalidate session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Prevent caching to avoid using back button after logout
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        // Redirect to login page
        response.sendRedirect("login.jsp");
    }

    /**
     * Delegates POST logout requests to {@code doGet}.
     *
     * @param request  the {@code HttpServletRequest}
     * @param response the {@code HttpServletResponse}
     * @throws ServletException if servlet-specific error occurs
     * @throws IOException      if an I/O error occurs during redirection
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
