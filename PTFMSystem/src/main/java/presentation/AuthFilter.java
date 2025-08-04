package presentation;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
import transferobjects.User;

/**
 * Authentication and Authorization filter for the application.
 * Ensures only logged-in users can access restricted pages,
 * and applies role-based restrictions for Managers and Operators.
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String uri = req.getRequestURI();
        boolean loggedIn = (session != null && session.getAttribute("user") != null);

        // Public resources (no authentication needed)
        boolean isPublicResource = uri.endsWith("login.jsp") ||
                                   uri.endsWith("register.jsp") ||
                                   uri.contains("/login") ||
                                   uri.contains("/register") ||
                                   uri.contains("/css") ||
                                   uri.contains("/images");

        if (isPublicResource) {
            chain.doFilter(request, response);
            return;
        }

        if (!loggedIn) {
            // If user is not logged in, redirect to login page
            res.sendRedirect("login.jsp?error=unauthorized");
            return;
        }

        // Role-based restriction logic
        User user = (User) session.getAttribute("user");
        String userType = user.getUserType();

        // Example: Managers can access everything, Operators restricted
        if ("Operator".equalsIgnoreCase(userType) &&
            (uri.contains("vehicleManagement.jsp") || uri.contains("maintenance.jsp"))) {
            res.sendRedirect("dashboard.jsp?error=noaccess");
            return;
        }

        // Prevent browser caching of restricted pages (security after logout)
        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        res.setHeader("Pragma", "no-cache");
        res.setDateHeader("Expires", 0);

        // Continue filter chain
        chain.doFilter(request, response);
    }
}
