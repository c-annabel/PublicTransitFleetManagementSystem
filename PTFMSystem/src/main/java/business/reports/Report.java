package business.reports;

import com.google.gson.JsonObject;

/**
 * Interface for generating JSON-based reports.
 * 
 * Implementations of this interface are responsible for retrieving
 * and formatting data into a {@code JsonObject} that can be used by
 * the frontend or reporting modules. The parameters allow for filtering
 * by date range or targeting specific operators.
 * 
 * This abstraction supports multiple types of reports, such as cost,
 * maintenance, or operator performance.
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
public interface Report {

    /**
     * Generates a report in JSON format.
     *
     * @param startDate   the start date of the report range (optional, may be null)
     * @param endDate     the end date of the report range (optional, may be null)
     * @param operatorId  the operator ID for operator-specific reports (0 if not applicable)
     * @return a {@code JsonObject} containing the generated report data
     */
    JsonObject generateReport(String startDate, String endDate, int operatorId);
}
