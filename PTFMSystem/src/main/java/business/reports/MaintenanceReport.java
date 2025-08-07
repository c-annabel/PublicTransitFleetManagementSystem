package business.reports;

import com.google.gson.JsonObject;
import dataaccess.ReportDAO;

/**
 * Generates a maintenance report in JSON format including summary statistics and cost trends.
 * 
 * This class implements the {@code Report} interface and fetches data from {@code ReportDAO}
 * for a given time range. The report includes both the number of completed vs pending tasks,
 * and the trend of maintenance costs over time.
 * 
 * Output JSON structure:
 * <ul>
 *   <li>{@code labels} - array with "Completed" and "Pending"</li>
 *   <li>{@code values} - corresponding values for the maintenance summary</li>
 *   <li>{@code trend} - a nested JSON object containing:
 *     <ul>
 *       <li>{@code labels} - date or time labels</li>
 *       <li>{@code values} - cost values for each time point</li>
 *     </ul>
 *   </li>
 *   <li>{@code error} - included if the report generation fails</li>
 * </ul>
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
public class MaintenanceReport implements Report {

    /**
     * Generates a maintenance report for the specified date range.
     *
     * @param startDate  the start date of the report period (format: YYYY-MM-DD)
     * @param endDate    the end date of the report period (format: YYYY-MM-DD)
     * @param operatorId the ID of the operator (not used in this report)
     * @return a {@code JsonObject} containing summary and trend data, or an error message
     */
    @Override
    public JsonObject generateReport(String startDate, String endDate, int operatorId) {
        JsonObject json = new JsonObject();
        try {
            ReportDAO dao = new ReportDAO();

            // Maintenance Summary
            double[] summary = dao.getMaintenanceSummary(startDate, endDate);
            json.add("labels", ReportUtils.toJsonArray(new String[]{"Completed", "Pending"}));
            json.add("values", ReportUtils.toJsonArray(summary));

            // Maintenance Trend
            String[] trendLabels = dao.getMaintenanceCostLabels(startDate, endDate);
            double[] trendValues = dao.getMaintenanceCostValues(startDate, endDate);

            JsonObject trend = new JsonObject();
            trend.add("labels", ReportUtils.toJsonArray(trendLabels));
            trend.add("values", ReportUtils.toJsonArray(trendValues));

            json.add("trend", trend);

        } catch (Exception e) {
            e.printStackTrace();
            json.addProperty("error", "Failed to generate maintenance report.");
        }
        return json;
    }
}
