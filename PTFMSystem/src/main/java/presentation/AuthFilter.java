package presentation;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
import transferobjects.User;

/**
 * Authentication and Authorization filter for the application.
 * 
 * This filter enforces:
 * <ul>
 *   <li>Authentication: Only logged-in users can access restricted pages.</li>
 *   <li>Authorization: Role-based restrictions prevent Operators from accessing Manager pages.</li>
 *   <li>Session safety: Validates that a User object exists in session.</li>
 *   <li>Security: Prevents browser caching of restricted resources.</li>
 * </ul>
 * 
 * Public resources such as login, registration, CSS, and image paths are excluded from filtering.
 * 
 * URL Pattern: {@code /*}
 * 
 * Redirects to:
 * <ul>
 *   <li>{@code login.jsp?error=unauthorized} if not logged in</li>
 *   <li>{@code login.jsp?error=sessionExpired} if session exists but user is null</li>
 *   <li>{@code dashboard.jsp?error=noaccess} if unauthorized access by role</li>
 * </ul>
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    /**
     * Filters incoming requests and enforces authentication and role-based access control.
     *
     * @param request  the incoming {@code ServletRequest}
     * @param response the outgoing {@code ServletResponse}
     * @param chain    the filter chain for continuing request processing
     * @throws IOException      if an I/O error occurs
     * @throws ServletException if a servlet error occurs
     */
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
