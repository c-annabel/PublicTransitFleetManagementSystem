package dataaccess;

import transferobjects.User;
import java.sql.*;

/**
 * Data Access Object for User entity.
 * Handles CRUD operations for the Users table.
 */
public class UserDAO {

    private final DataSource dataSource;

    /**
     * Constructor initializes DataSource using Singleton instance.
     */
    public UserDAO() {
        dataSource = DataSource.getInstance();
    }

    /**
     * Registers a new user in the database.
     *
     * @param user User object containing name, email, password, and userType.
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
     * Authenticates a user by email and password.
     *
     * @param email    User's email.
     * @param password User's password (plain text for demo).
     * @return User object if credentials are correct; null otherwise.
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
     * Checks if an email is already registered.
     *
     * @param email Email to check.
     * @return true if email exists; false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT user_id FROM Users WHERE email = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // true if record found
            }
        }
    }
}