package presentation;

import business.UserService;
import transferobjects.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Handles user login requests.
 *
 * This servlet receives POST requests from the login form, validates user credentials using
 * {@link UserService}, and creates an authenticated session if login is successful.
 *
 * If login fails, the user is redirected back to the login page with an error parameter.
 *
 * URL mapping: {@code /login}
 *
 * Expected parameters:
 * <ul>
 *   <li>email - The user's login email</li>
 *   <li>password - The user's password</li>
 * </ul>
 *
 * Session attributes:
 * <ul>
 *   <li>user - The authenticated {@link User} object</li>
 * </ul>
 * 
 * Redirects:
 * <ul>
 *   <li>dashboard.jsp - On successful login</li>
 *   <li>login.jsp?error=1 - On failed login</li>
 * </ul>
 * 
 * @author Annabel Cheng
 * @course CST8288 Lab013 Final Project
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserService userService = new UserService();

    /**
     * Processes POST login requests, authenticates the user, and sets session attributes.
     *
     * @param request  the {@code HttpServletRequest} containing email and password
     * @param response the {@code HttpServletResponse} for redirection
     * @throws ServletException if servlet-specific error occurs
     * @throws IOException      if an I/O error occurs during redirection
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            User user = userService.login(email, password);

            // Create a new session for the authenticated user
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            // Optional: Set session timeout (in seconds)
            session.setMaxInactiveInterval(30 * 60); // 30 minutes

            // Redirect to dashboard
            response.sendRedirect("dashboard.jsp");

        } catch (Exception e) {
            // Redirect to login page with error flag
            response.sendRedirect("login.jsp?error=1");
        }
    }
}
