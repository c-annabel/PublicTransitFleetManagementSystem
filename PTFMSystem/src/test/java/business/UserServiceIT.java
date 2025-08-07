package business;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import transferobjects.User;


public class UserServiceIT {
    
    public UserServiceIT() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of register method, of class UserService.
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
        
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of login method, of class UserService.
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
        
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
    
}
