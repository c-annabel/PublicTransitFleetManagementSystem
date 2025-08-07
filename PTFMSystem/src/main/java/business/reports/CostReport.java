package business.reports;

import com.google.gson.JsonObject;
import dataaccess.ReportDAO;

/**
 * Generates a cost report in JSON format, including both fuel and maintenance costs.
 * 
 * This class implements the {@code Report} interface and uses {@code ReportDAO}
 * to fetch the relevant data based on a given date range. The results are returned
 * as a {@code JsonObject} containing labeled cost data.
 * 
 * This report does not consider the operator ID in its logic (parameter included for interface compatibility).
 * 
 * Output keys:
 * - {@code labels}: category labels (e.g., Fuel, Maintenance)
 * - {@code values}: corresponding cost values
 * - {@code error} (optional): error message if generation fails
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
public class CostReport implements Report {

    /**
     * Generates the cost report for a given date range.
     *
     * @param startDate  the start date of the report period (YYYY-MM-DD)
     * @param endDate    the end date of the report period (YYYY-MM-DD)
     * @param operatorId the ID of the operator (unused in this report)
     * @return a {@code JsonObject} containing cost labels and values, or an error message
     */
    @Override
    public JsonObject generateReport(String startDate, String endDate, int operatorId) {
        JsonObject json = new JsonObject();
        try {
            ReportDAO dao = new ReportDAO();

            // Cost Analysis (Fuel + Maintenance)
            String[] labels = dao.getCostAnalysisLabels();
            double[] values = dao.getCostAnalysisValues(startDate, endDate);

            json.add("labels", ReportUtils.toJsonArray(labels));
            json.add("values", ReportUtils.toJsonArray(values));

        } catch (Exception e) {
            e.printStackTrace();
            json.addProperty("error", "Failed to generate cost report.");
        }
        return json;
    }
}
