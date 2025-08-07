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
 * Integration test class for {@code MaintenanceDAO}.
 * 
 * This class verifies that database operations related to maintenance tasks
 * are correctly handled by the DAO layer. It tests methods for insert, update,
 * delete, and various lookup operations related to maintenance scheduling.
 * 
 * All tests use JUnit 5 and assume that a consistent test database is available.
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
public class MaintenanceDAOIT {

    public MaintenanceDAOIT() {
    }

    /**
     * Runs once before all test cases.
     */
    @BeforeAll
    public static void setUpClass() {
    }

    /**
     * Runs once after all test cases.
     */
    @AfterAll
    public static void tearDownClass() {
    }

    /**
     * Runs before each test case.
     */
    @BeforeEach
    public void setUp() {
    }

    /**
     * Runs after each test case.
     */
    @AfterEach
    public void tearDown() {
    }

    /**
     * Tests {@code hasScheduledTask()} for a vehicle that should not have any tasks.
     */
    @Test
    public void testHasScheduledTask() {
        System.out.println("hasScheduledTask");
        int vehicleId = 12;
        MaintenanceDAO instance = new MaintenanceDAO();
        boolean expResult = false;
        boolean result = instance.hasScheduledTask(vehicleId);
        assertEquals(expResult, result);
    }

    /**
     * Tests {@code insertMaintenanceTask()} for inserting a new task.
     */
    @Test
    public void testInsertMaintenanceTask() {
        System.out.println("insertMaintenanceTask");
        MaintenanceTask task = new MaintenanceTask();
        task.setVehicleId(8);
        task.setScheduledDatetime(Timestamp.valueOf(LocalDate.of(2025, 9, 30).atStartOfDay()));
        task.setDescription("JUnit test task");
        task.setCost(new BigDecimal("320.00"));
        task.setCompleted(false);

        MaintenanceDAO instance = new MaintenanceDAO();
        int expResult = -1;
        int result = instance.insertMaintenanceTask(task);
        assertEquals(expResult, result);
    }

    /**
     * Tests {@code hasScheduledTaskForVehicle()} for a vehicle that doesn't exist.
     */
    @Test
    public void testHasScheduledTaskForVehicle() {
        System.out.println("hasScheduledTaskForVehicle");
        int vehicleId = 10;
        MaintenanceDAO instance = new MaintenanceDAO();
        boolean expResult = false;
        boolean result = instance.hasScheduledTaskForVehicle(vehicleId);
        assertEquals(expResult, result);
    }

    /**
     * Tests {@code isDateAlreadyBooked()} for a specific vehicle and date.
     */
    @Test
    public void testIsDateAlreadyBooked() {
        System.out.println("isDateAlreadyBooked");
        int vehicleId = 3;
        LocalDate selectedDate = LocalDate.of(2025, 8, 1);
        MaintenanceDAO instance = new MaintenanceDAO();
        boolean expResult = false;
        boolean result = instance.isDateAlreadyBooked(vehicleId, selectedDate);
        assertEquals(expResult, result);
    }

    /**
     * Tests {@code isDateAlreadyTaken()} to check if a date is globally booked.
     */
    @Test
    public void testIsDateAlreadyTaken() {
        System.out.println("isDateAlreadyTaken");
        LocalDate selectedDate = LocalDate.of(2025, 8, 9);
        MaintenanceDAO instance = new MaintenanceDAO();
        boolean expResult = true;
        boolean result = instance.isDateAlreadyTaken(selectedDate);
        assertEquals(expResult, result);
    }

    /**
     * Tests {@code getAllBookedDates()} to retrieve all maintenance booking dates.
     */
    @Test
    public void testGetAllBookedDates() {
        System.out.println("getAllBookedDates");
        MaintenanceDAO instance = new MaintenanceDAO();
        List<LocalDate> expResult = null;
        List<LocalDate> result = instance.getAllBookedDates();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Tests {@code getBookedDatesForVehicle()} for a specific vehicle.
     */
    @Test
    public void testGetBookedDatesForVehicle() {
        System.out.println("getBookedDatesForVehicle");
        int vehicleId = 7;
        MaintenanceDAO instance = new MaintenanceDAO();
        List<LocalDate> expResult = Collections.singletonList(LocalDate.of(2025, 8, 9));
        List<LocalDate> result = instance.getBookedDatesForVehicle(vehicleId);
        assertEquals(expResult, result);
    }

    /**
     * Tests {@code updateTask()} to update an existing task's details.
     */
    @Test
    public void testUpdateTask() {
        System.out.println("updateTask");
        MaintenanceTask task = new MaintenanceTask();
        task.setTaskId(5);
        task.setVehicleId(5);
        task.setScheduledDatetime(Timestamp.valueOf(LocalDate.of(2025, 8, 2).atStartOfDay()));
        task.setDescription("Updated via JUnit");
        task.setCompleted(true);
        task.setCost(new BigDecimal("1200.00"));
        MaintenanceDAO instance = new MaintenanceDAO();
        boolean expResult = true;
        boolean result = instance.updateTask(task);
        assertEquals(expResult, result);
    }

    /**
     * Tests {@code deleteTask()} to remove a task by its ID.
     */
    @Test
    public void testDeleteTask() {
        System.out.println("deleteTask");
        int taskId = 16;
        MaintenanceDAO instance = new MaintenanceDAO();
        boolean expResult = false;
        boolean result = instance.deleteTask(taskId);
        assertEquals(expResult, result);
    }

    /**
     * Tests {@code getAllMaintenanceTasks()} to retrieve all tasks.
     */
    @Test
    public void testGetAllMaintenanceTasks() {
        System.out.println("getAllMaintenanceTasks");
        MaintenanceDAO instance = new MaintenanceDAO();
        List<MaintenanceTask> expResult = null;
        List<MaintenanceTask> result = instance.getAllMaintenanceTasks();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Tests {@code getTaskById()} to retrieve a task by its ID.
     */
    @Test
    public void testGetTaskById() {
        System.out.println("getTaskById");
        int taskId = 10;
        MaintenanceDAO instance = new MaintenanceDAO();
        MaintenanceTask expResult = null;
        MaintenanceTask result = instance.getTaskById(taskId);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Tests {@code isDateAlreadyBookedExcludingTask()} to check for conflicts excluding the current task.
     */
    @Test
    public void testIsDateAlreadyBookedExcludingTask() {
        System.out.println("isDateAlreadyBookedExcludingTask");
        int vehicleId = 8;
        LocalDate date = LocalDate.of(2025, 8, 13);
        int taskId = 15;
        MaintenanceDAO instance = new MaintenanceDAO();
        boolean expResult = false;
        boolean result = instance.isDateAlreadyBookedExcludingTask(vehicleId, date, taskId);
        assertEquals(expResult, result);
    }
}
