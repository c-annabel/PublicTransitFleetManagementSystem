package business;

import dataaccess.UserDAO;
import transferobjects.User;

/**
 * Service layer for user-related operations.
 * Handles validation and delegates persistence to UserDAO.
 */
public class UserService {
    private final UserDAO userDAO;

    // Dependency Injection (can be replaced with frameworks later)
    public UserService() {
        this.userDAO = new UserDAO();
    }

    // For future testability, allow passing a mock DAO
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Registers a new user after performing validations.
     *
     * @param user User object with registration details.
     * @throws Exception if validation fails or duplicate email found.
     */
    public void register(User user) throws Exception {
        // Validation for empty fields
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new Exception("Name cannot be empty.");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new Exception("Email cannot be empty.");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new Exception("Password cannot be empty.");
        }
        if (user.getUserType() == null || user.getUserType().trim().isEmpty()) {
            throw new Exception("User type is required.");
        }

        // Check for duplicate email
        if (userDAO.emailExists(user.getEmail())) {
            throw new Exception("Email already exists. Please use another email.");
        }

        // Password hashing placeholder (demo uses plain text)
        // TODO: For production, use BCrypt:
        // user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

        // Register user
        userDAO.registerUser(user);
    }

    /**
     * Authenticates a user by email and password.
     *
     * @param email User email.
     * @param password User password.
     * @return User object if authentication is successful.
     * @throws Exception if input is invalid or credentials are incorrect.
     */
    public User login(String email, String password) throws Exception {
        // Validate inputs
        if (email == null || email.trim().isEmpty()) {
            throw new Exception("Email cannot be empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new Exception("Password cannot be empty.");
        }

        // Authenticate user
        User user = userDAO.authenticate(email, password);
        if (user == null) {
            throw new Exception("Invalid email or password.");
        }
        return user;
    }
}
