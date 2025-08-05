package business.reports;

import com.google.gson.JsonObject;
import dataaccess.ReportDAO;

public class MaintenanceReport implements Report {

    private final ReportDAO dao;

    public MaintenanceReport() {
        this.dao = new ReportDAO();
    }

    @Override
    public JsonObject generateReport(String startDate, String endDate, int operatorId) {
        JsonObject reportData = new JsonObject();
        try {
            // Summary: Completed vs Pending
            double[] summary = dao.getMaintenanceSummary(startDate, endDate);
            reportData.add("labels", ReportUtils.createJsonArray(new String[]{"Completed", "Pending"}));
            reportData.add("values", ReportUtils.createJsonArray(summary));

            // Optional: Maintenance trend (date vs cost)
            JsonObject trend = new JsonObject();
            trend.add("labels", ReportUtils.createJsonArray(dao.getMaintenanceCostLabels(startDate, endDate)));
            trend.add("values", ReportUtils.createJsonArray(dao.getMaintenanceCostValues(startDate, endDate)));
            reportData.add("trend", trend);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return reportData;
    }
}
