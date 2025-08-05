package business.reports;

import com.google.gson.JsonObject;
import dataaccess.ReportDAO;

public class OperatorPerformanceReport implements Report {

    private final ReportDAO dao;

    public OperatorPerformanceReport() {
        this.dao = new ReportDAO();
    }

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
