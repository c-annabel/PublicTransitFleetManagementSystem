package business.reports;

import com.google.gson.JsonObject;

public interface Report {
    /**
     * Generates a report in JSON format.
     *
     * @param startDate Start date filter (optional, can be null)
     * @param endDate End date filter (optional, can be null)
     * @param operatorId Operator ID for performance reports (0 if not applicable)
     * @return JsonObject containing report data
     */
    JsonObject generateReport(String startDate, String endDate, int operatorId);
}
