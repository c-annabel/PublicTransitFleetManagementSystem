package business;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import transferobjects.User;

/**
 * Integration test class for {@code UserService}.
 * 
 * This class verifies the correctness of the user registration and login logic
 * by simulating real-world usage scenarios and asserting expected behavior.
 * 
 * It includes setup and teardown hooks for test lifecycle management using JUnit 5.
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
public class UserServiceIT {

    /**
     * Default constructor.
     */
    public UserServiceIT() {
    }

    /**
     * Executed once before all test methods in this class.
     */
    @BeforeAll
    public static void setUpClass() {
    }

    /**
     * Executed once after all test methods in this class.
     */
    @AfterAll
    public static void tearDownClass() {
    }

    /**
     * Executed before each test method.
     */
    @BeforeEach
    public void setUp() {
    }

    /**
     * Executed after each test method.
     */
    @AfterEach
    public void tearDown() {
    }

    /**
     * Tests the {@code register} method of {@code UserService}.
     * <p>
     * This test verifies that an exception is thrown when an invalid user is registered,
     * specifically when the name is empty.
     *
     * @throws Exception if an unexpected error occurs during execution
     */
    @Test
    public void testRegister() throws Exception {
        System.out.println("register");
        User user = new User();
        user.setName("");
        user.setEmail("test@test.com");
        user.setPassword(" ");
        user.setUserType("Operator");

        UserService instance = new UserService();

        Exception exception = assertThrows(Exception.class, () -> {
            instance.register(user);
        });

        assertEquals("Name cannot be empty.", exception.getMessage());
    }

    /**
     * Tests the {@code login} method of {@code UserService}.
     * <p>
     * This test simulates a successful login with known credentials
     * and compares the returned {@code User} object against expected values.
     *
     * @throws Exception if an error occurs during login
     */
    @Test
    public void testLogin() throws Exception {
        System.out.println("login");
        String email = "tm@algonquin.com";
        String password = "cst8288";
        UserService instance = new UserService();

        // Expected User (manually created for comparison)
        User expResult = new User();
        expResult.setUserId(13);
        expResult.setName("Transit Manager");
        expResult.setEmail("tm@algonquin.com");
        expResult.setUserType("Manager");

        // Actual result from login()
        User result = instance.login(email, password);

        // Compare individual fields (safer than comparing objects directly)
        assertNotNull(result);
        assertEquals(expResult.getUserId(), result.getUserId());
        assertEquals(expResult.getName(), result.getName());
        assertEquals(expResult.getEmail(), result.getEmail());
        assertEquals(expResult.getUserType(), result.getUserType());
    }
}
