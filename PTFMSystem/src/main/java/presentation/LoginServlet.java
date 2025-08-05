package presentation;

import business.UserService;
import transferobjects.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Handles user login by validating credentials and creating session.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserService userService = new UserService();

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
