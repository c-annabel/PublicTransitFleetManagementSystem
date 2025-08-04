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

        // Get session without creating new
        HttpSession session = req.getSession(false);

        String uri = req.getRequestURI();
        boolean loggedIn = (session != null && session.getAttribute("user") != null);

        // Public resources allowed without login
        boolean isPublicResource = uri.contains("login.jsp") ||
                                   uri.contains("register.jsp") ||
                                   uri.contains("/login") ||
                                   uri.contains("/register") ||
                                   uri.contains("/css") ||
                                   uri.contains("/images") ||
                                   uri.endsWith("/");

        if (isPublicResource) {
            chain.doFilter(request, response);
            return;
        }

        // If not logged in, redirect to login page
        if (!loggedIn) {
            res.sendRedirect(req.getContextPath() + "/login.jsp?error=unauthorized");
            return;
        }

        // Safe check for user attribute
        User user = (User) session.getAttribute("user");
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/login.jsp?error=sessionExpired");
            return;
        }

        String userType = user.getUserType();

        // Role-based restriction
        if ("Operator".equalsIgnoreCase(userType) &&
            (uri.contains("vehicleManagement.jsp") || uri.contains("maintenance.jsp"))) {
            res.sendRedirect(req.getContextPath() + "/dashboard.jsp?error=noaccess");
            return;
        }

        // Prevent browser caching of restricted pages
        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        res.setHeader("Pragma", "no-cache");
        res.setDateHeader("Expires", 0);

        chain.doFilter(request, response);
    }
}
