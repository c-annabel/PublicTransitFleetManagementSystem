package business;

import dataaccess.UserDAO;
import transferobjects.User;

/**
 * Service layer for user-related operations such as registration and authentication.
 * Handles input validation and delegates persistence tasks to {@link UserDAO}.
 * 
 * @author Annabel Cheng
 * Course 25S CST8288 Lab013 Final Project
 */
public class UserService {
    private final UserDAO userDAO;

    /**
     * Default constructor that initializes the service with a new {@link UserDAO} instance.
     */
    public UserService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Constructor that allows injection of a custom {@link UserDAO} instance.
     * Useful for testing or mocking.
     *
     * @param userDAO the {@code UserDAO} instance to be used by this service
     */
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Registers a new user after performing validations on the input fields.
     * Ensures that name, email, password, and user type are not empty,
     * and that the email does not already exist in the database.
     *
     * @param user User object containing the registration details.
     * @throws Exception if validation fails or the email is already registered.
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
     * Authenticates a user using the provided email and password.
     * Validates that both fields are not empty before querying the database.
     *
     * @param email the email address entered by the user.
     * @param password the password entered by the user.
     * @return the {@code User} object if authentication is successful.
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
