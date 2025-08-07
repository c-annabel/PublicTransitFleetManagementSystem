package presentation;

import business.UserService;
import transferobjects.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Handles user registration by processing form input and invoking business logic.
 * 
 * This servlet receives registration data, constructs a User object, and delegates
 * the registration process to {@code UserService}. On success, the user is redirected
 * to the login page with a success flag. On failure, the user is redirected back to
 * the registration page with an error flag.
 * 
 * URL mapping: {@code /register}
 * 
 * Responsibilities:
 * <ul>
 *   <li>Read and sanitize registration form input</li>
 *   <li>Invoke {@code userService.register()}</li>
 *   <li>Redirect based on success or failure</li>
 * </ul>
 * 
 * @author Annabel Cheng
 * @course CST8288 Lab013 Final Project
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final UserService userService = new UserService();

    /**
     * Processes registration form submission.
     *
     * @param request  the {@code HttpServletRequest}
     * @param response the {@code HttpServletResponse}
     * @throws ServletException if servlet encounters an issue
     * @throws IOException      if an input or output error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Handle encoding for special characters
        request.setCharacterEncoding("UTF-8");

        // Read form parameters
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String userType = request.getParameter("userType");

        // Use updated constructor from User.java
        User user = new User(name, email, password, userType);

        try {
            userService.register(user);
            response.sendRedirect("login.jsp?success=1");
        } catch (Exception e) {
            // Registration failed
            response.sendRedirect("register.jsp?error=1");
        }
    }
}
