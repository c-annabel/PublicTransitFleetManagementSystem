package transferobjects;

/**
 * User.java - Transfer Object for authenticated user data.
 *
 * This class is part of the CST8288 Final Project.
 * It stores user attributes including ID, name, email, password, and role (Manager/Operator).
 *
 * Central to access control, authentication, and session management.
 * 
 * @author Annabel Cheng
 */

public class User {
    private int userId;
    private String name;
    private String email;
    private String password;
    private String userType; // Manager or Operator
    
    public User() {}
    
    public User(String name, String email, String password, String userType) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.userType = userType;
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
    
    @Override
    public String toString() {
    return "User{" +
           "userId=" + userId +
           ", name='" + name + '\'' +
           ", email='" + email + '\'' +
           ", userType='" + userType + '\'' +
           '}';
    }
}
