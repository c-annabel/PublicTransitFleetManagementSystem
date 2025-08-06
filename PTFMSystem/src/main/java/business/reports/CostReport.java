package business.reports;

import com.google.gson.JsonObject;
import dataaccess.ReportDAO;

public class CostReport implements Report {

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
