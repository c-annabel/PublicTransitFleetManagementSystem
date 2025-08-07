
package dataaccess;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import transferobjects.MaintenanceTask;

/**
 *
 * @author c-ann
 */
public class MaintenanceDAOIT {
    
    public MaintenanceDAOIT() {
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
     * Test of hasScheduledTask method, of class MaintenanceDAO.
     */
    @Test

    public void testHasScheduledTask() {
        System.out.println("hasScheduledTask");
        int vehicleId = 12;      /// Test if Vehicle ID 8 has a scheduled Task. 
        MaintenanceDAO instance = new MaintenanceDAO();
        boolean expResult = false;
        boolean result = instance.hasScheduledTask(vehicleId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of insertMaintenanceTask method, of class MaintenanceDAO.
     */
    @Test
    public void testInsertMaintenanceTask() {
        System.out.println("insertMaintenanceTask");
        MaintenanceTask task = new MaintenanceTask();
        task.setVehicleId(8); // vehicle exists
        task.setScheduledDatetime(Timestamp.valueOf(LocalDate.of(2025, 9, 30).atStartOfDay())); // safe future date
        task.setDescription("JUnit test task");
        task.setCost(new BigDecimal("320.00"));
        task.setCompleted(false);

        MaintenanceDAO instance = new MaintenanceDAO();
        int expResult = -1;
        int result = instance.insertMaintenanceTask(task);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of hasScheduledTaskForVehicle method, of class MaintenanceDAO.
     */
    @Test
    public void testHasScheduledTaskForVehicle() {
        System.out.println("hasScheduledTaskForVehicle");
        int vehicleId = 10;   //Not existed
        MaintenanceDAO instance = new MaintenanceDAO();
        boolean expResult = false;
        boolean result = instance.hasScheduledTaskForVehicle(vehicleId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of isDateAlreadyBooked method, of class MaintenanceDAO.
     */
    @Test
    public void testIsDateAlreadyBooked() {
        System.out.println("isDateAlreadyBooked");
        int vehicleId = 3;
        LocalDate selectedDate = LocalDate.of(2025,8,01);
        MaintenanceDAO instance = new MaintenanceDAO();
        boolean expResult = false;
        boolean result = instance.isDateAlreadyBooked(vehicleId, selectedDate);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of isDateAlreadyTaken method, of class MaintenanceDAO.
     */
    @Test
    public void testIsDateAlreadyTaken() {
        System.out.println("isDateAlreadyTaken");
        LocalDate selectedDate = LocalDate.of(2025,8,9);
        MaintenanceDAO instance = new MaintenanceDAO();
        boolean expResult = true;
        boolean result = instance.isDateAlreadyTaken(selectedDate);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllBookedDates method, of class MaintenanceDAO.
     */
    @Test
    public void testGetAllBookedDates() {
        System.out.println("getAllBookedDates");
        MaintenanceDAO instance = new MaintenanceDAO();
        List<LocalDate> expResult = null;
        List<LocalDate> result = instance.getAllBookedDates();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBookedDatesForVehicle method, of class MaintenanceDAO.
     */
    @Test
    public void testGetBookedDatesForVehicle() {
        System.out.println("getBookedDatesForVehicle");
        int vehicleId = 7;  //id=7
        MaintenanceDAO instance = new MaintenanceDAO();
        List<LocalDate> expResult = Collections.singletonList(LocalDate.of(2025,8,9));
        List<LocalDate> result = instance.getBookedDatesForVehicle(vehicleId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of updateTask method, of class MaintenanceDAO.
     */
    @Test
    public void testUpdateTask() {
        System.out.println("updateTask");
        MaintenanceTask task = new MaintenanceTask();
        task.setTaskId(5); // ✅ This task must exist in your database
        task.setVehicleId(5); // Same vehicle ID as existing record
        task.setScheduledDatetime(Timestamp.valueOf(LocalDate.of(2025, 8, 2).atStartOfDay()));
        task.setDescription("Updated via JUnit");
        task.setCompleted(true);
        task.setCost(new BigDecimal("1200.00"));
        MaintenanceDAO instance = new MaintenanceDAO();
        boolean expResult = true;
        boolean result = instance.updateTask(task);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteTask method, of class MaintenanceDAO.
     */
    @Test
    public void testDeleteTask() {
        System.out.println("deleteTask");
        int taskId = 16;
        MaintenanceDAO instance = new MaintenanceDAO();
        boolean expResult = false;
        boolean result = instance.deleteTask(taskId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllMaintenanceTasks method, of class MaintenanceDAO.
     */
    @Test
    public void testGetAllMaintenanceTasks() {
        System.out.println("getAllMaintenanceTasks");
        MaintenanceDAO instance = new MaintenanceDAO();
        List<MaintenanceTask> expResult = null;
        List<MaintenanceTask> result = instance.getAllMaintenanceTasks();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTaskById method, of class MaintenanceDAO.
     */
    @Test
    public void testGetTaskById() {
        System.out.println("getTaskById");
        int taskId = 10;
        MaintenanceDAO instance = new MaintenanceDAO();
        MaintenanceTask expResult = null;
        MaintenanceTask result = instance.getTaskById(taskId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of isDateAlreadyBookedExcludingTask method, of class MaintenanceDAO.
     */
    @Test
    public void testIsDateAlreadyBookedExcludingTask() {
        System.out.println("isDateAlreadyBookedExcludingTask");
        int vehicleId = 8;
        LocalDate date = LocalDate.of(2025,8,13);
        int taskId = 15;
        MaintenanceDAO instance = new MaintenanceDAO();
        boolean expResult = false;
        boolean result = instance.isDateAlreadyBookedExcludingTask(vehicleId, date, taskId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
    
}
