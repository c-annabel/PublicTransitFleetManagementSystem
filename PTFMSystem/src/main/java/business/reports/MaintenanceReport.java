package business.reports;

import com.google.gson.JsonObject;
import dataaccess.ReportDAO;

public class MaintenanceReport implements Report {

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
