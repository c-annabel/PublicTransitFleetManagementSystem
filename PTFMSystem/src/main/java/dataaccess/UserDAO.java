package dataaccess;

import transferobjects.User;
import java.sql.*;

/**
 * Data Access Object (DAO) for the User entity.
 * Handles all database operations related to the Users table,
 * including registration, authentication, and email verification.
 * 
 * @author Annabel Cheng
 * @comment Course 25S CST8288 Lab013 Final Project
 */
public class UserDAO {

    private final DataSource dataSource;

    /**
     * Constructs a UserDAO and initializes the DataSource using the Singleton pattern.
     */
    public UserDAO() {
        dataSource = DataSource.getInstance();
    }

    /**
     * Registers a new user by inserting their information into the Users table.
     *
     * @param user The User object containing the user's name, email, password, and user type.
     * @throws SQLException if a database access error occurs.
     */
    public void registerUser(User user) throws SQLException {
        String sql = "INSERT INTO Users (name, email, password, user_type) VALUES (?, ?, ?, ?)";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getUserType());
            ps.executeUpdate();
        }
    }

    /**
     * Authenticates a user by checking their email and password against the database.
     *
     * @param email    The email address of the user.
     * @param password The password of the user (assumes plain text for demo purposes).
     * @return A User object if the credentials are correct; null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public User authenticate(String email, String password) throws SQLException {
        String sql = "SELECT * FROM Users WHERE email = ? AND password = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setUserType(rs.getString("user_type"));
                    return user;
                }
            }
        }
        return null;
    }

    /**
     * Checks if the given email address is already registered in the Users table.
     *
     * @param email The email address to check.
     * @return true if the email exists in the database; false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT user_id FROM Users WHERE email = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
