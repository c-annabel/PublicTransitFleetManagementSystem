package business.reports;

import com.google.gson.JsonObject;
import dataaccess.ReportDAO;

public class CostReport implements Report {

    private final ReportDAO dao;

    public CostReport() {
        this.dao = new ReportDAO();
    }

    @Override
    public JsonObject generateReport(String startDate, String endDate, int operatorId) {
        JsonObject reportData = new JsonObject();
        try {
            // Labels for cost breakdown
            String[] labels = dao.getCostAnalysisLabels();
            double[] values = dao.getCostAnalysisValues(startDate, endDate);

            reportData.add("labels", ReportUtils.createJsonArray(labels));
            reportData.add("values", ReportUtils.createJsonArray(values));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return reportData;
    }
}
