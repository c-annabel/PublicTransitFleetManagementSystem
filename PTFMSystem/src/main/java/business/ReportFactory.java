package business;

import business.reports.CostReport;
import business.reports.MaintenanceReport;
import business.reports.OperatorPerformanceReport;
import business.reports.Report;

public class ReportFactory {

    public static Report getReport(String reportType) {
        if (reportType == null) {
            throw new IllegalArgumentException("Report type cannot be null");
        }

        switch (reportType) {
            case "maintenance":
                return new MaintenanceReport();
            case "cost":
                return new CostReport();
            case "operatorPerformance":
                return new OperatorPerformanceReport();
            default:
                throw new IllegalArgumentException("Invalid report type: " + reportType);              
        }
    }
}
