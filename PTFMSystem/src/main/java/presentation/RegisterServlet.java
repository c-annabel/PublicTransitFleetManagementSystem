package presentation;

import business.UserService;
import transferobjects.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final UserService userService = new UserService();

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
        } catch (Exception e) { // Log for debugging
            // Log for debugging
            response.sendRedirect("register.jsp?error=1");
        }
    }
}
