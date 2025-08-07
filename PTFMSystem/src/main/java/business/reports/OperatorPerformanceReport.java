package business.reports;

import com.google.gson.JsonObject;
import dataaccess.ReportDAO;

/**
 * Generates a report on a specific operator’s performance.
 * 
 * This class implements the {@code Report} interface and retrieves performance
 * metrics using the {@code ReportDAO}. The report includes the operator’s
 * on-time rate and efficiency score.
 * 
 * Output JSON structure:
 * <ul>
 *   <li>{@code onTimeRate} - percentage of trips arriving on time</li>
 *   <li>{@code efficiencyScore} - overall efficiency rating</li>
 * </ul>
 * 
 * If an error occurs during report generation, the returned JSON may be empty.
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
public class OperatorPerformanceReport implements Report {

    private final ReportDAO dao;

    /**
     * Constructs an {@code OperatorPerformanceReport} instance with a new {@code ReportDAO}.
     */
    public OperatorPerformanceReport() {
        this.dao = new ReportDAO();
    }

    /**
     * Generates an operator performance report based on the operator ID.
     * <p>
     * The date range parameters are accepted for interface compatibility but not used in this implementation.
     *
     * @param startDate  the start date of the report period (ignored)
     * @param endDate    the end date of the report period (ignored)
     * @param operatorId the ID of the operator whose performance is being reported
     * @return a {@code JsonObject} containing performance metrics
     */
    @Override
    public JsonObject generateReport(String startDate, String endDate, int operatorId) {
        JsonObject reportData = new JsonObject();
        try {
            double onTimeRate = dao.getOnTimeRate(operatorId);
            double efficiencyScore = dao.getEfficiencyScore(operatorId);

            reportData.addProperty("onTimeRate", onTimeRate);
            reportData.addProperty("efficiencyScore", efficiencyScore);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return reportData;
    }
}
